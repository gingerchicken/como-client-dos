package net.como.client.event;

public class RegistrationWorkerDaemon {
    private abstract class RegistrationThread extends Thread {
        public boolean running = true;

        abstract protected void operate();

        @Override
        public void run() {
            while (running) {
                operate();
                // Sleep for a millisecond
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class RegisterThread extends RegistrationThread {
        @Override
        protected void operate() {
            worker.registerAll();
        }
    }

    private class UnregisterThread extends RegistrationThread {
        @Override
        protected void operate() {
            worker.unregisterAll();
        }
    }

    private final RegistrationWorker worker;

    UnregisterThread unregisterThread;
    RegisterThread registerThread;

    public RegistrationWorkerDaemon(RegistrationWorker worker) {
        this.worker = worker;
    }

    /**
     * Start the registration worker daemon
     */
    public void start() {
        if (registerThread != null || unregisterThread != null) {
            return;
        }

        // Create the threads
        RegisterThread registerThread = new RegisterThread();
        UnregisterThread unregisterThread = new UnregisterThread();

        // Start the threads
        registerThread.start();
        unregisterThread.start();
    }

    /**
     * Stop the registration worker daemon
     */
    public void stop() {
        if (registerThread == null || unregisterThread == null) {
            return;
        }

        registerThread.running = false;
        unregisterThread.running = false;
    }
}
