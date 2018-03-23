package dataaccess.repository;

import dataaccess.DBConnection;
import dataaccess.dbmodel.ShowDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShowRepositoryMySql implements ShowRepository {

    private final DBConnection dbConnection;

    public ShowRepositoryMySql(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<ShowDTO> findAll() {
        Connection connection = dbConnection.getConnection();
        List<ShowDTO> shows = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM `show`";
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()) {
                shows.add(getShowFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shows;
    }

    public ShowDTO getById(int id) {
        Connection connection = dbConnection.getConnection();
        ShowDTO show = null;
        try {
            String query = "SELECT * FROM `show` WHERE show_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            System.out.println(statement);
            ResultSet rs = statement.executeQuery();
            if (rs.next())
                show = getShowFromResultSet(rs);
            else {
                System.out.println("No results");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return show;
    }


    public int create(ShowDTO show) {
        Connection connection = dbConnection.getConnection();
        try {
            String query = "INSERT INTO `show` (title, genre, cast, date, nr_tickets) " +
                    "VALUES (?, ?, ?, ? ,?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, show.getTitle());
            statement.setString(2, show.getGenre());
            statement.setString(3, show.getCast());
            statement.setTimestamp(4, show.getDate());
            statement.setInt(5, show.getNrTickets());
            System.out.println(statement);
            int id = statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean update(ShowDTO show) {
        Connection connection = dbConnection.getConnection();
        try {
            String query = "UPDATE `show` SET title=?, genre=?, cast=?, date=?, nr_tickets=? " +
                    "WHERE show_id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, show.getTitle());
            statement.setString(2, show.getGenre());
            statement.setString(3, show.getCast());
            statement.setTimestamp(4, show.getDate());
            statement.setInt(5, show.getNrTickets());
            statement.setInt(6, show.getId());
            System.out.println(statement);
            statement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean delete(ShowDTO show) {
        Connection connection = dbConnection.getConnection();
        try {
            String query = "DELETE FROM `show` WHERE show_id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, show.getId());
            System.out.println(statement);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<ShowDTO> searchByTitle(String title) {
        Connection connection = dbConnection.getConnection();
        List<ShowDTO> shows = new ArrayList<>();
        try {
            String query = "SELECT * FROM `show` WHERE title LIKE %?%";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, title);
            System.out.println(statement);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                shows.add(getShowFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shows;
    }

    private ShowDTO getShowFromResultSet(ResultSet rs) throws SQLException {
        ShowDTO show = new ShowDTO();
        show.setId(rs.getInt("show_id"));
        show.setTitle(rs.getString("title"));
        show.setGenre(rs.getString("genre"));
        show.setDate(rs.getTimestamp("date"));
        show.setCast(rs.getString("cast"));
        show.setNrTickets(rs.getInt("nr_tickets"));
        return show;
    }

}
