package server.controllers;

import server.Logger;
import server.models.services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Alfred Jones
 *
 * This class fetches all the files requested by the webpage
 *
 */


@Path("/")
public class FileController {

    /**
     * @param path the file path to the image
     * @return the image file
     * @author Alfred Jones
     * returns any images requested
     */
    @GET
    @Path("img/{path}")
    @Produces({"image/jpeg,image/png"})
    public byte[] getImageFile(@PathParam("path") String path) {
        return getFile("client/img/" + path);
    }

    /**
     * @param path the file path to the file
     * @return the file
     * @author Alfred Jones
     * returns any js files requested
     */
    @GET
    @Path("js/{path}")
    @Produces({"text/javascript"})
    public byte[] getJavaScriptFile(@PathParam("path") String path) {
        byte[] file = getFile("client/js/other/" + path);
        if(file == null){
            file = getFile("client/js/custom/" + path);
        }
        return file;
    }

    /**
     * @param path the file path to the file
     * @return the file
     * @author Alfred Jones
     * returns any css files requested
     */
    @GET
    @Path("css/{path}")
    @Produces({"text/css"})
    public byte[] getCSSFile(@PathParam("path") String path) {

        byte[] file = getFile("client/css/other/" + path);
        if(file == null){
            file = getFile("client/css/custom/" + path);
        }

        return file;
    }

    /**
     * @param path the file path to the file
     * @return the file
     * @author Alfred Jones
     * returns any html files requested
     */
    @GET
    @Path("{path}")
    @Produces(MediaType.TEXT_HTML)
    public byte[] getIHTMLFile(@CookieParam("sessionToken") Cookie sessionCookie, @PathParam("path") String path) {

        byte[] file  = getFile("client/html/public/" + path + ".html");
        if(file == null) {
            if(UserService.ValidateSessionToken(sessionCookie) != null) {
                //return the private file
                file = getFile("client/html/private/" + path + ".html");
            } else{
                //return a bad credentials error page if user doesn't have a valid session token
                file = getFile("client/html/public/badCredentials.html");
            } }

        if(file == null){
            file = getFile("client/html/public/404.html");
        }

        return file;

    }

    /**
     * @param sessionCookie the cookie sessionToken, used to identify the user
     * @return the index file
     * @author Alfred Jones
     * returns the index file
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public byte[] getIndexFile(@CookieParam("sessionToken") Cookie sessionCookie) {
        return getIHTMLFile(sessionCookie, "index");
    }

    /**
     * @return the favicon
     * @author Alfred Jones
     * gets the sites favicon
     */
    @GET
    @Path("favicon.ico")
    @Produces({"image/x-icon"})
    public byte[] getFavicon() {
        return getFile("client/img/favicon.ico");
    }

    /**
     * Gets the file
     * @author Alfred Jones
     * @param filename the file to be fetched
     * @return returns a byte[] holding our file, null if it's not found
     */
    private byte[] getFile(String filename) {
        try {
            File file = new File("src/main/resources/" + filename);

            //attempt to read the file
            byte[] fileData = new byte[(int) file.length()];
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            dis.readFully(fileData);
            dis.close();
            Logger.log("Sending: " + filename);
            return fileData;
        } catch (IOException ioe) {
            Logger.log("File IO error: " + ioe.getMessage());
        }
        return null;
    }

}