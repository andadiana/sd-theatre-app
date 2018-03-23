package dataaccess.repository;

import dataaccess.dbmodel.UserDTO;
import java.util.List;

public interface UserRepository {

    public List<UserDTO> findAll();
    public UserDTO getById(int id);
    public int create(UserDTO user);
    public boolean update(UserDTO user);
    public boolean delete(UserDTO user);
    public UserDTO getByUsername(String username);
    public List<UserDTO> getByUserType(String userType);
}
