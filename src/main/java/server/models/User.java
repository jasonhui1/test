package server.models;

import java.util.UUID;


/**
 * @author Alfred Jones
 *
 * The model class of a user, Contains all the relevant info on a user
 *
 */

public class User {


    private final String firstName, lastName, email;
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




    @Override
    public boolean equals(Object that){
        if(this == that) return true;//if both of them points the same address in memory

        if(!(that instanceof User)) return false; // if "that" is not a People or a childclass

        User user = (User)that; // than we can cast it to People safely

        return this.getSessionToken().equals(user.getSessionToken());
    }





}
