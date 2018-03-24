package dataaccess.repository;

import dataaccess.dbmodel.TicketDTO;

import java.util.List;

public interface TicketRepository {

    public List<TicketDTO> findAll();
    public TicketDTO getById(int id);
    public List<TicketDTO> getByShowId(int showId);
    public int create(TicketDTO ticket);
    public boolean update(TicketDTO ticket);
    public boolean delete(TicketDTO ticket);
}
