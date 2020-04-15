package server.models.services;

import server.DatabaseConnection;
import server.Logger;
import server.models.Budget;
import server.models.IncomeType;
import server.models.TransactionType;
import server.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BudgetService {
    public static void addBudget(Budget budget){
        try {
            PreparedStatement statement = DatabaseConnection.newStatement("INSERT INTO Budget (user_id, budget) VALUES (?, ?)");
            Logger.log("trying to add to database budget");

            if(statement != null){
                //Add data to query
                statement.setInt(1, budget.getUser_id());
                statement.setInt(2, budget.getAmount());
                statement.executeUpdate();
                Logger.log("transaction added to database");
            }
        } catch (Exception e){
            Logger.log("Failed");
            e.printStackTrace();
        }
    }

    public static void changeBudget(Budget budget){
        try {
            PreparedStatement statement = DatabaseConnection.newStatement("UPDATE Budget set budget = ? where user_id = ?");
            Logger.log("trying to change database budget");

            if(statement != null){
                //Add data to query
                statement.setInt(2, budget.getUser_id());
                statement.setInt(1, budget.getAmount());
                statement.executeUpdate();
                Logger.log("budget changed");
            }
        } catch (Exception e){
            Logger.log("Failed");
            e.printStackTrace();
        }
    }

    public static int getBudget(int user_id) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement("SELECT user_id, budget FROM Budget WHERE user_id = ?");

            if(statement != null){
                //Add data to query
                statement.setInt(1, user_id);
                ResultSet results = statement.executeQuery();
                if (results != null) {

                    return results.getInt("budget");

                }
            }

        } catch (SQLException e){
            return -1;
        }

        return -1;
    }
}
