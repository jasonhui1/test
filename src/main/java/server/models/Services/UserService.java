package server.models.Services;

import server.DatabaseConnection;
import server.Logger;
import server.models.User;

import javax.ws.rs.core.Cookie;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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


    /**
     *
     * @author: Alfie Jones
     * Deletes the specified user from our database
     * @param userID
     * @throws SQLException
     */
    public static void DeleteUser(int userID) throws SQLException {
        //The database manager will handle foreign keys references
        PreparedStatement statement = DatabaseConnection.newStatement("DELETE FROM User WHERE id = ?");
        if (statement != null) {
            statement.setInt(1, userID);
            statement.executeQuery();
        }
    }


    /**
     *
     * @author: Alfie Jones
     * Updates all user details apart from id and password
     * @param user The user model we will update
     * @throws SQLException
     */
    public static void UpdateDetails(User user) throws SQLException{
        PreparedStatement statement = DatabaseConnection.newStatement("UPDATE User SET email = ?, first_name = ?, last_name = ? WHERE id = ?");
        //find and update user with specified id
        if(statement != null){
            //add data to statement
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        }
    }


    /**
     * @author Gets all the users from out database and returns them in an array
     * @return ArrayList<User>, an arraylist containing all the user models in our database
     * @throws SQLException
     */
    public static ArrayList<User> GetAllUsers() throws SQLException{
        //ArrayList to hold users
        ArrayList<User> userList = new ArrayList<>();
        //Select all users from the database
        PreparedStatement statement = DatabaseConnection.newStatement("Select email, first_name, last_name, id FROM User");
        if (statement != null) {
            ResultSet results = statement.executeQuery();
            if (results != null) {
                //Loop through all the users
                while (results.next()) {
                    //Create the user object and add it to the arrayList
                    User user = new User(results.getString("email"), results.getString("first_name"), results.getString("last_name"), results.getInt("id"));
                    userList.add(user);
                }
            }
        }
        //return the list of users
        return userList;
    }




}
