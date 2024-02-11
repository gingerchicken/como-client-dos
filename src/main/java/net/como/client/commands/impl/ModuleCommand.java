package net.como.client.commands.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.como.client.commands.Command;
import net.como.client.module.Module;
import net.minecraft.command.CommandSource;

public class ModuleCommand extends Command {
    private final Module module;

    public ModuleCommand(Module module) {
        super(module.getName().toLowerCase(), module.getDescription());

        this.module = module;
    }

    public Module getModule() {
        return module;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            module.toggle();
            return 1;
        });
    }
}
