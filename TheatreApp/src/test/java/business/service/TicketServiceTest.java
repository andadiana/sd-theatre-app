package business.service;

import business.model.Seat;
import business.model.Show;
import business.model.Ticket;
import business.service.mock.MockSeatServiceImpl;
import business.service.mock.MockServiceProviderImpl;
import business.service.mock.MockTicketRepositoryImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TicketServiceTest {

    private static TicketService ticketService;
    private static SeatService seatService;
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ticketService = new TicketServiceImpl(new MockTicketRepositoryImpl(), new MockServiceProviderImpl());
        seatService = new MockSeatServiceImpl();
    }

    @Test
    public void nrTicketsExceeded_NotExceeded_False() {
        Show show = new Show();
        show.setId(1);
        show.setNrTickets(5);

        //Test method
        boolean exceeded = ticketService.nrTicketsExceeded(show);

        //Verify result
        assertFalse("Number of tickets not exceeded", exceeded);

    }

    @Test
    public void nrTicketsExceeded_Exceeded_True() {
        Show show = new Show();
        show.setId(2);
        show.setNrTickets(3);

        //Test method
        boolean exceeded = ticketService.nrTicketsExceeded(show);

        //Verify result
        assertTrue("Number of tickets exceeded", exceeded);

    }

    @Test
    public void editSeat_existingSeat_Success() {
        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setSeat(new Seat(1, 1, 1));
        Show show = new Show();
        show.setId(1);
        ticket.setShow(show);

        boolean res = ticketService.editSeat(ticket, 4, 4);

        Seat seat = seatService.getByPosition(4, 4);
        Ticket updatedTicket = ticketService.getById(ticket.getId());
        Seat updatedSeat = updatedTicket.getSeat();
        assertTrue("Seat has been modified ", (seat.getId() == updatedSeat.getId()));
    }

    @Test
    public void editSeat_seatReserved_Failure() {
        Ticket ticket = new Ticket();
        ticket.setId(2);
        ticket.setSeat(new Seat(2, 3, 1));
        Show show = new Show();
        show.setId(1);
        ticket.setShow(show);

        boolean res = ticketService.editSeat(ticket, 2, 2);
        assertFalse("Seat could not be modified, is already reserved ", res);
    }

}
