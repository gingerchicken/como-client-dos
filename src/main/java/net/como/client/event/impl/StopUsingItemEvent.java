package net.como.client.event.impl;

import net.como.client.event.Event;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class StopUsingItemEvent extends Event {
    public final PlayerEntity player;
    public final CallbackInfo info;

    public StopUsingItemEvent(PlayerEntity player, CallbackInfo info) {
        this.player = player;
        this.info = info;
    }
}
