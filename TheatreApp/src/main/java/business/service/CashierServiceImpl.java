package business.service;

import business.model.User;
import business.util.PasswordEncrypter;
import business.util.PasswordEncrypterMD5;
import dataaccess.dbmodel.UserDTO;
import dataaccess.repository.UserRepository;
import dataaccess.repository.UserRepositoryMySql;

import java.util.ArrayList;
import java.util.List;

public class CashierServiceImpl implements CashierService {

    private UserRepository repository;

    public CashierServiceImpl() {

        this.repository = new UserRepositoryMySql();
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
        String encryptedPass = encryptPassword(user.getPassword());
        user.setPassword(encryptedPass);
        UserDTO userDTO = userToDTO(user);
        return repository.create(userDTO);
    }

    public boolean updateCashier(User user) {
        String encryptedPass = encryptPassword(user.getPassword());
        user.setPassword(encryptedPass);
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
            case Admin: {
                userDTO.setUserType("ADMIN");
                break;
            }
            case Cashier: {
                userDTO.setUserType("CASHIER");
                break;
            }
        }
        return userDTO;
    }

    private String encryptPassword(String givenPass) {
        PasswordEncrypter encrypter = new PasswordEncrypterMD5();
        return encrypter.encrypt(givenPass);
    }
}
