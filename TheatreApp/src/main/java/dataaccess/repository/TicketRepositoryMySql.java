package dataaccess.repository;

import dataaccess.DBConnection;
import dataaccess.dbmodel.TicketDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketRepositoryMySql implements TicketRepository{

    private final DBConnection dbConnection;

    public TicketRepositoryMySql(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<TicketDTO> findAll() {
        Connection connection = dbConnection.getConnection();
        List<TicketDTO> tickets = new ArrayList<TicketDTO>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM `ticket`";
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()) {
                tickets.add(getTicketFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public TicketDTO getById(int id) {
        Connection connection = dbConnection.getConnection();
        TicketDTO ticket = null;
        try {
            String query = "SELECT * FROM ticket WHERE ticket_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            System.out.println(statement);
            ResultSet rs = statement.executeQuery();
            if (rs.next())
                ticket = getTicketFromResultSet(rs);
            else {
                System.out.println("No results");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ticket;
    }

    public int create(TicketDTO ticket) {
        Connection connection = dbConnection.getConnection();
        try {
            String query = "INSERT INTO ticket (seat_id, show_id, reserved) " +
                    "VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, ticket.getSeatId());
            statement.setInt(2, ticket.getShowId());
            statement.setBoolean(3, ticket.getReserved());

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

    public boolean update(TicketDTO ticket) {
        Connection connection = dbConnection.getConnection();
        try {
            String query = "UPDATE ticket SET seat_id=?, show_id=?, reserved=? " +
                    "WHERE ticket_id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, ticket.getSeatId());
            statement.setInt(2, ticket.getShowId());
            statement.setBoolean(3, ticket.getReserved());
            statement.setInt(4, ticket.getId());
            System.out.println(statement);
            statement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean delete(TicketDTO ticket) {
        Connection connection = dbConnection.getConnection();
        try {
            String query = "DELETE FROM ticket WHERE ticket_id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, ticket.getId());
            System.out.println(statement);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private TicketDTO getTicketFromResultSet(ResultSet rs) throws SQLException {
        TicketDTO ticket = new TicketDTO();
        ticket.setId(rs.getInt("ticket_id"));
        ticket.setShowId(rs.getInt("show_id"));
        ticket.setSeatId(rs.getInt("seat_id"));
        ticket.setReserved(rs.getBoolean("reserved"));
        return ticket;
    }
}