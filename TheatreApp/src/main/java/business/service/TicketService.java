package business.service;

import business.model.Show;
import business.model.Ticket;

import java.util.List;

public interface TicketService {

    public void createTicketsForShow(Show show);
    public List<Ticket> findAllTicketsForShow(Show show);
    public boolean editSeat(Ticket ticket, int rowNr, int seatNr);
    public boolean cancelReservation(Ticket ticket);
    public Ticket findSeatTicketForShow(Show show, int rowNr, int seatNr);
    public Ticket reserveTicket(Show show, int rowNr, int seatNr);
    public boolean nrTicketsExceeded(Show show);
    public List<Ticket> findSoldTicketsForShow(Show show);
    public boolean deleteAllTicketsForShow(Show show);
    public Ticket getById(int id);
}
