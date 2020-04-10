package server.models.services;

import server.DatabaseConnection;
import server.Logger;
import server.models.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionService {

    private final static List<Object> transactionTypes = getTypes("Spending_Type");
    private final static List<Object> incomeTypes = getTypes("Income_Type");


    /**
     * @author Ceri
     * form parameters
     */
    public static void addIncome(User user, String name, String date, int amount, String type, String description){

        try {
            PreparedStatement statement = DatabaseConnection.newStatement("INSERT INTO Income (name, date, amount, type_id, description, user_id) VALUES (?, ?, ?, ?, ?, ?)");
            Logger.log("trying to add to database");

            if(statement != null){
                //Add data to query
                statement.setString(1, name);
                statement.setString(2, date);
                statement.setInt(3, amount);
                statement.setInt(4, getIncomeId(type));
                statement.setString(5, description);
                statement.setInt(6, user.getId());
                statement.executeUpdate();
                Logger.log("transaction added to database");
            }
        } catch (Exception e){
            Logger.log("Failed");
            e.printStackTrace();
        }

    }



    /**
     * @author Jason
     * Add transaction the database
     * Form parameters
     *
     */
    public static void addTransaction(User user, int amount, String name, String description, String type, String date, int recurring){

        addTransactionToDatabase(user.getId(), amount, name, description, getTransactionId(type), date, recurring);

    }

    public static void addTransaction(int user_ID, int amount, String name, String description, int type_ID, String date, int recurring){

        addTransactionToDatabase(user_ID, amount, name, description, type_ID, date, recurring);
    }

    public static void addTransactionToDatabase(int user_ID, int amount, String name, String description, int type_ID, String date, int recurring){
        try {
            PreparedStatement statement;
            if(!description.equals("")) {
                statement = DatabaseConnection.newStatement("INSERT INTO Spending (date, user_id, spending_id, name, amount, recurring, description) VALUES (?, ?, ?, ?, ?, ?, ?)");
                statement.setString(7, description);

            } else {
                statement = DatabaseConnection.newStatement("INSERT INTO Spending (date, user_id, spending_id, name, amount, recurring) VALUES (?, ?, ?, ?, ?, ?)");
            }

            if(statement != null){
                //Add data to query
                statement = setTransactionQuery(statement, user_ID, amount, name, type_ID, date, recurring);
                if(statement != null) {
                    statement.executeUpdate();
                }
                Logger.log("transaction added to database");

            }
        } catch (Exception e){
            Logger.log("Failed to add the transaction to database");
            e.printStackTrace();
        }
    }

    public static PreparedStatement setTransactionQuery(PreparedStatement statement, int user_ID, int amount, String name, int type_ID, String date, int recurring){
        try{
            statement.setString(1, date);
            statement.setInt(2, user_ID);
            statement.setInt(3, type_ID); //spending_id
            statement.setString(4, name); //name
            statement.setInt(5, amount); //amount
            statement.setInt(6, recurring);
            return statement;

        } catch (SQLException e){
            e.printStackTrace();

        }

        return null;
    }

    public static void addRecurringTransaction(User user, int amount, String name, String description, String type, String date, int interval, String endDate) {
        try {
            PreparedStatement statement1;
            if(!description.equals("")) {
                statement1 = DatabaseConnection.newStatementAndgetID("INSERT INTO Spending (date, user_id, spending_id, name, amount, recurring, description) VALUES (?, ?, ?, ?, ?, ?, ?)");
                statement1.setString(7, description);

            } else {
                statement1 = DatabaseConnection.newStatementAndgetID("INSERT INTO Spending (date, user_id, spending_id, name, amount, recurring) VALUES (?, ?, ?, ?, ?, ?)");

            }

            int spending_ID = 0;
            if(statement1 != null){
                //Add data to the spending database query
                statement1 = setTransactionQuery(statement1, user.getId(), amount, name, getTransactionId(type), date, 1);
                //Get the id just added to the spending table
                if(statement1 != null) {
                    statement1.execute();

                    ResultSet rs = statement1.getGeneratedKeys();
                    if (rs.next()) {
                        spending_ID = rs.getInt(1);
                    }
                }
                Logger.log("transaction added to database");
            }

            if(spending_ID != 0) {
                PreparedStatement statement2 = DatabaseConnection.newStatement("INSERT INTO Recurring_Spending (spending_id, start_date, end_date, interval, last_updated_date) VALUES (?, ?, ?, ?, ?)");

                if (statement2 != null) {
                    statement2.setInt(1, spending_ID);
                    statement2.setString(2, date);
                    statement2.setString(3, endDate);
                    statement2.setInt(4, interval);
                    statement2.setString(5, date);
                    statement2.executeUpdate();
                    Logger.log("recurring transaction added to database");
                }
            }

        } catch (Exception e){
            Logger.log("Failed to add the transaction to database");
            e.printStackTrace();
        }
    }

    /**
     * @author Alfred
     * @param tableName the name of the table
     * @return a list of items from the table
     */
    private static List<Object> getTypes(String tableName){
        ArrayList<Object> mTypes = new ArrayList<>();
        try {
            PreparedStatement statement = DatabaseConnection.newStatement("SELECT id, name, description FROM " + tableName);
            if (statement != null) {
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    //Loop through all types
                    while (results.next()) {
                        //Add types to list
                        if(tableName.equals("Spending_Type")) {
                            mTypes.add(new TransactionType(results.getInt("id"), results.getString("name"), results.getString("description")));
                        }else if(tableName.equals("Income_Type")){
                            mTypes.add(new IncomeType(results.getInt("id"), results.getString("name"), results.getString("description")));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            //An error occurred
            Logger.log("Database error - can't select all from 'Transaction_Type' table");
            e.printStackTrace();
        }
        return mTypes;
    }

    /**
     * @author Alfred
     * @param id the id of the type
     * @return the type
     */
    public static TransactionType getTransactionType(int id){
        for (Object obj: transactionTypes) {
            TransactionType type = (TransactionType) obj;
            if(type.getId() == id){
                return type;
            }
        }
        return null;
    }

    /**
     * @author Jason
     * get the id of the type
     * @param name of the type
     * @return the type
     */

    public static int getTransactionId(String name){

        for (Object obj: transactionTypes) {
            TransactionType type = (TransactionType) obj;
            if(type.getName().equals(name)){

                return type.getId();
            }
        }
        return 0;
    }

    /**
     * @author Alfred
     * @param id the id of the type
     * @return the type
     */
    public static IncomeType getIncomeType(int id){
        for (Object obj: incomeTypes) {
            IncomeType type = (IncomeType) obj;
            if(type.getId() == id){
                return type;
            }
        }
        return null;
    }

    public static int getIncomeId(String name){
        Logger.log("Selected " + name);
        for (Object obj: incomeTypes) {
            IncomeType type = (IncomeType) obj;
            Logger.log("All names: " + type.getName());
            Logger.log(name + " " + type.getName() + name.equals(type.getName()));
            if(type.getName().equals(name)){

                return type.getId();
            }
        }
        return 0;
    }

    /**
     * @author Alfred
     * @param transactionList the list to populate
     * @param userID the id of our user
     * @return if the get was successful
     */
    public static String getRelevantTransactions(List<Transaction> transactionList, int userID){
        transactionList.clear();
        try {
            PreparedStatement statement = DatabaseConnection.newStatement("SELECT date, id, spending_id, name, amount, description FROM Spending WHERE user_id = ? ORDER BY date DESC");
            if (statement != null) {
                //We only want transactions from our user
                statement.setInt(1, userID);
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    //Loop through all transactions
                    while (results.next()) {
                        //Add transaction to list
                        transactionList.add(new Transaction(results.getInt("id"), results.getInt("amount"), results.getInt("date"), results.getString("name"), results.getString("description"), results.getInt("spending_id")));
                    }
                }
            }
        } catch (SQLException e) {
            //An error occurred
            String error = "Database error - can't select all from 'Spending_Type' table: " + e.getMessage();
            e.printStackTrace();
            return error;
        }
        return "OK";
    }

    /**
     * @author Alfred
     * @param incomeList the list to populate
     * @param userID the id of our user
     * @return if the get was successful
     */
    public static String getRelevantIncome(List<Income> incomeList, int userID){
        incomeList.clear();
        try {
            PreparedStatement statement = DatabaseConnection.newStatement("SELECT date, id, type_id, name, amount, description FROM \"Income\" WHERE user_id = ? ORDER BY date DESC");
            if (statement != null) {
                //We only want incomes from our user
                statement.setInt(1, userID);
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    //Loop through all incomes
                    while (results.next()) {
                        //Add income to list
                        incomeList.add(new Income(results.getInt("id"), results.getInt("amount"), results.getString("name"), results.getString("description"), results.getInt("type_id"), results.getInt("date")));
                    }
                }
            }
        } catch (SQLException e) {
            //An error occurred
            String error = "Database error - can't select all from 'Income' table: " + e.getMessage();
            e.printStackTrace();
            return error;
        }
        return "OK";


    }

    /**
     * @author Jason
     * @param userID the id of our user
     */

    public static void updateRecurringPayment(int userID){

        try {
            PreparedStatement statement = DatabaseConnection.newStatement("SELECT id, recurring, date, name, spending_id, description, amount FROM Spending WHERE user_id = ?");

            if (statement != null) {
                //We only want spending from our user
                statement.setInt(1, userID);
                ResultSet spending_results = statement.executeQuery();

                //Has results
                if (spending_results != null) {
                    //Loop through all results
                    while (spending_results.next()) {
                        //If this is a recurring payment
                        if(spending_results.getInt("recurring") == 1){

                            //copy data
                            int spending_ID = spending_results.getInt("id");
                            String name = spending_results.getString("name");
                            int type_ID = spending_results.getInt("spending_id");
                            String description = spending_results.getString("description");
                            int amount = spending_results.getInt("amount");
                            int recurring = spending_results.getInt("recurring");

                            //New statement for the recurring payment table
                            statement= DatabaseConnection.newStatement("SELECT id, last_updated_date, end_date, interval FROM Recurring_Spending WHERE spending_id = ?");
                            //We only want this recurring spending
                            statement.setInt(1, spending_ID);
                            ResultSet recurSpending_results = statement.executeQuery();


                            if (recurSpending_results != null) {
                                while (recurSpending_results.next()) {
                                    int recurSpending_id = recurSpending_results.getInt("id");
                                    int interval = recurSpending_results.getInt("interval"); //interval in seconds
                                    long start_date = Long.parseLong(recurSpending_results.getString("last_updated_date"));
                                    long end_date = Long.parseLong(recurSpending_results.getString("end_date"));
                                    Date today = new Date();
                                    long today_date = today.getTime() / 1000; //todays date in second, same as other date above
                                    long current_calculating_date = start_date;

                                    //Add payment if the interval is over
                                    while (end_date >= (current_calculating_date + interval)) {

                                        //Keep adding before today
                                        if ((current_calculating_date + interval) < today_date) {
                                            current_calculating_date += interval;
                                            addTransaction(userID, amount, name, description, type_ID, Long.toString(current_calculating_date), recurring);

                                        } else {
                                            break;
                                        }
                                    }

                                    //Update last updated date
                                    statement = DatabaseConnection.newStatement("UPDATE Recurring_Spending set last_updated_date = ? where id = ?");
                                    statement.setString(1, Long.toString(current_calculating_date));
                                    statement.setInt(2, recurSpending_id);
                                    statement.executeUpdate();
                                }
                            }

                        }

                    }
                }
            }
        } catch (SQLException e) {
            //An error occurred
            String error = "Database error - can't select all from 'Spending' table: " + e.getMessage();
            e.printStackTrace();

        }

    }

}
