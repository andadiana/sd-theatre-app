package dataaccess.repository;

import dataaccess.DBConnection;
import dataaccess.dbmodel.UserDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryMySql implements UserRepository {

    public List<UserDTO> findAll() {
        Connection connection = DBConnection.getConnection();
        List<UserDTO> users = new ArrayList<UserDTO>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM `user`";
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()) {
                users.add(getUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public UserDTO getById(int id) {
        Connection connection = DBConnection.getConnection();
        UserDTO user = null;
        try {
            String query = "SELECT * FROM user WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            System.out.println(statement);
            ResultSet rs = statement.executeQuery();
            if (rs.next())
                user = getUserFromResultSet(rs);
            else {
                System.out.println("No results");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public int create(UserDTO user) {
        Connection connection = DBConnection.getConnection();
        try {
            String query = "INSERT INTO user (username, password, user_type) " +
                    "VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getUserType());

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

    public boolean update(UserDTO user) {
        Connection connection = DBConnection.getConnection();
        try {
            String query = "UPDATE user SET username=?, password=?, user_type=? " +
                    "WHERE user_id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getUserType());
            statement.setInt(4, user.getId());
            System.out.println(statement);
            statement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean delete(UserDTO user) {
        Connection connection = DBConnection.getConnection();
        try {
            String query = "DELETE FROM user WHERE user_id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, user.getId());
            System.out.println(statement);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public UserDTO getByUsername(String username) {
        Connection connection = DBConnection.getConnection();
        UserDTO user = null;
        try {
            String query = "SELECT * FROM user WHERE username= ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            System.out.println(statement);
            ResultSet rs = statement.executeQuery();
            if (rs.next())
                user = getUserFromResultSet(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<UserDTO> getByUserType(String userType) {
        Connection connection = DBConnection.getConnection();
        List<UserDTO> users = new ArrayList<>();
        try {
            String query = "SELECT * FROM user WHERE user_type= ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userType);
            System.out.println(statement);
            ResultSet rs = statement.executeQuery();

            while(rs.next()) {
                users.add(getUserFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private UserDTO getUserFromResultSet(ResultSet rs) throws SQLException {
        UserDTO user = new UserDTO();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setUserType(rs.getString("user_type"));
        return user;
    }

}
