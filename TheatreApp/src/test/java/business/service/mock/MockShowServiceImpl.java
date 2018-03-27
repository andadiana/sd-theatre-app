package business.service.mock;

import business.model.Show;
import business.service.ShowService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MockShowServiceImpl implements ShowService {

    private List<Show> shows;
    private int currentId;

    public MockShowServiceImpl() {
        shows = new ArrayList<>();
        Show s1 = new Show();
        s1.setId(1);
        s1.setNrTickets(3);
        s1.setDate(new Timestamp(System.currentTimeMillis()));
        s1.setTitle("s1");
        s1.setGenre(Show.Genre.Opera);
        s1.setCast("cast1");
        Show s2 = new Show();
        s2.setId(2);
        s2.setNrTickets(3);
        s2.setDate(new Timestamp(System.currentTimeMillis()));
        s2.setTitle("s2");
        s2.setGenre(Show.Genre.Opera);
        s2.setCast("cast2");
        Show s3 = new Show();
        s3.setId(1);
        s3.setNrTickets(3);
        s3.setDate(new Timestamp(System.currentTimeMillis()));
        s3.setTitle("s3");
        s3.setGenre(Show.Genre.Ballet);
        s3.setCast("cast3");
        shows.add(s1);
        shows.add(s2);
        shows.add(s3);
        currentId = 3;
    }

    public List<Show> findAll() {
        return shows;
    }

    public Show getById(int id) {
        return shows.stream().filter(s -> s.getId() == id).findFirst().get();
    }

    public int create(Show show) {
        currentId++;
        show.setId(currentId);
        shows.add(show);
        return currentId;
    }

    public boolean update(Show show) {
        Show s = getById(show.getId());
        if (s != null) {
            s = show;
            return true;
        }
        return false;
    }

    public boolean delete(Show show) {
        return shows.remove(show);
    }

    public List<Show> searchByTitle(String title) {
        return shows.stream().filter(s -> s.getTitle() == title).collect(Collectors.toList());
    }
    public List<Show> getAllAvailable(Timestamp timestamp) {
        return shows.stream().filter(s -> s.getDate().after(new Timestamp(System.currentTimeMillis()))).collect(Collectors.toList());
    }
}
