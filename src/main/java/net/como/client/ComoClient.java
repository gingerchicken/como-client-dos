package net.como.client;

import net.como.client.event.EventEmitter;
import net.como.client.event.RegistrationWorker;
import net.como.client.event.RegistrationWorkerDaemon;
import net.como.client.module.Module;
import net.como.client.module.render.Greeter;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;

public class ComoClient implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("como-client");

    private static ComoClient instance;

    // Modules
    private HashMap<Class<? extends Module>, Module> modules = new HashMap<>();

    // Events
    private EventEmitter eventEmitter = new EventEmitter();
    private RegistrationWorker registrationWorker = new RegistrationWorker(eventEmitter);
    private RegistrationWorkerDaemon registrationWorkerDaemon = new RegistrationWorkerDaemon(registrationWorker);

    /**
     * Get the instance of the ComoClient as a singleton
     * @return the instance of the ComoClient
     */
    public static ComoClient getInstance() {
        return instance;
    }

    /**
     * Get the registration worker
     * @return the registration worker
     */
    public RegistrationWorker getRegistrationWorker() {
        return registrationWorker;
    }

    /**
     * Get the registration worker daemon
     * @return the registration worker daemon
     */
    public RegistrationWorkerDaemon getRegistrationWorkerDaemon() {
        return registrationWorkerDaemon;
    }

    /**
     * Get the event emitter
     * @return the event emitter
     */
    public EventEmitter getEventEmitter() {
        return eventEmitter;
    }

	@Override
	public void onInitialize() {
        instance = this;

		LOGGER.info("Oh bugger, not this again.");

        getRegistrationWorkerDaemon().start(); // TODO kill this!

        // Register modules
        this.registerModules();
	}

    public boolean registerModule(Module module) {
        // Check if it already exists
        // Get the class
        Class<? extends Module> moduleClass = module.getClass();

        // If it does, then return false
        if (this.modules.containsKey(moduleClass)) return false;

        // Add the module
        this.modules.put(moduleClass, module);

        // Log the add
        LOGGER.info(String.format("Registered module '%s'", moduleClass.toString()));

        // Success
        return true;
    }

    public Module getModuleByClass(Class<? extends Module> clazz) {
        return this.modules.get(clazz);
    }

    private void registerModules() {
        this.registerModule(new Greeter());

        this.getModuleByClass(Greeter.class).setEnabled(true);
    }
}
