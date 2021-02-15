package dev.minecode.coins.api.object;

public enum CoinsEventPriority {

    LOW(0), NORMAL(1), HIGH(2);

    private int priorityInt;

    CoinsEventPriority(int priorityInt) {
        this.priorityInt = priorityInt;
    }

    public int getPriorityInt() {
        return priorityInt;
    }
}