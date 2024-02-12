package net.como.client.event.impl;

import net.como.client.event.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class InteractEntityAtLocationEvent extends Event {
//        public void interactEntityAtLocation(PlayerEntity player, Entity target, EntityHitResult hitResult, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
    public final PlayerEntity player;
    public final Entity target;
    public final EntityHitResult hitResult;
    public final Hand hand;
    public final CallbackInfoReturnable<ActionResult> info;

    public InteractEntityAtLocationEvent(PlayerEntity player, Entity target, EntityHitResult hitResult, Hand hand, CallbackInfoReturnable<ActionResult> info) {
        this.player = player;
        this.target = target;
        this.hitResult = hitResult;
        this.hand = hand;
        this.info = info;
    }
}
