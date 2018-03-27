package business.service;

import dataaccess.repository.*;

public class ServiceProviderImpl implements ServiceProvider {

    private SeatRepository seatRepository;
    private ShowRepository showRepository;
    private TicketRepository ticketRepository;
    private UserRepository userRepository;

    public ServiceProviderImpl() {
        this.seatRepository = new SeatRepositoryMySql();
        this.showRepository = new ShowRepositoryMySql();
        this.ticketRepository = new TicketRepositoryCacheDecorator(new TicketRepositoryMySql());
        this.userRepository = new UserRepositoryMySql();
    }

    public SeatService getSeatService() {
        return new SeatServiceImpl(seatRepository);
    }

    public ShowService getShowService() {
        return new ShowServiceImpl(showRepository);
    }

    public TicketService getTicketService() {
        return new TicketServiceImpl(ticketRepository, this);
    }

    public LoginService getLoginService() {
        return new LoginServiceImpl(userRepository);
    }

    public CashierService getCashierService() {
        return new CashierServiceImpl(userRepository);
    }
}
