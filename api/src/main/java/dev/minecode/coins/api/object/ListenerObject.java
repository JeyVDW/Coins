package dev.minecode.coins.api.object;

import java.lang.reflect.Method;

public class ListenerObject {

    private CoinsListener coinsListener;
    private Method method;
    private int eventPriority;

    public ListenerObject(CoinsListener coinsListener, Method method, int eventPriority) {
        this.coinsListener = coinsListener;
        this.method = method;
        this.eventPriority = eventPriority;
    }

    public CoinsListener getCoinsListener() {
        return coinsListener;
    }

    public Method getMethod() {
        return method;
    }

    public int getEventPriority() {
        return eventPriority;
    }
}
