package business.model;

import java.sql.Timestamp;

public class Show {

    private int id;
    private String title;
    private Genre genre;
    private String cast;
    private Timestamp date;
    private int nrTickets;

    public enum Genre {
        Opera,
        Ballet
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getCast() {
        return cast;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setNrTickets(int nrTickets) {
        this.nrTickets = nrTickets;
    }

    public int getNrTickets() {
        return nrTickets;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String toString() {
        return title + " " + genre + " " + date;
    }

}
