package server.models;

import java.util.UUID;


/**
 * @author Alfred Jones
 *
 * The model class of a user, Contains all the relevant info on a user
 *
 */

public class User {


    private String firstName, lastName, email;
    private final int id;
    private final String sessionToken;


    //class constructor
    public User(String email, String firstName, String lastName, int id){
        this.email = email.toLowerCase();   //Emails aren't case sensitive
        this.firstName = firstName;
        this.lastName = lastName;
        this.sessionToken = UUID.randomUUID().toString();   //Generate a session token to identify the user
        this.id = id;
    }


    /**
     * method to update the user details in the class so that when update details method is run in UserService, the
     * correct details are updated on database
     * @param email - email to be updated to
     * @param firstName - new firstname to be updated to
     * @param lastName - new lastname to be updated to
     * @Author Matthew Johnson
     */
    public void updateUser(String email, String firstName, String lastName){
        /*Checks if what is entered is blank. If so, it shouldn't be changed. If it contains something, change detail*/
        if (!email.isEmpty()){
            this.email = email;
        }

        if (!firstName.isEmpty()){
            this.firstName = firstName;
        }

        if (!lastName.isEmpty()){
            this.lastName = lastName;
        }
    }

    //return users first name
    public String getFirstName() {
        return firstName;
    }

    //return users last name
    public String getLastName() {
        return lastName;
    }

    //return users session token
    public String getSessionToken(){return sessionToken;}

    //return users email
    public String getEmail() {
        return email;
    }

    //return user id
    public int getId() {
        return id;
    }

}
