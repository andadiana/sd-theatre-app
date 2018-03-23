package dataaccess.dbmodel;

public class TicketDTO {

    private int id;
    private int seatId;
    private int showId;
    private boolean reserved;

    public TicketDTO() {
        this.id = 0;
        this.seatId = 0;
        this.showId = 0;
        this.reserved = false;
    }

    public TicketDTO(int id, int seatId, int showId, boolean reserved) {
        this.id = id;
        this.showId = showId;
        this.seatId = seatId;
        this.reserved = reserved;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getSeatId() {
        return seatId;
    }

    public int getShowId() {
        return showId;
    }

    public boolean getReserved() {
        return reserved;
    }

    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public String toString() {
        return id + " " + seatId + " " + showId + " " + reserved;
    }

}
