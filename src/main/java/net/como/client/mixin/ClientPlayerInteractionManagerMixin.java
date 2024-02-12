package net.como.client.mixin;

import net.como.client.ComoClient;
import net.como.client.event.impl.AttackEntityEvent;
import net.como.client.event.impl.InteractBlockEvent;
import net.como.client.event.impl.InteractEntityAtLocationEvent;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    public void interactBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        ComoClient.getInstance().getEventEmitter().emitEvent(
                new InteractBlockEvent(player, hand, hitResult, cir)
        );
    }

    @Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
    public void attackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        ComoClient.getInstance().getEventEmitter().emitEvent(
            new AttackEntityEvent(player, target, ci)
        );
    }

    @Inject(method = "interactEntityAtLocation", at = @At(value = "HEAD"), cancellable = true)
    public void interactEntityAtLocation(PlayerEntity player, Entity target, EntityHitResult hitResult, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ComoClient.getInstance().getEventEmitter().emitEvent(
                new InteractEntityAtLocationEvent(player, target, hitResult, hand, cir)
        );
    }
}
