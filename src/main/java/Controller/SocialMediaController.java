package Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.AccountDAO;
import Model.Account;
import Service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private AccountService accountService;

    public SocialMediaController() {
        accountService = new AccountService();
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

        /*
        app.post(messages, null);
        app.get(messages, null);

        app.get(messageID, null);
        app.delete(messageID, null);
        app.patch(messageID, null);

        app.get(accountID, null);
        */


        return app;
    }

    // /register
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

    // /login
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
}