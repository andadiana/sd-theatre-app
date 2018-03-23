package business.service;

import business.model.User;

import java.util.List;

public interface CashierService {

    public int createCashier(User user);
    public List<User> findallCashiers();
    public User getById(int id);
    public boolean updateCashier(User user);
    public boolean deleteCashier(User user);

}
