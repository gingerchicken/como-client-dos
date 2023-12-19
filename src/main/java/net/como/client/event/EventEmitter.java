package net.como.client.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EventEmitter {
    private final Lock lock = new ReentrantLock();
    private final HashMap<Class<? extends Event>, List<EventListener>> listeners = new HashMap<>();

    /**
     * Register a listener for all events
     * @param listener the listener to register
     */
    public void registerListener(EventListener listener) {
        lock.lock();
        try {
            // Enumerate all methods in the listener
            for (Method method : listener.getClass().getMethods()) {
                // Check if the method has the EventHandler annotation
                if (!method.isAnnotationPresent(EventHandler.class)) continue;

                // Get the first parameter of the method
                Class<?> parameter = method.getParameterTypes()[0];

                // Check if the parameter is a subclass of Event
                if (!Event.class.isAssignableFrom(parameter)) continue;

                // Extract event class from the parameter
                Class<? extends Event> eventClass = (Class<? extends Event>) parameter;

                // Register the listener for the event
                registerListener(eventClass, listener);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Unregister a listener for all events
     * @param listener the listener to unregister
     */
    public void unregisterListener(EventListener listener) {
        lock.lock();
        try {
            // Iterate over all event types
            for (Class<? extends Event> eventType : this.listeners.keySet()) {
                unregisterListener(eventType, listener);
            }
        } finally {
            lock.unlock();
        }
    }

    private void registerListener(Class<? extends Event> event, EventListener listener) {
        // Check if the event type exists in the map
        if (!this.listeners.containsKey(event)) {
            // If it doesn't, then add it
            this.listeners.put(event, new ArrayList<>());
        }

        // Add the listener to the list
        this.listeners.get(event).add(listener);
    }

    private void unregisterListener(Class<? extends Event> event, EventListener listener) {
        // Check if the event type exists in the map
        if (this.listeners.containsKey(event)) {
            // Remove the listener from the list
            this.listeners.get(event).remove(listener);

            // Check if the list is empty
            if (this.listeners.get(event).isEmpty()) {
                // If it is, then remove the event type
                this.listeners.remove(event);
            }
        }
    }

    /**
     * Emit an event
     * @param event the event to emit
     */
    public void emitEvent(Event event) {
        lock.lock();
        try {
            // Check if the event type exists in the map
            if (this.listeners.containsKey(event.getClass())) {
                // Get the list of listeners
                List<EventListener> listeners = this.listeners.get(event.getClass());

                // Iterate over all listeners
                for (EventListener listener : listeners) {
                    // Get the @EventHandler methods
                    for (Method method : listener.getClass().getMethods()) {
                        // Check if the method has the EventHandler annotation
                        if (!method.isAnnotationPresent(EventHandler.class)) continue;

                        // Get the first parameter of the method
                        Class<?> parameter = method.getParameterTypes()[0];

                        // Check if the parameter is a subclass of Event
                        if (!Event.class.isAssignableFrom(parameter)) continue;

                        // Extract event class from the parameter
                        Class<? extends Event> eventClass = (Class<? extends Event>) parameter;

                        // Check if the event class matches the event type
                        if (!eventClass.equals(event.getClass())) continue;

                        // Invoke the method
                        try {
                            method.invoke(listener, event);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }
}
