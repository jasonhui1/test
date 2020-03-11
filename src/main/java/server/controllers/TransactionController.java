package server.controllers;


import server.Logger;
import server.models.User;
import server.models.services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

@Path("transaction/")
public class TransactionController {

    /**
     * @author Alfred Jones
     *
     * This is a template for how we can send our data to add a transaction to the database
     */

    @POST
    @Path("submit/add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String AddTransaction(@CookieParam("sessionToken") Cookie sessionCookie,
                                 @FormParam("type") String type,
                                 @FormParam("amount") String amount,
                                 @FormParam("date") String date){
        User user = UserService.ValidateSessionToken(sessionCookie);
        if (user != null) {
            //Add the transaction
            Logger.log("A user has added a transaction");
        } else {
            //The user isn't logged in and shouldn't be able to make database changes
            Logger.log("An unauthorised attempted at adding a transaction was occurred");
            return "error";
        }

        return "success";
    }



}
