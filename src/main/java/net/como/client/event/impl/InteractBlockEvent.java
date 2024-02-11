package net.como.client.event.impl;

import net.como.client.event.Event;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class InteractBlockEvent extends Event {
    public final ClientPlayerEntity player;
    public final Hand hand;
    public final BlockHitResult hitResult;
    public final CallbackInfoReturnable<ActionResult> info;

    public InteractBlockEvent(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> info) {
        this.player = player;
        this.hand = hand;
        this.hitResult = hitResult;
        this.info = info;
    }
}
