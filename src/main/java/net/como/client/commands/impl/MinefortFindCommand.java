package net.como.client.commands.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.como.client.commands.Command;
import net.como.client.utils.APIWrapper;
import net.como.client.utils.ChatUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.util.UUID;

public class MinefortFindCommand extends Command {
    private static final SimpleCommandExceptionType PLAYER_NOT_FOUND = new SimpleCommandExceptionType(Text.of("Player not found"));

    public MinefortFindCommand() {
        super("minefortfind", "Find a player in Minefort", "mf-find");
    }

    private void perform(String username) {
        // Get the UUID
        UUID uuid;
        try {
            uuid = APIWrapper.getPlayerUUID(username);
        } catch (Exception e) {
            ChatUtils.error("Player not found");
            return;
        }

        // Get the server list
        JsonArray servers = APIWrapper.getMinefortServerList(500);

        for (JsonElement jsonElement : servers) {
            for (JsonElement p : jsonElement.getAsJsonObject().getAsJsonObject("players").getAsJsonArray("list")) {
                if (!p.getAsJsonObject().get("uuid").getAsString().equals(uuid.toString())) {
                    continue;
                }

                // Get the server name
                String serverName = jsonElement.getAsJsonObject().get("serverName").getAsString();

                ChatUtils.info("Found " + username + " on " + serverName);
                return;
            }
        }

        ChatUtils.error("Unable to find player " + username + " on any minefort server.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("player", StringArgumentType.word()).executes(context -> {
            String username = StringArgumentType.getString(context, "player");
            Thread t = new Thread(() -> perform(username));
            t.start();
            return 1;
        }));
    }
}
