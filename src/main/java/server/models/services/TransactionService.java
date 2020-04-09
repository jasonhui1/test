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

        try {
            PreparedStatement statement = DatabaseConnection.newStatement("INSERT INTO Spending (date, user_id, spending_id, name, description, amount, recurring) VALUES (?, ?, ?, ?, ?, ?, ?)");

            if(statement != null){
                //Add data to query
                statement.setString(1, date);
                statement.setInt(2, user.getId());
                statement.setInt(3, getTransactionId(type)); //spending_id
                statement.setString(4, name); //name
                statement.setString(5, description); //description
                statement.setInt(6, amount); //amount
                statement.setInt(7, recurring);

                statement.executeUpdate();
                Logger.log("transaction added to database");
            }
        } catch (Exception e){
            Logger.log("Failed to add the transaction to database");
            e.printStackTrace();
        }

    }

    public static void addTransaction(int user_ID, int amount, String name, String description, int type_ID, String date, int recurring){

        try {
            PreparedStatement statement = DatabaseConnection.newStatement("INSERT INTO Spending (date, user_id, spending_id, name, description, amount, recurring) VALUES (?, ?, ?, ?, ?, ?, ?)");

            if(statement != null){
                //Add data to query
                statement.setString(1, date);
                statement.setInt(2, user_ID);
                statement.setInt(3, type_ID); //spending_id
                statement.setString(4, name); //name
                statement.setString(5, description); //description
                statement.setInt(6, amount); //amount
                statement.setInt(7, recurring);

                statement.executeUpdate();
                Logger.log("transaction added to database");
            }
        } catch (Exception e){
            Logger.log("Failed to add the transaction to database");
            e.printStackTrace();
        }

    }

    public static void addRecurringTransaction(User user, int amount, String name, String description, String type, String date, int interval, String endDate) {
        try {

            PreparedStatement statement1 = DatabaseConnection.newStatementAndgetID("INSERT INTO Spending (date, user_id, spending_id, name, description, amount, recurring) VALUES (?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement statement2 = DatabaseConnection.newStatement("INSERT INTO Recurring_Spending (spending_id, start_date, end_date, interval, last_updated_date) VALUES (?, ?, ?, ?, ?)");
            int spending_ID = 0;
            if(statement1 != null){
                //Add data to the spending database query
                statement1.setString(1, date);
                statement1.setInt(2, user.getId());
                statement1.setInt(3, getTransactionId(type)); //spending_id
                statement1.setString(4, name); //name
                statement1.setString(5, description); //description
                statement1.setInt(6, amount); //amount
                statement1.setInt(7, 1); //amount
//                statement1.setString(7, endDate); //endDate
//                statement1.setInt(8, interval); //interval
                statement1.execute();
                ResultSet rs = statement1.getGeneratedKeys();
                if (rs.next()) {
                    spending_ID = rs.getInt(1);
                }
                Logger.log("transaction added to database");
            }

            if(statement2 != null){
                statement2.setInt(1, spending_ID);
                statement2.setString(2, date);
                statement2.setString(3, endDate);
                statement2.setInt(4, interval);
                statement2.setString(5, date);
                statement2.executeUpdate();
                Logger.log("recurring transaction added to database");
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

    public static void checkRecurringPayment(int userID){



        try {
            PreparedStatement statement = DatabaseConnection.newStatement("SELECT id, recurring, date, name, spending_id, description, amount FROM Spending WHERE user_id = ?");

            if (statement != null) {
                //We only want spending from our user
                statement.setInt(1, userID);
                ResultSet spending_results = statement.executeQuery();

                if (spending_results != null) {
                    //Loop through all incomes
                    while (spending_results.next()) {
                        int spending_ID = spending_results.getInt("id");
                        String date = spending_results.getString("date");
                        String name = spending_results.getString("name");
                        int type_ID = spending_results.getInt("spending_id");
                        String description = spending_results.getString("description");
                        int amount = spending_results.getInt("amount");
                        int recurring = spending_results.getInt("recurring");

                        //Add recurring payment
                        if(spending_results.getInt("recurring") == 1){
                            statement= DatabaseConnection.newStatement("SELECT id, last_updated_date, end_date, interval FROM Recurring_Spending WHERE spending_id = ?");
                            //We only want spending from our user
                            statement.setInt(1, spending_ID);
                            ResultSet recurSpending_results = statement.executeQuery();
                            Logger.log("Spending ID" + spending_ID);
                            if (recurSpending_results != null) {
                                while (recurSpending_results.next()) {
                                    int recurSpending_id = recurSpending_results.getInt("id");
                                    int interval = recurSpending_results.getInt("interval");
                                    long start_date = Long.parseLong(recurSpending_results.getString("last_updated_date"));
                                    long end_date = Long.parseLong(recurSpending_results.getString("end_date"));
                                    Date today = new Date();
                                    long today_date = today.getTime() / 1000;
                                    long current_calculating_date = start_date;

                                    while (end_date >= (current_calculating_date + interval)) {
                                        //Payment made before today

                                        if (current_calculating_date < today_date) {
                                            current_calculating_date += interval;
                                            addTransaction(userID, amount, name, description, type_ID, Long.toString(current_calculating_date), recurring);
                                            Logger.log("" + current_calculating_date);
                                        } else {
                                            break;
                                        }
                                    }

                                    statement = DatabaseConnection.newStatement("UPDATE Recurring_Spending set last_updated_date = ? where id = ?");
                                    //change
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
