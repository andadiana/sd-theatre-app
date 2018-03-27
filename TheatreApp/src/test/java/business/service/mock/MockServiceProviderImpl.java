package business.service.mock;

import business.service.*;

public class MockServiceProviderImpl implements ServiceProvider {
    public SeatService getSeatService() {
        return new MockSeatServiceImpl();
    }

    public ShowService getShowService() {
        return new MockShowServiceImpl();
    }

    public TicketService getTicketService() {
        return null;
    }

    public LoginService getLoginService() {
        return null;
    }

    public CashierService getCashierService() {
        return null;
    }
}
