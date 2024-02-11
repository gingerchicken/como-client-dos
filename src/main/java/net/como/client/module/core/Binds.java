package net.como.client.module.core;

import net.como.client.ComoClient;
import net.como.client.event.EventHandler;
import net.como.client.event.impl.KeyEvent;
import net.como.client.module.Module;
import net.como.client.utils.ChatUtils;

public class Binds extends Module {
    public Binds() {
        this.setDescription("Allows you to use keys to trigger events");
        this.setCategory("Core");
    }

    @EventHandler
    public void onKeyEvent(KeyEvent event) {
        // Ensure key down
        if (event.action != 1) return;

        // Trigger the bind
        if (!ComoClient.getInstance().getBindings().fireBind(event.key)) {
            return; // No binds were triggered
        }

        // Cancel the event (prevent vanilla keybinds from triggering)
        event.info.cancel();
    }
}
