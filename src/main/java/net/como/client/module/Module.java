package net.como.client.module;

import net.como.client.ComoClient;
import net.como.client.event.EventListener;
import net.como.client.utils.ChatUtils;

public abstract class Module implements EventListener {
    private boolean enabled;
    private String category = "Misc"; // TODO enum?
    private String description;

    /**
     * Get if the module is enabled
     * @return if the module is enabled or not
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Set the exact enabled value
     * @param enabled is it enabled
     */
    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;

        // Show the status message
        showStatusMessage();

        // Trigger the onEnable or onDisable method
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    /**
     * Invert if the module is enabled or not
     */
    public void toggle() {
        setEnabled(!isEnabled());
    }

    /**
     * Get the fixed module name
     * @return the module name
     */
    public String getName() {
        // Get the class
        return this.getClass().getSimpleName();
    }

    private void showStatusMessage() {
        String status = isEnabled() ? ChatUtils.GREEN + "enabled" : ChatUtils.RED + "disabled";

        ChatUtils.info(getName() + " is now " + status);
    }

    /**
     * Called when the module is enabled
     */
    protected void onEnable() {
        ComoClient.getInstance().getRegistrationWorker().addRegisterFlag(this);
    }

    /**
     * Called when the module is disabled
     */
    protected void onDisable() {
        ComoClient.getInstance().getRegistrationWorker().addUnregisterFlag(this);
    }

    protected void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    protected void info(String message) {
        ChatUtils.info(this.getName(), message);
    }

    protected void error(String message) {
        ChatUtils.error(this.getName(), message);
    }
}
