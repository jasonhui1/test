package server.models;

import org.json.simple.JSONObject;
import server.models.services.TransactionService;

import java.util.Date;

/**
 * @author Alfred
 * The model class for incom
 */
public class Income {

    private final int id, amount;
    private final String name, description;
    private final IncomeType type;
    private final Date date;


    public Income(int id, int amount, String name, String description, int typeId, int date) {
        this.id = id;
        this.amount = amount;
        this.name = name;
        this.description = description;
        this.type = TransactionService.getIncomeType(typeId);;
        this.date = new java.util.Date((long)date*1000);
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public IncomeType getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }


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
