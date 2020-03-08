package server.controllers;

import server.Logger;
import server.models.Services.UserService;
import server.models.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("user/")
public class UserController {

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String loginUser(@FormParam("email") String email,
                            @FormParam("password") String password) {
        User user = UserService.verifyLogin(email.toLowerCase(), password); //Check a user with these details exists
        if(user != null){   //User is null if bad details
            Logger.log("A user has logged in");
            UserService.users.add(user);    //add the newly logged in user to our array list
            return user.getSessionToken();  //return the users session token
        }else{
            Logger.log("A user has entered the wrong details in");
            return "BadDetails";
        }
    }

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String add(@FormParam("email") String email,
                            @FormParam("password") String password,
                      @FormParam("firstName") String firstName,
                      @FormParam("lastName") String lastName) {

        return null;
    }
}
