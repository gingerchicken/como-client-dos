package net.como.client.commands;

import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import net.minecraft.command.CommandSource;

public abstract class Command {
    private final String name;
    private final String description;
    private final List<String> aliases = new ArrayList<>();

    public Command(String name, String description, String... aliases) {
        this.name = name;
        this.description = description;

        for (String alias : aliases) {
            this.aliases.add(alias); // Add the aliases
        }
    }

    public Command(String name) {
        this(name, "No description :(");
    }

    /**
     * Create a required argument
     * @param <T> The type of the argument
     * @param name The name of the argument
     * @param type The type of the argument
     * @return The argument
     */
    protected static <T> RequiredArgumentBuilder<CommandSource, T> argument(final String name, final ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    /**
     * Create a literal argument
     * @param name The name of the argument
     * @return The argument
     */
    protected static LiteralArgumentBuilder<CommandSource> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    /**
     * Register the command and its aliases
     * @param dispatcher
     */
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        register(dispatcher, name);
        // Register aliases
        for (String alias : aliases) {
            register(dispatcher, alias);
        }
    }

    /**
     * Register the command
     * @param dispatcher The command dispatcher
     * @param name The name to register
     */
    private void register(CommandDispatcher<CommandSource> dispatcher, String name) {
        LiteralArgumentBuilder<CommandSource> builder = LiteralArgumentBuilder.literal(name);
        build(builder);
        dispatcher.register(builder);
    }

    /**
     * Get the name of the command
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the description of the command
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the aliases for the command
     * @return An iterable of aliases
     */
    public Iterable<String> getAliases() {
        return aliases;
    }

    /**
     * Build the command
     * @param builder The command builder
     */
    public abstract void build(LiteralArgumentBuilder<CommandSource> builder);
}
