package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO dao;

    public AccountService() {
        dao = new AccountDAO();
    }

    public AccountService(AccountDAO dao) {
        this.dao = dao;
    }

    public Account insertAccount(String username, String password) {
        if (username == null || password == null || username.isBlank() || password.length() < 4 || !dao.usernameIsFree(username)) {
            return null;
        }

        return dao.insertAccount(username, password);
    }

    public Account getAccountByCredentials(String username, String password) {
        return dao.getAccountByCredentials(username, password);
    }
}
