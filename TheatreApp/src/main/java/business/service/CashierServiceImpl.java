package business.service;

import business.model.User;
import dataaccess.dbmodel.UserDTO;
import dataaccess.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class CashierServiceImpl implements CashierService {

    private UserRepository repository;

    public CashierServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> findallCashiers() {
        List<UserDTO> userDTOS = repository.getByUserType("CASHIER");
        List<User> users = new ArrayList<>();
        for (UserDTO user: userDTOS) {
            users.add(dtoToUser(user));
        }
        return users;
    }

    public User getById(int id) {
        UserDTO userDTO = repository.getById(id);
        return dtoToUser(userDTO);
    }

    public int createCashier(User user) {
        UserDTO userDTO = userToDTO(user);
        return repository.create(userDTO);
    }

    public boolean updateCashier(User user) {
        UserDTO userDTO = userToDTO(user);
        return repository.update(userDTO);
    }

    public boolean deleteCashier(User user) {
        UserDTO userDTO = userToDTO(user);
        return repository.delete(userDTO);
    }

    private User dtoToUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        switch (userDTO.getUserType()) {
            case "ADMIN": user.setUserType(User.UserType.Admin);
            break;
            case "CASHIER": user.setUserType(User.UserType.Cashier);
            break;
        }
        return user;
    }

    private UserDTO userToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        switch (user.getUserType()) {
            case Admin: userDTO.setUserType("ADMIN");
            break;
            case Cashier: userDTO.setUserType("CASHIER");
            break;
        }
        return userDTO;
    }
}
