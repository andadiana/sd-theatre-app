package business.service.mock;

import business.model.Ticket;
import dataaccess.dbmodel.TicketDTO;
import dataaccess.repository.TicketRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MockTicketRepositoryImpl implements TicketRepository {

    private List<TicketDTO> tickets;
    private int currentId;

    public MockTicketRepositoryImpl() {
        tickets = new ArrayList<>();
        tickets.add(new TicketDTO(1,1,1,false));
        tickets.add(new TicketDTO(2,2,1,true));
        tickets.add(new TicketDTO(3,3,1,true));
        tickets.add(new TicketDTO(4,1,2,true));
        tickets.add(new TicketDTO(5,2,2,true));
        tickets.add(new TicketDTO(6,3,2,true));
        tickets.add(new TicketDTO(7,1,3,true));
        tickets.add(new TicketDTO(8,2,3,true));
        tickets.add(new TicketDTO(9,3,3,true));
        currentId = 9;
    }

    public List<TicketDTO> findAll() {
        return tickets;
    }

    public TicketDTO getById(int id) {
        return tickets.stream().filter(t -> t.getId() == id).findFirst().get();
    }

    public List<TicketDTO> getByShowId(int showId) {
        return tickets.stream().filter(t -> t.getShowId() == showId).collect(Collectors.toList());
    }

    public int create(TicketDTO ticket) {
        currentId++;
        ticket.setId(currentId);
        tickets.add(ticket);
        return currentId;
    }

    public boolean update(TicketDTO ticket) {
        TicketDTO t = getById(ticket.getId());
        if (t != null) {
            t = ticket;
            return true;
        }
        return false;
    }

    public boolean delete(TicketDTO ticket) {
        return tickets.remove(ticket);
    }

    public TicketDTO findSeatTicketForShow(int showId, int seatId) {
        return tickets.stream().filter(t -> t.getShowId() == showId && t.getSeatId() == seatId).findFirst().get();
    }

    public List<TicketDTO> findSoldTicketsForShow(int showId) {
        return tickets.stream().filter(t -> t.getShowId() == showId && t.getReserved()).collect(Collectors.toList());
    }

    public boolean deleteAllTicketsForShow(int showId) {
        tickets =  tickets.stream().filter(t -> t.getShowId() != showId).collect(Collectors.toList());
        return true;
    }
}
