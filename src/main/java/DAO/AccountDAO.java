package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import Model.*;
import Util.ConnectionUtil;

public class AccountDAO {
    // #1
    public Account insertAccount(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, username);
            statement.setString(2, password);

            statement.executeUpdate();
            var rs = statement.getGeneratedKeys();
            if (rs.next()) {
                var id = (int)rs.getLong(1);
                return new Account(id, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // #1
    public boolean usernameIsFree(String username) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT account_id FROM account WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);

            var rs = statement.executeQuery();
            var exists = !rs.next();    
            return exists;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // #2
    public Account getAccountByCredentials(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT account_id FROM account WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            
            var rs = statement.executeQuery();
            if (rs.next()) {
                return new Account((int) rs.getLong(1), username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
