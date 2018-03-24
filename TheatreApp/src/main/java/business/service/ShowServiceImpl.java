package business.service;

import business.model.Show;
import dataaccess.DBConnection;
import dataaccess.dbmodel.ShowDTO;
import dataaccess.repository.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShowServiceImpl implements ShowService {

    private ShowRepository repository;

    public ShowServiceImpl() {
        this.repository = new ShowRepositoryMySql();
    }

    public List<Show> findAll() {
        List<ShowDTO> showDTOS = repository.findAll();
        List<Show> shows = new ArrayList<>();
        for (ShowDTO show: showDTOS) {
            shows.add(dtoToShow(show));
        }
        return shows;
    }

    public Show getById(int id) {
        ShowDTO showDTO = repository.getById(id);
        return dtoToShow(showDTO);
    }

    public int create(Show show) {
        ShowDTO showDTO = showToDto(show);
        int showId = repository.create(showDTO);
        TicketService ticketService = new TicketServiceImpl();
        ticketService.createTicketsForShow(show);
        return showId;
    }

    public boolean update(Show show) {
        ShowDTO showDTO = showToDto(show);
        return repository.update(showDTO);
    }

    public boolean delete(Show show) {
        ShowDTO showDTO = showToDto(show);
        //TODO: decide what to do with tickets
        return repository.delete(showDTO);
    }

    public List<Show> searchByTitle(String title) {
        List<ShowDTO> showDTOS = repository.searchByTitle(title);
        List<Show> shows = new ArrayList<>();
        for (ShowDTO show: showDTOS) {
            shows.add(dtoToShow(show));
        }
        return shows;

    }

    public List<Show> getAllAvailable(Timestamp timestamp) {
        List<ShowDTO> showDTOS = repository.findAll();
        return showDTOS.stream().filter((s) -> timestamp.before(s.getDate())).map((s) -> dtoToShow(s)).collect(Collectors.toList());
    }

    private Show dtoToShow(ShowDTO showDTO) {
        Show show = new Show();
        show.setId(showDTO.getId());
        show.setCast(showDTO.getCast());
        show.setDate(showDTO.getDate());
        show.setNrTickets(showDTO.getNrTickets());
        show.setTitle(showDTO.getTitle());
        switch (showDTO.getGenre()) {
            case "OPERA": show.setGenre(Show.Genre.Opera);
                          break;
            case "BALLET": show.setGenre(Show.Genre.Ballet);
                           break;
            default: show.setGenre(Show.Genre.Opera);
        }
        return show;
    }

    private ShowDTO showToDto(Show show) {
        ShowDTO showDTO = new ShowDTO();
        showDTO.setId(show.getId());
        showDTO.setTitle(show.getTitle());
        showDTO.setDate(show.getDate());
        showDTO.setNrTickets(show.getNrTickets());
        showDTO.setCast(show.getCast());
        switch (show.getGenre()) {
            case Opera: {
                showDTO.setGenre("OPERA");
                break;
            }
            case Ballet: {
                showDTO.setGenre("BALLET");
                break;
            }
        }
        return showDTO;
    }
}
