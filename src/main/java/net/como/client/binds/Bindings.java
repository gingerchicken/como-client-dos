package net.como.client.binds;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bindings {
    private HashMap<Integer, ArrayList<Bind>> binds = new HashMap<>();

    // The lock for the map to prevent concurrent modification
    private Lock lock = new ReentrantLock();

    /**
     * Add a bind to the queue for a key
     * @param key the key to bind the bind to
     * @param bind the bind to add
     */
    public void appendBind(int key, Bind bind) {
        // Get the lock
        this.lock.lock();

        try {
            if (!this.binds.containsKey(key)) {
                this.binds.put(key, new ArrayList<>()); // Add a new ArrayList if the key is not already in the map
            }

            this.binds.get(key).add(bind); // Add the bind to the list of binds for the key
        } finally {
            this.lock.unlock(); // Release the lock
        }
    }

    /**
     * Prepend a bind to the queue for a key
     * @param key the key to prepend the bind to
     * @param bind the bind to prepend
     */
    public void prependBind(int key, Bind bind) {
        this.lock.lock(); // Get the lock

        try {
            if (!this.binds.containsKey(key)) {
                this.binds.put(key, new ArrayList<>()); // Add a new ArrayList if the key is not already in the map

                // Add the bind to the list of binds for the key
                this.binds.get(key).add(bind);

                // Done!
            } else {
                // Create a new list
                ArrayList<Bind> list = new ArrayList<>();
                list.add(bind); // Add the bind to the list
                list.addAll(this.binds.get(key)); // Add all the binds for the key to the list

                this.binds.put(key, list); // Replace the list of binds for the key with the new list
            }
        } finally {
            this.lock.unlock(); // Release the lock
        }
    }

    /**
     * Remove a bind from the queue for a key
     * @param key the key to remove the bind from
     * @param bind the bind to remove
     */
    public void removeBind(int key, Bind bind) {
        this.lock.lock(); // Get the lock

        try {
            // Firstly, check if there is a bind for the key
            if (!this.binds.containsKey(key)) {
                // Unlock the lock and return since there is nothing to do.
                this.lock.unlock();
                return;
            }

            // Get the list of binds for the key
            ArrayList<Bind> list = this.binds.get(key);

            // Check if the list contains the bind
            if (list.contains(bind)) {
                // Remove the bind from the list
                list.remove(bind);
            }

            // Check if the list is empty
            if (list.isEmpty()) {
                // Remove the list from the map
                this.binds.remove(key);
            }
        } finally {
            this.lock.unlock(); // Release the lock
        }
    }

    public boolean fireBind(int key) {
        // Get the lock
        this.lock.lock();

        boolean fired = false;

        try {
            // Check if there is a bind for this key
            if (this.binds.containsKey(key)) {
                // Get the list of binds for the key
                ArrayList<Bind> list = this.binds.get(key);

                // Loop through the list and fire each bind
                for (Bind bind : list) {
                    bind.fire(); // TODO add on another thread to prevent blocking?
                    fired = true; // Set fired to true
                }
            }
        } finally {
            this.lock.unlock(); // Release the lock
        }

        // TODO what if a bind bind/unbinds something?
        return fired;
    }
}
