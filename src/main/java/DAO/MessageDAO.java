package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.*;
import Util.ConnectionUtil;

public class MessageDAO {
    // #3
    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, message.posted_by);
            statement.setString(2, message.message_text);
            statement.setLong(3, message.time_posted_epoch);

            statement.executeUpdate();
            var rs = statement.getGeneratedKeys();
            if (rs.next()) {
                return new Message((int)rs.getLong(1), message.posted_by, message.message_text, message.time_posted_epoch);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // #4
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<Message>();

        try {
            String sql = "SELECT * FROM message";
            PreparedStatement statement = connection.prepareStatement(sql);
            var rs = statement.executeQuery();

            while (rs.next()) {
                Message m = new Message(
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"), 
                    rs.getString("message_text"), 
                    rs.getLong("time_posted_epoch")
                );
                messages.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    // #5
    public Message getMessageByID(int message_id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, message_id);

            var rs = statement.executeQuery();
            if (rs.next()) {
                return new Message(
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"), 
                    rs.getString("message_text"), 
                    rs.getLong("time_posted_epoch")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // #6
    public boolean deleteMessageByID(int message_id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, message_id);

            var rows = statement.executeUpdate();
            return rows != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // #7
    public boolean updateMessage(int message_id, String message_text) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "UPDATE message SET message_body = ? WHERE message_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, message_text);
            statement.setInt(2, message_id);

            var rows = statement.executeUpdate();
            return rows != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // #8
    public List<Message> getAllMessagesByUserID(int user_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<Message>();

        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, user_id);

            var rs = statement.executeQuery();
            while (rs.next()) {
                Message m = new Message(
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"), 
                    rs.getString("message_text"), 
                    rs.getLong("time_posted_epoch")
                );
                messages.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
}