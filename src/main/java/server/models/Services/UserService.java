package server.models.Services;

import server.DatabaseConnection;
import server.Logger;
import server.PasswordHash;
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
     * Verify user login details are correct
     * @param enteredEmail : The email entered
     * @param enteredPassword : Password associated with the email
     * @return : Returns the user model if login was successful, otherwise a null object is returned
     */
    public static User verifyLogin(String enteredEmail, String enteredPassword) {
        try {
            //select any user with a matching email address
            PreparedStatement statement = DatabaseConnection.newStatement("SELECT password_hash, password_salt, first_name, last_name, id FROM User WHERE email = ?");
            if (statement != null) {
                //set the question mark in the above statement to the email address entered
                statement.setString(1, enteredEmail);
                //Execute the above statement
                ResultSet results = statement.executeQuery();
                if (results != null && results.next()) {
                    //check user has matching passwords
                    if (validatePassword(enteredPassword,
                            results.getString("password_hash"),
                            results.getBytes("password_salt"))) {
                        //return the user object
                        return new User(enteredEmail,
                                results.getString("first_name"),
                                results.getString("last_name"),
                                results.getInt("id"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //return null if user didn't have correct info
        return null;
    }

    //validates if two passwords, one hashed and one not, to see if they're equal
    //returns true if equal, false if not

    /**
     * validates if two passwords, one hashed and one not, to see if they're equal
     * @param passwordEntered : the users password to check
     * @param password_hash : the hashed password
     * @param password_salt : the passwords salt
     * @return returns true if equal, false if not
     */
    private static boolean validatePassword(String passwordEntered, String password_hash, byte[] password_salt){

        if(password_hash.equals(PasswordHash.hash(passwordEntered, password_salt))){
            Logger.log("A user has entered a correct password!");
            return true;
        }
        Logger.log("A user has entered an incorrect password!");
        return false;
    }


    /**
     * writes the passed in user to our user table in database
     * @param user : The user model to be added to the database
     * @param password : The password for the user
     */
    public static void addUser(User user, String password) throws SQLException {

        try {
            PreparedStatement statement = DatabaseConnection.newStatement("INSERT INTO User (email, password_hash, password_salt, first_name, last_name) VALUES (?, ?, ?, ?, ?)");
            if(statement != null){
                //Get our password hash
                byte[] salt = PasswordHash.getSalt();
                //Add data to query
                statement.setString(1, user.getEmail());
                statement.setString(2, PasswordHash.hash(password, salt)); //Hash the password
                statement.setBytes(3, salt);
                statement.setString(4, user.getFirstName());
                statement.setString(5, user.getLastName());
                statement.executeUpdate();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @author: Alfie Jones
     * Deletes the specified user from our database
     * @param userID
     * @throws SQLException
     */
    public static void deleteUser(int userID) throws SQLException {
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
     * Checks to see if an email exists
     * @param email
     * @throws SQLException
     */
    public static boolean validEmail(String email) throws SQLException {
        //TODO add more validation to make sure the email is of a correct form

        //Check to see if the email already exists
        PreparedStatement statement = DatabaseConnection.newStatement("Select 1 FROM User WHERE email = ?");
        if (statement != null) {
            statement.setString(1, email);
            ResultSet results = statement.executeQuery();

            if(results.next()){
                return false;
            } else{
                return true;
            }
        }

    return false;

    }




    /**
     *
     * @author: Alfie Jones
     * Updates all user details apart from id and password
     * @param user The user model we will update
     * @throws SQLException
     */
    public static void updateDetails(User user) throws SQLException{
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
