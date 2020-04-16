package server.models;

public class Budget {
    private final int user_id, amount;
    public Budget(int user_id, int amount){
        this.user_id = user_id;
        this.amount = amount;
    }

    public int getUser_id(){
        return user_id;
    }

    public int getAmount(){
        return amount;
    }
}
