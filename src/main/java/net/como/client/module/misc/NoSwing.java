package net.como.client.module.misc;

import net.como.client.event.EventHandler;
import net.como.client.event.impl.SendPacketEvent;
import net.como.client.module.Module;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;

public class NoSwing extends Module {
    public NoSwing() {
        this.setDescription("Stops the client from swinging their arm.");
    }

    @EventHandler
    public void onPacket(SendPacketEvent event) {
        Packet<?> packet = event.packet;
        if (!(packet instanceof HandSwingC2SPacket)) return;

        event.info.cancel();
    }
}
