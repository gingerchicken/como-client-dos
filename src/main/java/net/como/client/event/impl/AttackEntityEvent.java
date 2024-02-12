package net.como.client.event.impl;

import net.como.client.event.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class AttackEntityEvent extends Event {
    public final PlayerEntity player;
    public final Entity target;
    public final CallbackInfo info;

    public AttackEntityEvent(PlayerEntity player, Entity target, CallbackInfo info) {
        this.player = player;
        this.target = target;
        this.info = info;
    }
}
