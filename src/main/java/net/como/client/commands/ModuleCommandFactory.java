package net.como.client.commands;

import net.como.client.ComoClient;
import net.como.client.module.Module;
import net.como.client.commands.impl.ModuleCommand;
import java.util.ArrayList;
import java.util.List;

public class ModuleCommandFactory {
    public static Iterable<Command> createCommands(ComoClient client) {
        List<Command> commands = new ArrayList<>();
        for (Module module : client.getModules()) {
            commands.add(new ModuleCommand(module));
        }
        return commands;
    }

    public static Iterable<Command> createCommands() {
        return createCommands(ComoClient.getInstance());
    }
}
