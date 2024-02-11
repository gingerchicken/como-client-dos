package net.como.client.module.core;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.como.client.ComoClient;
import net.como.client.commands.Commands;
import net.como.client.event.EventHandler;
import net.como.client.event.impl.SendChatMessageEvent;
import net.como.client.module.Module;
import net.como.client.utils.ChatUtils;

public class ChatCommands extends Module {
    public ChatCommands() {
        this.setDescription("Allows you to run commands in chat");
        this.setCategory("Core");
    }

    @EventHandler
    public void onMessage(SendChatMessageEvent event) {
        String message = event.message;
        Commands commands = ComoClient.getInstance().getCommands();

        // Check if the message starts with the command prefix
        if (!message.startsWith(commands.getPrefix())) return;

        // Get the main command
        String subMessage = message.substring(commands.getPrefix().length());
    
        // Run the command
        try {
            commands.dispatch(subMessage);
        } catch (CommandSyntaxException e)  {
            ChatUtils.error(e.getContext());
        }

        // Cancel the original message
        event.info.cancel();

        // Add the message to the chat history
        ComoClient.getInstance().getClient().inGameHud.getChatHud().addToMessageHistory(subMessage);
    }
}
