package net.como.client.module.render;

import net.como.client.ComoClient;
import net.como.client.event.EventHandler;
import net.como.client.event.impl.PlayerTickEvent;
import net.como.client.event.impl.ResourcePackSendEvent;
import net.como.client.module.Module;
import net.como.client.utils.ChatUtils;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;

import java.util.UUID;

public class AntiResoursePack extends Module {
    public AntiResoursePack() {
        this.setDescription("Disables the server's resource pack.");
        this.setCategory("Render");
    }

    private ResourcePackSendS2CPacket lastPacket;

    private void logDecline() {
        // Generate a message
        String message = lastPacket == null
                ? "Blocked server resource pack!"
                : String.format("Blocked resource pack from %s (SHA1: %s%s%s)", lastPacket.url(), ChatUtils.GREEN, lastPacket.hash(), ChatUtils.WHITE);

        // Send the message
        info(message);
    }

    private void sendAccept() {
        ClientPlayNetworkHandler networkHandler = ComoClient.getInstance().me().networkHandler;

        // "Accept it"
        networkHandler.sendPacket(new ResourcePackStatusC2SPacket(lastPacket.id(), ResourcePackStatusC2SPacket.Status.ACCEPTED));

        // "It has downloaded"
        networkHandler.sendPacket(new ResourcePackStatusC2SPacket(lastPacket.id(), ResourcePackStatusC2SPacket.Status.SUCCESSFULLY_LOADED));
    }

    @EventHandler
    public void onResourcePackSend(ResourcePackSendEvent event) {
        lastPacket = event.packet;
        event.info.cancel();
    }

    @EventHandler
    public void onTick(PlayerTickEvent event) {
        if (lastPacket == null) return; // Wait a tick
        sendAccept(); // Accept the packet
        logDecline(); // Log the decline
        lastPacket = null; // Reset
    }
}
