package dataaccess.repository;

import dataaccess.dbmodel.TicketDTO;

import java.util.List;

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
