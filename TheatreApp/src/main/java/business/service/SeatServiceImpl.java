package business.service;

import business.model.Seat;
import dataaccess.dbmodel.SeatDTO;
import dataaccess.repository.SeatRepository;

import java.util.ArrayList;
import java.util.List;

public class SeatServiceImpl implements SeatService {

    private SeatRepository repository;

    public SeatServiceImpl(SeatRepository seatRepository) {
        this.repository = seatRepository;
    }

    public List<Seat> findAll() {
        List<SeatDTO> seatDTOS = repository.findAll();
        List<Seat> seats = new ArrayList<>();
        for (SeatDTO seat: seatDTOS) {
            seats.add(dtoToSeat(seat));
        }
        return seats;
    }

    public Seat getById(int id) {
        SeatDTO seatDTO = repository.getById(id);
        return dtoToSeat(seatDTO);
    }

    public Seat getByPosition(int rowNr, int seatNr) {
        SeatDTO seatDTO = repository.getByPosition(rowNr, seatNr);
        return dtoToSeat(seatDTO);
    }

    private Seat dtoToSeat(SeatDTO seatDTO) {
        Seat seat = new Seat();
        seat.setId(seatDTO.getId());
        seat.setRowNr(seatDTO.getRowNr());
        seat.setSeatNr(seatDTO.getSeatNr());
        return seat;
    }

}
