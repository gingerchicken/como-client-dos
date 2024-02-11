package net.como.client.event.impl;

import net.como.client.event.Event;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class SendPacketEvent extends Event {
    public final Packet<?> packet;
    public final CallbackInfo info;

    public SendPacketEvent(Packet<?> packet, CallbackInfo info) {
        this.packet = packet;
        this.info = info;
    }
}
