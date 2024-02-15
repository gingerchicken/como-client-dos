package net.como.client.event.impl;

import net.como.client.event.Event;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ResourcePackSendEvent extends Event {
    public final ResourcePackSendS2CPacket packet;
    public final CallbackInfo info;

    public ResourcePackSendEvent(ResourcePackSendS2CPacket packet, CallbackInfo info) {
        this.packet = packet;
        this.info = info;
    }
}
