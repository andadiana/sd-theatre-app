package business.service;

import business.model.Show;
import business.service.mock.MockTicketRepositoryImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TicketServiceTest {

    private static TicketService ticketService;
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ticketService = new TicketServiceImpl(new MockTicketRepositoryImpl());
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
}
