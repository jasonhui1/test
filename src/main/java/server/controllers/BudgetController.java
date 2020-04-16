package server.controllers;


import org.json.simple.JSONArray;
import server.Logger;
import server.models.Budget;
import server.models.User;
import server.models.services.BudgetService;
import server.models.services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("budget/")
public class BudgetController {

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String AddBudget(@CookieParam("sessionToken") Cookie sessionCookie,
                             @FormParam("amount") float amount){

        User user = UserService.ValidateSessionToken(sessionCookie);
        Logger.log("adding budget");
        if(user != null) {
            BudgetService.addBudget(new Budget(user.getId(), (int) (amount * 100)));
            return Integer.toString((int)(amount*100));
        } else{
            Logger.log("A user has entered the wrong details in");
            return "BadDetails";
        }


    }

    @POST
    @Path("change")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String ChangeBudget(@CookieParam("sessionToken") Cookie sessionCookie,
                            @FormParam("amount") float amount){

        User user = UserService.ValidateSessionToken(sessionCookie);
        Logger.log("changing budget");
        if(user != null) {
            BudgetService.changeBudget(new Budget(user.getId(), (int) (amount * 100)));
            return Integer.toString((int)(amount*100));
        } else{
            Logger.log("A user has entered the wrong details in");
            return "BadDetails";
        }


    }

    @POST
    @Path("get")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public int getBudget(@CookieParam("sessionToken") Cookie sessionCookie){
        User user = UserService.ValidateSessionToken(sessionCookie);
        int budget = 0;
        if(user != null) {
            budget = BudgetService.getBudget(user.getId());
        }
        return budget;
    }

}
