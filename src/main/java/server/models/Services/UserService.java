package server.models.Services;

import server.Logger;
import server.models.User;

import javax.ws.rs.core.Cookie;
import java.util.ArrayList;


/**
 * @author Alfie Jones
 *
 * This class holds all the methods relating to our user model
 *
 */


public class UserService {

    //Holds all active users
    public static ArrayList<User> users = new ArrayList<>();

    /**
     * @author: Alfie Jones
     * @param sessionCookie the cookie to check
     * @return returns the user model related to the cookie
     *
     * checks the given cookie to see if its related to an active user and if so return that user
     *
     */
    public static User ValidateSessionToken(Cookie sessionCookie){
        if(sessionCookie != null) {
            //Loop through all active users
            for (User user : users) {
                //Compare tokens
                if (user.getSessionToken().equals(sessionCookie.getValue())) {
                    //If session tokens match
                    Logger.log("Valid user token from " + user.getEmail() + " received");
                    //Return user responsible for that token
                    return user;
                }
            }
        }
        //Invalid token
        Logger.log("Invalid user token received");
        return null;
    }

}
