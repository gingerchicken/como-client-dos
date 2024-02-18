package net.como.client.module.misc;

import net.como.client.ComoClient;
import net.como.client.event.EventHandler;
import net.como.client.event.impl.PlayerTickEvent;
import net.como.client.module.Module;
import net.como.client.utils.ChatUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.world.GameMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GamemodeAlert extends Module {
    public GamemodeAlert() {
        this.setDescription("Alerts you when someone changes gamemode.");
    }

    private HashMap<UUID, GameMode> lastGamemode = new HashMap<>();

    private int ticks = 0;

    private void logChange(PlayerListEntry player, GameMode old, GameMode newMode) {
        String dName = player.getDisplayName() != null ? player.getDisplayName().getString() : player.getProfile().getName();

        this.info(String.format("%s changed from %s%s%s to %s%s%s.",
                dName,

                ChatUtils.GOLD,
                old.getName(),
                ChatUtils.WHITE,

                ChatUtils.GOLD,
                newMode.getName(),
                ChatUtils.WHITE
        ));
    }

    @EventHandler
    public void onTick(PlayerTickEvent event) {
        if (ticks++ % 2 != 0) return; // Every other tick

        // Minecraft Client
        MinecraftClient client = ComoClient.getInstance().getClient();

        // Enumerate the players
        List<UUID> seenPlayers = new ArrayList<>();
        for (PlayerListEntry playerListEntry : client.getNetworkHandler().getPlayerList()) {
            // Get their ID
            UUID id = playerListEntry.getProfile().getId();

            // Get their gamemode
            GameMode current = playerListEntry.getGameMode();

            // Make sure it isn't null
            if (current == null) continue;

            // Add them to the list
            seenPlayers.add(id);

            // Check if we have seen them before
            if (!lastGamemode.containsKey(id)) {
                // Add them and continue
                lastGamemode.put(id, current);
                continue;
            }

            // Get their last gamemode
            GameMode last = lastGamemode.get(id);

            // If it is the same, continue
            if (last == current) continue;

            // Log the change
            this.logChange(playerListEntry, last, current);

            // Update the last gamemode
            lastGamemode.put(id, current);
        }

        // Create a new list of the old keys (prevents concurrent modification)
        List<UUID> oldKeys = new ArrayList<>(lastGamemode.keySet());

        // Check for any removed players
        for (UUID oldKey : oldKeys) {
            // If we have seen them, continue
            if (seenPlayers.contains(oldKey)) continue;

            // Remove them
            lastGamemode.remove(oldKey);
        }
    }
}
