package business.model;

public class Ticket {

    private int id;
    private Seat seat;
    private Show show;
    private boolean reserved;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public Show getShow() {
        return show;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public boolean isReserved() {
        return reserved;
    }

    public String toString() {
        return seat.toString();
    }
}
