package business.service;

import business.model.Seat;
import business.model.Show;
import business.model.Ticket;
import dataaccess.dbmodel.TicketDTO;
import dataaccess.repository.TicketRepository;
import dataaccess.repository.TicketRepositoryCacheDecorator;
import dataaccess.repository.TicketRepositoryMySql;

import java.util.ArrayList;
import java.util.List;

public class TicketServiceImpl implements TicketService {

    private TicketRepository repository;


    public TicketServiceImpl() {
        this.repository = new TicketRepositoryCacheDecorator(new TicketRepositoryMySql());
    }

    public void createTicketsForShow(Show show) {
        for (int i = 1; i <= SeatService.THEATRE_ROWS; i++) {
            for (int j = 1; j <= SeatService.THEATRE_COLS; j++) {
                SeatService seatService = new SeatServiceImpl();
                Seat seat = seatService.getByPosition(i, j);
                Ticket ticket = new Ticket();
                ticket.setReserved(false);
                ticket.setSeat(seat);
                ticket.setShow(show);
                repository.create(ticketToDTO(ticket));
            }
        }
    }

    public List<Ticket> findAllTicketsForShow(Show show) {
        List<TicketDTO> ticketDTOS = repository.getByShowId(show.getId());
        List<Ticket> tickets = new ArrayList<>();
        for (TicketDTO ticket: ticketDTOS) {
            tickets.add(dtoToTicket(ticket));
        }
        return tickets;
    }

    public boolean editSeat(Ticket ticket, Seat newSeat) {
        Show show = ticket.getShow();
        ticket.setReserved(false);
        Ticket newTicket = new Ticket();
        newTicket.setShow(show);
        newTicket.setReserved(true);
        newTicket.setSeat(newSeat);
        TicketDTO newTicketDTO = ticketToDTO(newTicket);
        TicketDTO ticketDTO = ticketToDTO(ticket);
        repository.update(ticketDTO);
        return repository.update(newTicketDTO);
    }

    public boolean cancelReservation(Ticket ticket) {
        TicketDTO ticketDTO = ticketToDTO(ticket);
        ticketDTO.setReserved(false);
        return repository.update(ticketDTO);
    }

    private Ticket dtoToTicket(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        ticket.setId(ticketDTO.getId());
        SeatService seatService = new SeatServiceImpl();
        ticket.setSeat(seatService.getById(ticketDTO.getSeatId()));
        ShowService showService = new ShowServiceImpl();
        ticket.setShow(showService.getById(ticketDTO.getShowId()));
        ticket.setReserved(ticketDTO.getReserved());
        return ticket;
    }

    private TicketDTO ticketToDTO(Ticket ticket) {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticket.getId());
        ticketDTO.setReserved(ticket.isReserved());
        ticketDTO.setShowId(ticket.getShow().getId());
        ticketDTO.setSeatId(ticket.getSeat().getId());
        return ticketDTO;
    }
}
