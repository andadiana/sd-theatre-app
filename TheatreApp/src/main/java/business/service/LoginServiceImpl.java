package business.service;

import business.model.User.UserType;
import business.util.PasswordEncrypter;
import business.util.PasswordEncrypterMD5;
import dataaccess.dbmodel.UserDTO;
import dataaccess.repository.UserRepository;
import dataaccess.repository.UserRepositoryMySql;

public class LoginServiceImpl implements LoginService {

    UserRepository repository;

    public LoginServiceImpl() {
        this.repository = new UserRepositoryMySql();
    }

    public UserType logIn(String username, String password) throws Exception{
        UserDTO user = repository.getByUsername(username);
        if (user == null) {
            throw new Exception("Username does not exist!");
        }
        if (checkPassword(password, user.getPassword())) {
            UserType type = null;
            switch (user.getUserType()) {
                case "ADMIN": {
                    type = UserType.Admin;
                    break;
                }
                case "CASHIER": {
                    type = UserType.Cashier;
                    break;
                }
                default: {
                    type = UserType.Cashier;
                }
            }
            return type;
        }
        throw new Exception("Incorrect password!");
    }

    private boolean checkPassword(String givenPass, String actualPass) {
        PasswordEncrypter encrypter = new PasswordEncrypterMD5();
        String encryptedPass = encrypter.encrypt(givenPass);
        if (encryptedPass.equals(actualPass)) {
            return true;
        }
        return false;
    }
}
