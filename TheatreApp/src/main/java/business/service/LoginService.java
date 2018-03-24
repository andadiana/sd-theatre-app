package business.service;

import business.model.User.UserType;

public interface LoginService {

    public UserType logIn(String username, String password) throws Exception;
}
