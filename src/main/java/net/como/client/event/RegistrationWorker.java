package net.como.client.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RegistrationWorker {
    
    private final EventEmitter emitter;

    /**
     * The list of listeners to register
     */
    private List<EventListener> registerQueue = new ArrayList<>();

    /**
     * The list of listeners to unregister
     */
    private List<EventListener> unregisterQueue = new ArrayList<>();

    /**
     * Lock for the register queue
     */
    Lock registerLock = new ReentrantLock();

    /**
     * Lock for the unregister queue
     */
    Lock unregisterLock = new ReentrantLock();

    public RegistrationWorker(EventEmitter emitter) {
        this.emitter = emitter;
    }

    public boolean isFlaggedForRegister(EventListener listener) {
        // Lock the register queue
        registerLock.lock();

        // Result of the contains check
        boolean contains = false;
        
        try {
            // Check if the listener is in the register queue
            contains = registerQueue.contains(listener);
        } finally {
            registerLock.unlock();
        }

        return contains;
    }

    public boolean isFlaggedForUnregister(EventListener listener) {
        // Lock the unregister queue
        unregisterLock.lock();
        boolean contains = false;
        try {
            // Check if the listener is in the unregister queue
            contains = unregisterQueue.contains(listener);
        } finally {
            unregisterLock.unlock();
        }

        return contains;
    }

    public void addRegisterFlag(EventListener listener) {
        registerLock.lock();
        try {
            // Remove from the unregister queue if it's there
            removeUnregisterFlag(listener);

            // Add to the register queue
            registerQueue.add(listener);
        } finally {
            registerLock.unlock();
        }
    }

    public void addUnregisterFlag(EventListener listener) {
        unregisterLock.lock();
        try {
            // Remove from the register queue if it's there
            removeRegisterFlag(listener);

            // Add to the unregister queue
            unregisterQueue.add(listener);
        } finally {
            unregisterLock.unlock();
        }
    }

    /**
     * Remove the register flag for a listener
     * @param listener the listener to remove the flag for
     */
    public void removeRegisterFlag(EventListener listener) {
        registerLock.lock();
        try {
            registerQueue.remove(listener);
        } finally {
            registerLock.unlock();
        }
    }

    /**
     * Remove the unregister flag for a listener
     * @param listener the listener to remove the flag for
     */
    public void removeUnregisterFlag(EventListener listener) {
        unregisterLock.lock();
        try {
            unregisterQueue.remove(listener);
        } finally {
            unregisterLock.unlock();
        }
    }

    /**
     * Register all listeners in the register queue
     */
    public void registerAll() {
        registerLock.lock();
        try {
            for (EventListener listener : registerQueue) {
                emitter.registerListener(listener);
            }
            registerQueue.clear();
        } finally {
            registerLock.unlock();
        }
    }

    /**
     * Unregister all listeners in the unregister queue
     */
    public void unregisterAll() {
        unregisterLock.lock();
        try {
            for (EventListener listener : unregisterQueue) {
                emitter.unregisterListener(listener);
            }
            unregisterQueue.clear();
        } finally {
            unregisterLock.unlock();
        }
    }
}
