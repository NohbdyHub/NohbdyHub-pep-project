package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO dao;

    public MessageService() {
        dao = new MessageDAO();
    }

    public MessageService(MessageDAO dao) {
        this.dao = dao;
    }

    public Message insertMessage(Message message) {
        if (message.message_text.isBlank() || message.message_text.length() > 255 || dao.userCanSendMessage(message.posted_by)) {
            return null;
        }
        return dao.insertMessage(message);
    }

    public List<Message> getAllMessages() {
        return dao.getAllMessages();
    }

    public Message getMessageByID(int message_id) {
        return dao.getMessageByID(message_id);
    }

    public Message deleteMessageByID(int message_id) {
        Message message = getMessageByID(message_id);
        if (message == null) {
            return null;
        }

        if(dao.deleteMessageByID(message_id)) {
            return message;
        } else {
            return null;
        }        
    }
    
    public Message patchMessageByID(int message_id, String message_text) {
        Message message = getMessageByID(message_id);
        if (message == null) {
            return null;
        }

        if(!message_text.isBlank() && message_text.length() <= 255 && dao.updateMessage(message_id, message_text)) {
            message.message_text = message_text;
            return message;
        } else {
            return null;
        }        
    }

    public List<Message> getAllMessagesByUserID(int user_id) {
        return dao.getAllMessagesByUserID(user_id);
    }
}
