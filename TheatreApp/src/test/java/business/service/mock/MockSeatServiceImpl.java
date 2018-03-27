package business.service.mock;

import business.model.Seat;
import business.service.SeatService;

import java.util.ArrayList;
import java.util.List;

public class MockSeatServiceImpl implements SeatService {

    private List<Seat> seats;
    private int currentId;

    public MockSeatServiceImpl() {
        seats = new ArrayList<>();
        seats.add(new Seat(1, 1, 1));
        seats.add(new Seat(2, 3, 1));
        seats.add(new Seat(3, 2, 2));
        seats.add(new Seat(4, 4,4));
        currentId = 3;
    }

    public List<Seat> findAll() {
        return seats;

    }
    public Seat getById(int id) {
        return seats.stream().filter(s -> s.getId() == id).findFirst().get();
    }

    public Seat getByPosition(int rowNr, int seatNr) {
        return seats.stream().filter(s -> s.getRowNr() == rowNr && s.getSeatNr() == seatNr).findFirst().get();
    }
}
