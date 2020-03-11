package server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author Alfred
 *
 * Hashes the passwords and generates a salt for them
 */
public class PasswordHash {

    /**
     * Hashes a password using the password and its salt
     *
     * @param password the password to be hashed
     * @param salt     the salt used in the hash
     * @return returns the hashed password
     * @author Alfred Jones
     */
    public static String hash(String password, byte[] salt){
        String generatedPassword;
        try {
            //use sha-512 to generate hash
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            //add salt
            md.update(salt);
            //break password into bytes and encode it
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return generatedPassword;
    }

    /**
     * @return returns the salt
     * @throws NoSuchAlgorithmException throws an error if our algorithm doesn't exist
     * @author Alfred Jones
     * Gets a random 128 byte salt for our password hash
     */
    public static byte[] getSalt() throws NoSuchAlgorithmException {
        //Always use a SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        //Create array for salt
        byte[] salt = new byte[128];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        return salt;
    }


}
