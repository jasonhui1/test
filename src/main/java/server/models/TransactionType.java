package server.models;


/**
 * @author Alfred
 * The model class for transaction types
 */
public class TransactionType {

    private final int id;
    private final String name, description;

    public TransactionType(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
