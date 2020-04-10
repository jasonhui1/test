package server.models;

public class RecurringTransaction {

    private final int spendingId, startDate, endDate, interval, lastUpdatedDate;

    public RecurringTransaction(int spendingId, int startDate, int endDate, int interval, int lastUpdatedDate){

        this.spendingId = spendingId;
        this.startDate = startDate;
        this.endDate = endDate;
        this .interval = interval;
        this.lastUpdatedDate = lastUpdatedDate;

    }

    public int getSpendingId(){
        return spendingId;
    }

    public int getEndDate() {
        return endDate;
    }

    public int getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public int getStartDate() {
        return startDate;
    }

    public int getInterval() {
        return interval;
    }
}
