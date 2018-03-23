package dataaccess.repository;

import dataaccess.dbmodel.ShowDTO;

import java.util.List;

public interface ShowRepository {

    public List<ShowDTO> findAll();
    public ShowDTO getById(int id);
    public int create(ShowDTO show);
    public boolean update(ShowDTO show);
    public boolean delete(ShowDTO show);
    public List<ShowDTO> searchByTitle(String title);

}
