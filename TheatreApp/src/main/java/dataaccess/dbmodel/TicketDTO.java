package dataaccess.dbmodel;

public class TicketDTO {

    private int id;
    private int seatId;
    private int showId;
    private Boolean reserved;

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

    public Boolean getReserved() {
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

}
