package server.controllers;

import server.Logger;
import server.models.User;
import server.models.services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;


/**
 * @author Alfie
 *
 * Deals with requests relating to
 */

@Path("user/")
public class UserController {

    /**
     * @param email    the email of the person attempting to login
     * @param password the password of the person attempting to login
     * @return returns information as to whether the login was successful or not
     * @author Alfred Jones
     * Handles the login requests
     */
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

    /**
     * @param email     the email of the person attempting to sign up
     * @param password  the password of the person attempting to sign up
     * @param firstName the first name of the user signing up
     * @param lastName  the last name of the user signing up
     * @return returns information as to whether the login was successful or not
     * @author Alfred Jones
     * Handles the signup requests
     */
    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String add(@FormParam("email") String email,
                      @FormParam("password") String password,
                      @FormParam("firstName") String firstName,
                      @FormParam("lastName") String lastName) {

        //TODO ensure details aren't null, lastName can be null however

        try {

            if(!UserService.validEmail(email)){
                return "Error: The email entered already has an account";
            }
            UserService.addUser(new User(email, firstName, lastName, -1), password);

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }

        Logger.log("Added a new user to our database");

        return "success";
    }
}
