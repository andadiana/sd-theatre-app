package dataaccess.dbmodel;

import java.sql.Timestamp;

public class ShowDTO {
    private int id;
    private String title;
    private String genre;
    private String cast;
    private Timestamp date;
    private int nrTickets;

    public ShowDTO() {
        this.id = 0;
        this.title = null;
        this.genre = null;
        this.cast = null;
        this.date = null;
        this.nrTickets = 0;
    }

    public ShowDTO(String title, String genre, String cast, Timestamp date, int nrTickets) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.cast = cast;
        this.date = date;
        this.nrTickets = nrTickets;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getGenre(){
        return genre;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getCast(){
        return cast;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setNrTickets(int nrTickets) {
        this.nrTickets = nrTickets;
    }

    public int getNrTickets() {
        return nrTickets;
    }

    public String toString() {
        return "Show " + title + "(" + genre + ") : " + date;
    }
}
