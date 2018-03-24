package business.service;

import business.model.Seat;
import business.model.Show;
import business.model.Ticket;

import java.util.List;

public interface TicketService {

    public void createTicketsForShow(Show show);
    public List<Ticket> findAllTicketsForShow(Show show);
    public boolean editSeat(Ticket ticket, Seat newSeat);
    public boolean cancelReservation(Ticket ticket);
}
