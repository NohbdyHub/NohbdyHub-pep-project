package Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        String register = "/register";
        String login = "/login";
        String messages = "/messages";
        String messageID = messages + "/{message_id}";
        String accountID = "/accounts/{account_id}" + messages;

        app.post(register, this::registerHandler);         
        app.post(login, this::loginHandler);

        app.post(messages, this::addMessageHandler);
        app.get(messages, this::getMessagesHandler);
        
        app.get(messageID, this::getMessageByIDHandler);
        app.delete(messageID, this::deleteMessageByIDHandler);
        app.patch(messageID, this::patchMessageByIDHandler);
        app.get(accountID, this::getMessageByUserHandler);

        return app;
    }

    // POST /register
    private void registerHandler(Context ctx) {
        try {
            var om = new ObjectMapper();
            Account body = om.readValue(ctx.body(), Account.class);
            Account inserted = accountService.insertAccount(body.username, body.password);
            
            if (inserted != null) {
                ctx.json(om.writeValueAsString(inserted));
            } else {
                ctx.status(400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(400);
        }
    }

    // POST /login
    private void loginHandler(Context ctx) {
        try {
            var om = new ObjectMapper();
            Account body = om.readValue(ctx.body(), Account.class);
            Account loggedIn = accountService.getAccountByCredentials(body.username, body.password);
            
            if (loggedIn != null) {
                ctx.json(om.writeValueAsString(loggedIn));
            } else {
                ctx.status(401);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(401);
        }
    }

    // POST /messages
    private void addMessageHandler(Context ctx) {
        try {
            var om = new ObjectMapper();
            Message body = om.readValue(ctx.body(), Message.class);
            Message sent = messageService.insertMessage(body);
            
            if (sent != null) {
                ctx.json(om.writeValueAsString(sent));
            } else {
                ctx.status(400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(400);
        }
    }

    // GET /messages
    private void getMessagesHandler(Context ctx) {
        try {
            var om = new ObjectMapper();
            var messages = messageService.getAllMessages();
            ctx.json(om.writeValueAsString(messages));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GET /messages/{message_id}
    private void getMessageByIDHandler(Context ctx) {
        try {
            var om = new ObjectMapper();
            var message = messageService.getMessageByID(Integer.parseInt(ctx.pathParam("message_id")));
            if (message != null) {
                ctx.json(om.writeValueAsString(message));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE /messages/{message_id}
    private void deleteMessageByIDHandler(Context ctx) {
        try {
            var om = new ObjectMapper();
            var message = messageService.deleteMessageByID(Integer.parseInt(ctx.pathParam("message_id")));
            if (message != null) {
                ctx.json(om.writeValueAsString(message));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // PATCH /messages/{message_id}
    private void patchMessageByIDHandler(Context ctx) {
        try {
            var om = new ObjectMapper();
            Message body = om.readValue(ctx.body(),Message.class);
            Message updated = messageService.patchMessageByID(Integer.parseInt(ctx.pathParam("message_id")), body.message_text);

            if (updated != null) {
                ctx.json(om.writeValueAsString(updated));
            } else {
                ctx.status(400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(400);
        }
    }

    // GET /accounts/{account_id}/messages
    private void getMessageByUserHandler(Context ctx) {
        try {
            var om = new ObjectMapper();
            var messages = messageService.getAllMessagesByUserID(Integer.parseInt(ctx.pathParam("account_id")));
            ctx.json(om.writeValueAsString(messages));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}