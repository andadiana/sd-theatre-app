package business.model;

public class Seat {

    private int id;
    private int rowNr;
    private int seatNr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeatNr() {
        return seatNr;
    }

    public void setSeatNr(int seatNr) {
        this.seatNr = seatNr;
    }

    public int getRowNr() {
        return rowNr;
    }

    public void setRowNr(int rowNr) {
        this.rowNr = rowNr;
    }

    public String toString() {
        return "row " + rowNr + " seat " + seatNr;
    }
}
