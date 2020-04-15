package server.models;

import org.json.simple.JSONObject;
import server.models.services.TransactionService;

import java.util.Date;


/**
 * @author Alfred
 * The model class for transactions
 */
public class Transaction {

    private final int id, amount, recurring, userId, typeId, date_insec;
    private final String name, description;
    private final TransactionType type;
    private final Date date;



    public Transaction(int id, int amount, int date, String name, String description, int typeId, int recurring) {
        this.id = id;
        this.amount = amount;
        this.date = new java.util.Date((long)date*1000);
        this.name = name;
        this.description = description;
        this.type = TransactionService.getTransactionType(typeId);
        this.recurring = recurring;
        this.userId = 0;
        this.date_insec = 0;
        this.typeId = 0;
    }

    public Transaction(String name, String description, int typeId, int recurring, int userId, int amount, int date) {
        this.userId = userId;
        this.amount = amount;
        this.date_insec = date;
        this.date = new java.util.Date((long)date*1000);
        this.name = name;
        this.description = description;
        this.typeId = typeId;
        this.recurring = recurring;
        this.id = 0;
        this.type = TransactionService.getTransactionType(typeId);
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public Date getDate() {
        return date;
    }

    public int getDate_insec() { return date_insec; }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TransactionType getType() {
        return type;
    }

    public int getTypeId() { return typeId; }

    public int getAmount() {
        return amount;
    }

    public int getRecurring() {return recurring;}

    //Create a json file representing the object
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("id", getId());
        j.put("date", getDate().getTime());
        j.put("amount", getAmount());
        j.put("name", getName());
        j.put("typeName", getType().getName());
        j.put("description", getDescription());
        return j;
    }
}