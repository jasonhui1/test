package server.models;



/**
 * @author Alfred
 * The model class for income Types
 */
public class IncomeType {

    private final int id;
    private final String name, description;

    public IncomeType(int id, String name, String description) {
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

    public String getDescription() {
        return description;
    }
}
