package server.models.services;

import server.DatabaseConnection;
import server.Logger;
import server.models.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {

    private final static List<Object> transactionTypes = getTypes("Spending_Type");
    private final static List<Object> incomeTypes = getTypes("Income_Type");

    /**
     * @author Jason
     * Add transaction the database
     * Form parameters
     *
     */
    public static void addTransaction(User user, int amount, String name, String description, String type, String date){

        try {
            PreparedStatement statement = DatabaseConnection.newStatement("INSERT INTO Spending (date, user_id, spending_id, name, description, amount) VALUES (?, ?, ?, ?, ?, ?)");

            if(statement != null){
                //Add data to query
                statement.setString(1, date);
                statement.setInt(2, user.getId());
                statement.setInt(3, getTransactionId(type)); //spending_id
                statement.setString(4, name); //name
                statement.setString(5, description); //description
                statement.setInt(6, amount); //amount

                statement.executeUpdate();
                Logger.log("transaction added to database");
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



}
