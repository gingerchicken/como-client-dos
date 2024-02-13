package net.como.client.commands;

import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.como.client.commands.impl.CheckCommand;
import net.como.client.commands.impl.CorruptCommand;
import net.como.client.commands.impl.MinefortFindCommand;
import net.como.client.commands.impl.ParticleCrash;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandSource;

public class Commands {
    private final CommandDispatcher<CommandSource> dispatcher = new CommandDispatcher<>();
    private final CommandSource source = new ClientCommandSource(null, MinecraftClient.getInstance());
    private final List<Command> commands = new ArrayList<>();
    private String prefix;

    /**
     * Create a new command manager with a specific prefix
     * @param prefix The prefix
     */
    public Commands(String prefix) {
        // Set the prefix
        this.prefix = prefix;
    }

    /**
     * Create a new command manager with the default prefix
     */
    public Commands() {
        this(".");
    }

    public CommandDispatcher<CommandSource> getDispatcher() {
        return dispatcher;
    }

    public CommandSource getSource() {
        return source;
    }

    /**
     * Set the prefix
     * @param prefix The prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Get the prefix
     * @return The prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Register all commands
     */
    public void registerAll() {
        register(new CheckCommand());
        register(new CorruptCommand());
        register(new ParticleCrash());
        register(new MinefortFindCommand());

        // Add all module commands
        ModuleCommandFactory.createCommands().forEach(this::register);
    }

    public void register(Command command) {
        // Check if the command is already registered
        if (commands.contains(command)) {
            return;
        }
        command.register(dispatcher); // Register the command
        commands.add(command); // Add the command to the list

        // Sort the list
        commands.sort((a, b) -> a.getName().compareTo(b.getName()));
    }

    /**
     * Execute a command (without the prefix)
     * @param command The command to execute
     * @throws CommandSyntaxException If the command syntax is invalid
     */
    public void dispatch(String command) throws CommandSyntaxException {
        dispatcher.execute(command, source); // Execute the command
    }

    /**
     * Get a command by its name
     * @param name The name of the command
     * @return The command (or null if not found)
     */
    public Command getCommandByName(String name) {
        // TODO If the list is sorted, why not use a binary search?
        for (Command command : commands) {
            if (!command.getName().equals(name)) {
                continue;
            }

            // Return the command
            return command;
        }

        // No command found
        return null;
    }

    /**
     * Get the commands list
     * @return The commands iterator
     */
    public Iterable<Command> getCommands() {
        return commands;
    }
}
