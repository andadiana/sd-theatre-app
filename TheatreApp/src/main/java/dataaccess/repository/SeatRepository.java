package dataaccess.repository;

import dataaccess.dbmodel.SeatDTO;

import java.util.List;

public interface SeatRepository {

    public List<SeatDTO> findAll();
    public SeatDTO getById(int id);
    public int create(SeatDTO seat);
    public boolean update(SeatDTO seat);
    public boolean delete(SeatDTO seat);
}
