package server;

import java.sql.*;


/**
 * @author Alfred Jones
 *
 * This class manages the onnection with our database
 */
public class DatabaseConnection {

    private static Connection connection = null;

    /**
     * @author Alfred Jones
     * Opens the database specified, this only needs to be run if the database hasn't already been opened
     * @param dbFile the location of our database
     */
    public static void open(String dbFile)
    {
        try
        {
            //dynamically load JDBC drivers
            Class.forName("org.sqlite.JDBC");
            //try and connect to the database stored in resources
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/" + dbFile);
            Logger.log("Database connection successfully established.");
        }
        catch (ClassNotFoundException exception)
        {
            Logger.log("Class not found exception: " + exception.getMessage());
        }
        catch (SQLException exception)
        {
            Logger.log("Database connection error: " + exception.getMessage());
        }
    }

    /**
     * @author Alfred Jones
     * Takes an sql statment and performs it on the database if it's been opened
     * @param query the sql statement
     * @return returns a prepared statement
     * @throws SQLException throws and error if something went wrong
     */
    public static PreparedStatement newStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    public static PreparedStatement newStatementAndgetID(String query) throws SQLException {
        return connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    }


    /**
     *
     * @author Alfred Jones
     * Closes the open database
     */
    public static void close()
    {
        Logger.log("Disconnecting from database.");
        try {
            //if the connection is on, close it.
            if (connection != null) connection.close();
        }
        catch (SQLException exception)
        {
            Logger.log("Database disconnection error: " + exception.getMessage());
        }
    }
}