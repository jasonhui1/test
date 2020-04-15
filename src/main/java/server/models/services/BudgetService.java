package server.models.services;

import server.DatabaseConnection;
import server.Logger;
import server.models.Budget;
import server.models.User;

import java.sql.PreparedStatement;

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
}
