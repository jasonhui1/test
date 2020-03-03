package server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;


/**
 * @author Alfie Jones
 *
 * This class starts up our server
 */
public class ServerStart {


    public static void main(String[] args) {


        //Open the database
        DatabaseConnection.open("TransactionDatabase.db");

        ResourceConfig config = new ResourceConfig();
        config.packages("server");
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        Server server = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");

        try {
            server.start();
            Logger.log("Server successfully started.");
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.destroy();
            DatabaseConnection.close();
            Logger.log("Server closed.");
        }
    }
}

