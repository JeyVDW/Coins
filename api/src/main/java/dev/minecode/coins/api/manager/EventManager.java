package dev.minecode.coins.api.manager;

import dev.minecode.coins.api.object.CoinsEvent;
import dev.minecode.coins.api.object.CoinsListener;

public interface EventManager {

    void registerListener(CoinsListener listener);

    void unregisterListener(CoinsListener listener);

    <T extends CoinsEvent> T callEvent(T coreEvent);

}