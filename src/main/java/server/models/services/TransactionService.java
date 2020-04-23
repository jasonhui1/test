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
     * @author Ceri
     * form parameters
     */
    public static void deleteSpending(User user, String name){

        try {
            PreparedStatement statement = DatabaseConnection.newStatement("DELETE from Spending WHERE user_id = ? AND name = ?");
            Logger.log("trying to edit database");

            if(statement != null){
                //Add data to query
                statement.setInt(1, user.getId());
                statement.setString(2, name);
                statement.executeUpdate();
                Logger.log("transaction removed from database");
            }
        } catch (Exception e){
            Logger.log("Failed");
            e.printStackTrace();
        }

    }
    /**
     * @author Jason
     * Add transaction the database
     * @param transaction the class stores the details of the transaction
     *
     */

    public static void addTransaction(Transaction transaction){
        try {
            String description = transaction.getDescription();
            PreparedStatement statement;

            if(!description.equals("")) {
                statement = DatabaseConnection.newStatement("INSERT INTO Spending (date, user_id, spending_id, name, amount, recurring, description) VALUES (?, ?, ?, ?, ?, ?, ?)");
                statement.setString(7, description);

            } else {
                statement = DatabaseConnection.newStatement("INSERT INTO Spending (date, user_id, spending_id, name, amount, recurring) VALUES (?, ?, ?, ?, ?, ?)");
            }

            if(statement != null){
                //Add data to query
                statement = setTransactionStatement(statement, transaction);
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

    /**
     * @author Jason
     * Add transaction the database
     * @param transaction the class stores the details of the transaction
     * @return spending_ID for recurring spending
     */

    public static int addTransactionReturnId(Transaction transaction){

        int spending_ID = 0;
        try {
            String description = transaction.getDescription();
            PreparedStatement statement;

            if(!description.equals("")) {
                statement = DatabaseConnection.newStatementAndgetID("INSERT INTO Spending (date, user_id, spending_id, name, amount, recurring, description) VALUES (?, ?, ?, ?, ?, ?, ?)");
                statement.setString(7, description);

            } else {
                statement = DatabaseConnection.newStatementAndgetID("INSERT INTO Spending (date, user_id, spending_id, name, amount, recurring) VALUES (?, ?, ?, ?, ?, ?)");
            }

            if(statement != null){
                //Add data to query
                statement = setTransactionStatement(statement, transaction);
                if(statement != null) {
                    statement.executeUpdate();
                    ResultSet rs = statement.getGeneratedKeys();
                    if (rs.next()) {
                        spending_ID = rs.getInt(1);
                    }
                }

                Logger.log("transaction added to database");

            }
        } catch (Exception e){
            Logger.log("Failed to add the transaction to database");
            e.printStackTrace();
        }

        return spending_ID;
    }

    /**
     * @author Jason
     * set the transaction statement to the right parameters
     * @param statement the current statement
     * @param transaction the class stores the details of the transaction
     *
     */

    public static PreparedStatement setTransactionStatement(PreparedStatement statement, Transaction transaction){

        try{
            statement.setInt(1, transaction.getDate_insec());
            statement.setInt(2, transaction.getUserId());
            statement.setInt(3, transaction.getTypeId()); //spending_id
            statement.setString(4, transaction.getName()); //name
            statement.setInt(5, transaction.getAmount()); //amount
            statement.setInt(6, transaction.getRecurring());
            return statement;

        } catch (SQLException e){
            e.printStackTrace();

        }

        return null;
    }

    /**
     * @author Jason
     * Add recurring transaction to the database
     * @param transaction the class stores the details of the recurring transaction
     *
     */
    public static void addRecurringTransaction(RecurringTransaction transaction) {
        try {

            int spending_ID = transaction.getSpendingId();

            if(spending_ID != 0) {
                PreparedStatement statement2 = DatabaseConnection.newStatement("INSERT INTO Recurring_Spending (spending_id, start_date, end_date, interval, last_updated_date) VALUES (?, ?, ?, ?, ?)");

                if (statement2 != null) {
                    statement2.setInt(1, spending_ID);
                    statement2.setInt(2, transaction.getStartDate());
                    statement2.setInt(3, transaction.getEndDate());
                    statement2.setInt(4, transaction.getInterval());
                    statement2.setInt(5, transaction.getLastUpdatedDate());
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
            PreparedStatement statement = DatabaseConnection.newStatement("SELECT date, id, spending_id, name, amount, description, recurring FROM Spending WHERE user_id = ? ORDER BY date DESC");
            if (statement != null) {
                //We only want transactions from our user
                statement.setInt(1, userID);
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    //Loop through all transactions
                    while (results.next()) {
                        //Add transaction to list
                        transactionList.add(new Transaction(results.getInt("id"), results.getInt("amount"), results.getInt("date"), results.getString("name"), results.getString("description"), results.getInt("spending_id"), results.getInt("recurring")));
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
     * @param transactionList the list to populate
     * @param userID the id of our user
     * @return if the get was successful
     */
    public static String getRelevantTransactions(List<Transaction> transactionList, int userID, int timespan){
        transactionList.clear();
        try {
            PreparedStatement statement = DatabaseConnection.newStatement("SELECT date, id, spending_id, name, amount, description, recurring FROM Spending WHERE (user_id = ? && date >= ?) ORDER BY date DESC");
            if (statement != null) {
                //We only want transactions from our user
                statement.setInt(1, userID);
                statement.setInt(2, timespan);
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    //Loop through all transactions
                    while (results.next()) {
                        //Add transaction to list
                        transactionList.add(new Transaction(results.getInt("id"), results.getInt("amount"), results.getInt("date"), results.getString("name"), results.getString("description"), results.getInt("spending_id"), results.getInt("recurring")));
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

    public static void updateRecurringPayment(int userID) {

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
                        if (spending_results.getInt("recurring") == 1) {

                            Transaction transaction = new Transaction(spending_results.getString("name"), spending_results.getString("description"), spending_results.getInt("spending_id"), spending_results.getInt("recurring"), userID, spending_results.getInt("amount"), spending_results.getInt("date"));
                            int spending_id = spending_results.getInt("id");
                            //New statement for the recurring payment table
                            statement = DatabaseConnection.newStatement("SELECT id, start_date, last_updated_date, end_date, interval FROM Recurring_Spending WHERE spending_id = ?");
                            //We only want the recurring spending record with this spending_id, which is a recurring payment
                            statement.setInt(1, spending_id);
                            ResultSet recurSpending_results = statement.executeQuery();

                            if (recurSpending_results != null) {

                                while (recurSpending_results.next()) {
                                    //copy data from the recurring spending table
                                    RecurringTransaction recurringTransaction = new RecurringTransaction(spending_id, recurSpending_results.getInt("start_date"), recurSpending_results.getInt("end_date"), recurSpending_results.getInt("interval"), recurSpending_results.getInt("last_updated_date"));
                                    Date today = new Date();
                                    int today_date = (int) (today.getTime() / 1000); //todays date in second, same as other date above
                                    int current_calculating_date = recurringTransaction.getLastUpdatedDate();
                                    int interval = recurringTransaction.getInterval();

                                    //If the recurring payment is not ended yet, check if new payments need to be added
                                    while (recurringTransaction.getEndDate() >= (current_calculating_date + interval)) {

                                        //Keep adding before today
                                        if ((current_calculating_date + interval) < today_date) {
                                            current_calculating_date += interval;
                                            addTransaction(new Transaction(transaction.getName(), transaction.getDescription(), transaction.getTypeId(), transaction.getRecurring(), transaction.getUserId(), transaction.getAmount(), current_calculating_date));

                                        } else {
                                            break;
                                        }
                                    }

                                    //Id of the changed recurring transaction record
                                    int recurSpending_id = recurSpending_results.getInt("id");
                                    //Update last updated date
                                    statement = DatabaseConnection.newStatement("UPDATE Recurring_Spending set last_updated_date = ? where id = ?");
                                    statement.setInt(1, current_calculating_date);
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
            e.printStackTrace();

        }
    }


    /**
     * Function to get total amount spent on certain category by user
     * @Author Matthew
     * @param userID pass in the ID of the user
     * @param category string of the category to get value of
     * @return TOTAL amount spent on that category
     */
    public static int getSpending(int userID, String category, Boolean recurringPayment) {
        int totalAmount = 0;
        String sqlStatement;
        if (recurringPayment){
            sqlStatement = "SELECT amount FROM SPENDING WHERE user_id = ? AND spending_id = ?";
        }else{
            sqlStatement = "SELECT amount FROM SPENDING WHERE user_id = ? AND spending_id = ? AND recurring = 0";
        }
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(sqlStatement);
            if (statement != null){
                statement.setInt(1, userID);
                statement.setInt(2, getTransactionId(category));
                ResultSet results = statement.executeQuery();
                if (results != null){
                  while (results.next()) {
                      totalAmount = totalAmount + results.getInt("amount");
                  }
                }

            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return totalAmount;
    }



}
