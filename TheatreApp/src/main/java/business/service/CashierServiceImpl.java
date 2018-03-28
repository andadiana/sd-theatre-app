package business.service;

import business.model.User;
import business.util.PasswordEncrypter;
import business.util.PasswordEncrypterMD5;
import dataaccess.dbmodel.UserDTO;
import dataaccess.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class CashierServiceImpl implements CashierService {

    private UserRepository repository;

    public CashierServiceImpl(UserRepository userRepository) {

        this.repository = userRepository;
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
        //check if username already exists
        UserDTO userDTO = repository.getByUsername(user.getUsername());
        if (userDTO != null) {
            return 0;
        }
        String encryptedPass = encryptPassword(user.getPassword());
        user.setPassword(encryptedPass);
        userDTO = userToDTO(user);
        return repository.create(userDTO);
    }

    public boolean updateUsername(User user) {
        //check if username already exists
        UserDTO userDTO = repository.getByUsername(user.getUsername());
        if (userDTO != null) {
            return false;
        }
        userDTO = userToDTO(user);
        return repository.update(userDTO);
    }

    public boolean updatePassword(User user) {
        //check if username already exists
        UserDTO userDTO = repository.getById(user.getId());
        if (!userDTO.getUsername().equals(user.getUsername())) {
            //changed username and password too
            if (!updateUsername(user)) {
                return false;
            }
        }
        String encryptedPass = encryptPassword(user.getPassword());
        user.setPassword(encryptedPass);
        userDTO = userToDTO(user);
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
