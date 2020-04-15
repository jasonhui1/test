package server.controllers;


import server.Logger;
import server.models.Budget;
import server.models.User;
import server.models.services.BudgetService;
import server.models.services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

@Path("budget/")
public class BudgetController {

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String AddBudeget(@CookieParam("sessionToken") Cookie sessionCookie,
                                 @FormParam("amount") float amount){


        User user = UserService.ValidateSessionToken(sessionCookie);
        Logger.log("adding budget");
        if(user != null) {
            BudgetService.addBudget(new Budget(user.getId(), (int) (amount * 100)));
        }

        return  "success";
    }
}
