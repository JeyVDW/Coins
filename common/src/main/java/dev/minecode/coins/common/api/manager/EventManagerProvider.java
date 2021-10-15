package dev.minecode.coins.common.api.manager;

import dev.minecode.coins.api.manager.EventManager;
import dev.minecode.coins.api.object.CoinsEvent;
import dev.minecode.coins.api.object.CoinsEventHandler;
import dev.minecode.coins.api.object.CoinsListener;
import dev.minecode.coins.api.object.ListenerObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class EventManagerProvider implements EventManager {

    public EventManagerProvider() {
    }

    public static ArrayList<CoinsListener> getCoinsListeners() {
        return coinsListeners;
    }
    @Override
    public <T extends CoinsEvent> T callEvent(T coreEvent) {
        ArrayList<ListenerObject> listenerObjects = new ArrayList<>();
        for (CoinsListener listener : coinsListeners) {
            for (int i = 0; i < listener.getClass().getDeclaredMethods().length; i++) {
                Method method = listener.getClass().getDeclaredMethods()[i];
                for (int j = 0; j < method.getAnnotations().length; j++) {
                    Annotation annotation = method.getAnnotations()[j];
                    if (annotation.annotationType() == CoinsEventHandler.class) {
                        CoinsEventHandler coinsEventHandler = (CoinsEventHandler) annotation;
                        listenerObjects.add(new ListenerObject(listener, method, coinsEventHandler.eventPriority().getPriorityInt()));
                    }
                }
            }
        }

        for (int i = 2; i >= 0; i--)
            callEventsByPriority(listenerObjects, i);
        return coreEvent;
    }

    private static ArrayList<CoinsListener> coinsListeners = coinsListeners = new ArrayList<>();

    @Override
    public void registerListener(CoinsListener listener) {
        coinsListeners.add(listener);
    }

    @Override
    public void unregisterListener(CoinsListener listener) {
        coinsListeners.remove(listener);
    }


    private void callEventsByPriority(ArrayList<ListenerObject> listenerObjects, int priority) {
        for (ListenerObject listenerObject : listenerObjects) {
            if (listenerObject.getEventPriority() == priority) {
                try {
                    listenerObject.getMethod().invoke(listenerObject.getCoinsListener());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}