package business.service;

import business.model.User;

public interface UserService {

    public boolean logIn(String username, String password);
}
