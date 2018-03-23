package dataaccess.dbmodel;

public class SeatDTO {

    private int id;
    private int rowNr;
    private int seatNr;

    public int getRowNr() {
        return rowNr;
    }

    public int getSeatNr() {
        return seatNr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRowNr(int rowNr) {
        this.rowNr = rowNr;
    }

    public void setSeatNr(int seatNr) {
        this.seatNr = seatNr;
    }

    public String toString() {
        return id + " " + rowNr + " " + seatNr;
    }
}
