package dataaccess.repository;

import dataaccess.dbmodel.TicketDTO;

import java.util.List;
import java.util.stream.Collectors;

public class TicketRepositoryCacheDecorator implements TicketRepository {

    private TicketRepository ticketRepository;
    private Cache<TicketDTO> ticketCache;

    public TicketRepositoryCacheDecorator(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketCache = new Cache<TicketDTO>();
    }

    public List<TicketDTO> findAll() {
        if (!ticketCache.isEmpty()) {
            return ticketCache.load();
        }
        List<TicketDTO> tickets = ticketRepository.findAll();
        ticketCache.save(tickets);
        return tickets;
    }

    public TicketDTO getById(int id) {
        if (!ticketCache.isEmpty()) {
            return ticketCache.load().stream().filter(x -> x.getId() == id).findFirst().get();
        }
        return ticketRepository.getById(id);
    }

    public TicketDTO findSeatTicketForShow(int showId, int seatId) {
        if (!ticketCache.isEmpty()) {
            return ticketCache.load().stream().filter(x -> (x.getShowId() == showId && x.getSeatId() == seatId)).findFirst().get();
        }
        return ticketRepository.findSeatTicketForShow(showId, seatId);
    }

    public List<TicketDTO> getByShowId(int showId) {
        if (!ticketCache.isEmpty()) {
            return ticketCache.load().stream().filter(x -> x.getShowId() == showId).collect(Collectors.toList());
        }
        return ticketRepository.getByShowId(showId);
    }

    public int create(TicketDTO ticket) {
        ticketCache.restoreCache();
        return ticketRepository.create(ticket);
    }

    public boolean update(TicketDTO ticket) {
        ticketCache.restoreCache();
        return ticketRepository.update(ticket);
    }

    public boolean delete(TicketDTO ticket) {
        ticketCache.restoreCache();
        return ticketRepository.delete(ticket);
    }

}
