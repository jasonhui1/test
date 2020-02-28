package server.models;

import java.util.UUID;


/**
 * @author: Alfie Jones
 *
 * The model class of a user, Contains all the relevant info on a user
 *
 */

public class User {


    private String firstName, lastName, email;
    private int id;
    private String sessionToken;


    //class constructor
    public User(String email, String firstName, String lastName, int id){
        this.email = email.toLowerCase();   //Emails aren't case sensitive
        this.firstName = firstName;
        this.lastName = lastName;
        this.sessionToken = UUID.randomUUID().toString();   //Generate a session token to identify the user
        this.id = id;
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
