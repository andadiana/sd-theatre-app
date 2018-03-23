package dataaccess.dbmodel;

public class UserDTO {

    private int id;
    private String username;
    private String password;
    private String userType;


    public UserDTO() {
        this.id = 0;
        this.username = null;
        this.password = null;
        this.userType = null;
    }

    public UserDTO(int id, String username, String password, String userType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    public String toString() {
        return id + " " + username + " " + userType;
    }
}
