package business.service;

public interface ServiceProvider {

    public SeatService getSeatService();

    public ShowService getShowService();

    public TicketService getTicketService();

    public LoginService getLoginService();

    public CashierService getCashierService();
}
