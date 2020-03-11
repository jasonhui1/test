package server.controllers;


import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

@Path("Transaction/")
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

        return "success";
    }



}
