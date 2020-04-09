package server.controllers;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Logger;
import server.models.Income;
import server.models.Transaction;
import server.models.User;
import server.models.services.TransactionService;
import server.models.services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("transaction/")
public class TransactionController {

    /**
     * @author Alfred Jones
     *
     * This is a template for how we can send our data to add a transaction to the database
     */

    @POST
    @Path("submit/add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String AddTransaction(@CookieParam("sessionToken") Cookie sessionCookie,
                                 @FormParam("amount") float amount,
                                 @FormParam("name") String name,
                                 @FormParam("description") String description,
                                 @FormParam("type") String type,
                                 @FormParam("date") String date,
                                 @FormParam("recurring_interval") float interval,
                                 @FormParam("end_date") String endDate) throws ParseException {

        User user = UserService.ValidateSessionToken(sessionCookie);
        Logger.log("new transaction");
        //Date format
        Date formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(date);
        long datems = formatDate.getTime();
        //Put date in string in millisecond
        String dateToDatebase = Long.toString(datems/1000);
        int pence = (int)(amount*100);
        Logger.log("" + (interval <= 0.0));

        if (user != null) {
            //Add the transaction
            try {
                if(interval <= 0.0) {
                    TransactionService.addTransaction(user, pence, name, description, type, dateToDatebase, 0);
                } else {
                    int interval_in_sec = (int)(interval*3600);
                    Date formatEndDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(endDate);
                    long datems2 = formatEndDate.getTime();
                    String EnddateToDatebase = Long.toString(datems2/1000);
                    TransactionService.addRecurringTransaction(user, pence, name, description, type, dateToDatebase, interval_in_sec, EnddateToDatebase);

                }
            } catch(Exception e){
                e.printStackTrace();
            }
            Logger.log("A user has added a transaction");

        } else {
            //The user isn't logged in and shouldn't be able to make database changes
            Logger.log("An unauthorised attempted at adding a transaction was occurred");
            return "error";
        }

        return  "success";
    }
    /**
     * @author Ceri Griffiths
     *
     * This is a template for how we can send our data to add an income to the database
     */
    @POST
    @Path("submit/income")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String AddIncome(@CookieParam("sessionToken") Cookie sessionCookie,
                            @FormParam("incomeName") String name,
                            @FormParam("incomeDate") String date,
                            @FormParam("incomeAmount") float amount,
                            @FormParam("incomeType") String type,
                            @FormParam("incomeDescription") String description) throws ParseException {

        User user = UserService.ValidateSessionToken(sessionCookie);
        //Date format
        Date formatDate = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm").parse(date);
        long datems = formatDate.getTime();
        //Put date in string in millisecond
        String dateToDatebase = Long.toString(datems/1000);
        int pence = (int)(amount*100);

        if (user != null) {
            //Add the transaction
            try {
                TransactionService.addIncome(user, name, dateToDatebase,pence, type, description);
            } catch(Exception e){
                e.printStackTrace();
            }
            Logger.log("A user has added an income form");

        } else {
            //The user isn't logged in and shouldn't be able to make database changes
            Logger.log("An unauthorised attempted at adding an income was occurred");
            return "error";
        }

        return  "type: " + type +  " date: " + date;
    }



    /**
     * @author Alfred Jones
     * @param sessionCookie The cookie of the user
     * @return spending in json format
     */
    @GET
    @Path("get/spending")
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("unchecked")
    public String getRelevantTransactions(@CookieParam("sessionToken") Cookie sessionCookie) {
        JSONObject response = new JSONObject();
        User user = UserService.ValidateSessionToken(sessionCookie);
        if(user != null) {
            ArrayList<Transaction> transactions = new ArrayList<>();
            //Read shifts into arrayList
            String status = TransactionService.getRelevantTransactions(transactions, user.getId());
            if (status.equals("OK")) {
                //Shifts were successfully added to list
                //Convert shifts to json format and return to client
                return TransactionListTOJSON(transactions);
            } else {
                //Something went wrong, return and error along with the error message
                response.put("error", status);
                return response.toString();
            }
        }else{
            //Return an error if the user doesn't have a valid session token
            response.put("error", "Invalid Session Token");
            return response.toString();
        }
    }

    /**
     * @author Alfred Jones
     * @param sessionCookie The cookie of the user
     * @return income in json format
     */
    @GET
    @Path("get/income")
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("unchecked")
    public String getRelevantIncome(@CookieParam("sessionToken") Cookie sessionCookie) {
        JSONObject response = new JSONObject();
        User user = UserService.ValidateSessionToken(sessionCookie);
        if(user != null) {
            ArrayList<Income> incomes = new ArrayList<>();
            //Read income into arrayList
            String status = TransactionService.getRelevantIncome(incomes, user.getId());
            if (status.equals("OK")) {
                //incomes were successfully added to list
                //Convert incomes to json format and return to client
                return IncomeListTOJSON(incomes);
            } else {
                //Something went wrong, return and error along with the error message
                response.put("error", status);
                return response.toString();
            }
        }else{
            //Return an error if the user doesn't have a valid session token
            response.put("error", "Invalid Session Token");
            return response.toString();
        }
    }

    /**
     * @author Alfred Jones
     * @return json
     * Convert spending list to json format
     */
    @SuppressWarnings("unchecked")
    private String TransactionListTOJSON(List<Transaction> list) {
        JSONArray messageList = new JSONArray();
        //Loop through each transaction
        for (Transaction t: list) {
            //convert to json and add to new list
            messageList.add(t.toJSON());
        }
        return messageList.toString();
    }

    /**
     * @author Alfred Jones
     * @return json
     * Convert income list to json format
     */
    @SuppressWarnings("unchecked")
    private String IncomeListTOJSON(List<Income> list) {
        JSONArray messageList = new JSONArray();
        //Loop through each income
        for (Income t: list) {
            //convert to json and add to new list
            messageList.add(t.toJSON());
        }
        return messageList.toString();
    }


}
