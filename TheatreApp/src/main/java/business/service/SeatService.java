package business.service;

import business.model.Seat;

import java.util.List;

public interface SeatService {

    public static final int THEATRE_ROWS = 10;
    public static final int THEATRE_COLS = 10;

    public List<Seat> findAll();
    public Seat getById(int id);
    public Seat getByPosition(int rowNr, int seatNr);
}
