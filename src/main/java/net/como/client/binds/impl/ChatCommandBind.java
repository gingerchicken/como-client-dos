package net.como.client.binds.impl;

import net.como.client.ComoClient;
import net.como.client.binds.Bind;
import net.minecraft.client.gui.screen.ChatScreen;

public class ChatCommandBind implements Bind {
    @Override
    public void fire() {
        // Check if there is no current screen
        if (ComoClient.getInstance().getClient().currentScreen != null) return;

        // Open the chat
        ComoClient.getInstance().getClient().setScreen(new ChatScreen(""));
    }
}
