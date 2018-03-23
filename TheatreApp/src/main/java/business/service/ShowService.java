package business.service;

import business.model.Show;

import java.util.List;

public interface ShowService {

    public List<Show> findAll();
    public Show getById(int id);
    public int create(Show show);
    public boolean update(Show show);
    public boolean delete(Show show);
    public List<Show> searchByTitle(String title);
}
