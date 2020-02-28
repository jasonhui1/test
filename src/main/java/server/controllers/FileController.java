package server.controllers;

import server.Logger;
import server.models.Services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Alfie Jones
 *
 * This class fetches all the files requested by the webpage
 *
 */


@Path("/")
public class FileController {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public byte[] getIndexFile(@CookieParam("sessionToken") Cookie sessionCookie) {
        return getIHTMLFile(sessionCookie, "index");
    }

    @GET
    @Path("img/{path}")
    @Produces({"image/jpeg,image/png"})
    public byte[] getImageFile(@PathParam("path") String path) {
        return getFile("client/img/" + path);
    }

    @GET
    @Path("js/{path}")
    @Produces({"text/javascript"})
    public byte[] getJavaScriptFile(@PathParam("path") String path) {
        return getFile("client/js/" + path);
    }

    @GET
    @Path("css/{path}")
    @Produces({"text/css"})
    public byte[] getCSSFile(@PathParam("path") String path) {
        return getFile("client/css/" + path);
    }

    @GET
    @Path("{path}")
    @Produces({"text/html"})
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

        return file;

    }

    @GET
    @Path("favicon.ico")
    @Produces({"image/x-icon"})
    public byte[] getFavicon() {
        return getFile("client/favicon.ico");
    }

    /**
     * Gets the file
     * @param filename the file to be fetched
     * @return returns a byte[] holding our file, null if it's not found
     */
    private byte[] getFile(String filename) {
        try {
            File file = new File("src/main/resources/" + filename);

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