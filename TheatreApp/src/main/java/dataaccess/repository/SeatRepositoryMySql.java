package dataaccess.repository;

import dataaccess.DBConnection;
import dataaccess.dbmodel.SeatDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatRepositoryMySql implements SeatRepository {


    private DBConnection dbConnection;

    public SeatRepositoryMySql(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<SeatDTO> findAll() {
        Connection connection = dbConnection.getConnection();
        List<SeatDTO> seats = new ArrayList<SeatDTO>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM seat";
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()) {
                seats.add(getSeatFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    public SeatDTO getById(int id) {
        Connection connection = dbConnection.getConnection();
        SeatDTO seat = null;
        try {
            String query = "SELECT * FROM seat WHERE seat_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            System.out.println(statement);
            ResultSet rs = statement.executeQuery();
            if (rs.next())
                seat = getSeatFromResultSet(rs);
            else {
                System.out.println("No results");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seat;
    }

    public int create(SeatDTO seat) {
        Connection connection = dbConnection.getConnection();
        try {
            String query = "INSERT INTO seat (row_nr, seat_nr) " +
                    "VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, seat.getRowNr());
            statement.setInt(2, seat.getSeatNr());

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

    public boolean update(SeatDTO seat) {
        Connection connection = dbConnection.getConnection();
        try {
            String query = "UPDATE seat SET row_nr=?, seat_nr=? " +
                    "WHERE seat_id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, seat.getRowNr());
            statement.setInt(2, seat.getSeatNr());
            statement.setInt(3, seat.getId());
            System.out.println(statement);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean delete(SeatDTO seat) {
        Connection connection = dbConnection.getConnection();
        try {
            String query = "DELETE FROM seat WHERE seat_id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, seat.getId());
            System.out.println(statement);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private SeatDTO getSeatFromResultSet(ResultSet rs) throws SQLException {
        SeatDTO seat = new SeatDTO();
        seat.setId(rs.getInt("seat_id"));
        seat.setRowNr(rs.getInt("row_nr"));
        seat.setSeatNr(rs.getInt("seat_nr"));
        return seat;
    }
}
