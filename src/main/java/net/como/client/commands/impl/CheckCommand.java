package net.como.client.commands.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.como.client.commands.Command;
import net.como.client.utils.ChatUtils;
import net.minecraft.command.CommandSource;

public class CheckCommand extends Command {
    public CheckCommand() {
        super("check", "Checks that the client is present.", "test");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            ChatUtils.info("Yup, it works!");
            return 1;
        });
    }
}
