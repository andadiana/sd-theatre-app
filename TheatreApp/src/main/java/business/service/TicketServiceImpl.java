package business.service;

import business.model.Seat;
import business.model.Show;
import business.model.Ticket;
import dataaccess.dbmodel.TicketDTO;
import dataaccess.repository.*;

import java.util.ArrayList;
import java.util.List;

public class TicketServiceImpl implements TicketService {

    private TicketRepository repository;
    private ServiceProvider serviceProvider;

    public TicketServiceImpl(TicketRepository repository, ServiceProvider serviceProvider) {
        this.repository = repository;
        this.serviceProvider = serviceProvider;
    }

    public void createTicketsForShow(Show show) {
        for (int i = 1; i <= SeatService.THEATRE_ROWS; i++) {
            for (int j = 1; j <= SeatService.THEATRE_COLS; j++) {
                SeatService seatService = serviceProvider.getSeatService();
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

    public Ticket getById(int id) {
        TicketDTO ticketDTO = repository.getById(id);
        return dtoToTicket(ticketDTO);
    }

    public Ticket findSeatTicketForShow(Show show, int rowNr, int seatNr) {
        SeatService seatService = serviceProvider.getSeatService();
        Seat seat = seatService.getByPosition(rowNr, seatNr);
        TicketDTO ticketDTO = repository.findSeatTicketForShow(show.getId(), seat.getId());
        return dtoToTicket(ticketDTO);
    }

    public boolean editSeat(Ticket ticket, int rowNr, int seatNr) {
        Show show = ticket.getShow();
        SeatService seatService = serviceProvider.getSeatService();
        Seat newSeat = seatService.getByPosition(rowNr, seatNr);
        //check if there that seat is occupied for the show
        if (repository.findSeatTicketForShow(show.getId(), newSeat.getId()) == null) {
            ticket.setSeat(newSeat);
            TicketDTO ticketDTO = ticketToDTO(ticket);
            return repository.update(ticketDTO);
        }
        else {
            return false;
        }
    }

    public boolean cancelReservation(Ticket ticket) {
        TicketDTO ticketDTO = ticketToDTO(ticket);
        return repository.delete(ticketDTO);
    }

    public Ticket reserveTicket(Show show, int rowNr, int seatNr) {
        SeatService seatService = serviceProvider.getSeatService();
        Seat reservedSeat = seatService.getByPosition(rowNr, seatNr);
        Ticket ticket = new Ticket();
        ticket.setShow(show);
        ticket.setSeat(reservedSeat);
        ticket.setReserved(true);
        TicketDTO ticketDTO = ticketToDTO(ticket);
        int id = repository.create(ticketDTO);
        ticket.setId(id);
        return ticket;
    }

    public List<Ticket> findSoldTicketsForShow(Show show) {
        //TODO same as find all tickets for show?
        List<TicketDTO> ticketDTOS = repository.findSoldTicketsForShow(show.getId());
        List<Ticket> tickets = new ArrayList<>();
        for (TicketDTO ticket: ticketDTOS) {
            tickets.add(dtoToTicket(ticket));
        }
        return tickets;
    }

    public boolean nrTicketsExceeded(Show show) {
        //TODO call findalltickets for show instead of findsoldtickets?
        List<TicketDTO> soldTickets = repository.findSoldTicketsForShow(show.getId());
        if (soldTickets.size() >= show.getNrTickets()) {
            return true;
        }
        return false;
    }

    public boolean deleteAllTicketsForShow(Show show) {
        return repository.deleteAllTicketsForShow(show.getId());
    }

    private Ticket dtoToTicket(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        ticket.setId(ticketDTO.getId());
        SeatService seatService = serviceProvider.getSeatService();
        ticket.setSeat(seatService.getById(ticketDTO.getSeatId()));
        ShowService showService = serviceProvider.getShowService();
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
