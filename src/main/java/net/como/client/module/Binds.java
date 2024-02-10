package net.como.client.module;

import net.como.client.ComoClient;
import net.como.client.event.EventHandler;
import net.como.client.event.impl.KeyEvent;

public class Binds extends Module {
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
