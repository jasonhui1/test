package server.controllers;

import org.eclipse.jetty.server.session.Session;
import server.Logger;
import server.models.User;
import server.models.services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
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


    /**
     * @author Alfred Jones
     * Handles the logout request
     */
    @POST
    @Path("logout")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void add(@CookieParam("sessionToken") Cookie sessionCookie) {

        User user = UserService.ValidateSessionToken(sessionCookie);

        if(user != null){
            UserService.users.remove(user);
        }

    }



    /**
     *
     * @param email             the new email of the user to be changed to
     * @param firstName         Change user's first name to this name
     * @param lastName          change user's last name to this name
     * @param newPassword       the new password of the user
     * @author Matthew Johnson
     */
    @POST
    @Path("amend")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String amend(@FormParam("email") String email,
                      @FormParam("firstName") String firstName,
                      @FormParam("lastName") String lastName,
                      @FormParam("newPassword") String newPassword,
                      @CookieParam("sessionToken") Cookie sessionCookie) {
        User user = UserService.ValidateSessionToken(sessionCookie);
        Logger.log("                                     ----                HERE HERE HERE HERE HERE HERE HERE HERE    " + email);

        try {

            //if user is null, no user signed in, otherwise, that's the user which is signed in
            if (user != null && UserService.validEmail(email)){
                user.updateUser(email, firstName,lastName);
                UserService.updateDetails(user);

                if (!newPassword.isEmpty()){
                    UserService.updatePassword(user, newPassword);
                }
                Logger.log("Updated details of user " + user.getId());
                return "Success";
            }else{
                return "Email in use";
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return "Error";
        }

    }


    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void delete(@CookieParam("sessionToken") Cookie sessionCookie){
        User user = UserService.ValidateSessionToken(sessionCookie);
        try {
            assert user != null;
            Logger.log("About to delete user " + user.getId());
            UserService.deleteUser(user);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
