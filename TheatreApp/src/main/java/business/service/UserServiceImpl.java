package business.service;

import business.util.PasswordEncrypter;
import business.util.PasswordEncrypterMD5;
import dataaccess.dbmodel.UserDTO;
import dataaccess.repository.UserRepository;

public class UserServiceImpl implements UserService{

    UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    public boolean logIn(String username, String password) {
        UserDTO user = repository.getByUsername(username);
        PasswordEncrypter encrypter = new PasswordEncrypterMD5();
        String encryptedPass = encrypter.encrypt(password);
        if (encryptedPass.equals(user.getPassword())) {
            return true;
        }
        return false;
    }
}
