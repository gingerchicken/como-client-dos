package net.como.client.mixin;

import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.ComoClient;
import net.como.client.event.impl.PlayerTickEvent;
import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(at = @At("RETURN"), method="tick", cancellable = false)
    private void onTick(CallbackInfo info) {
        ComoClient.getInstance().getEventEmitter().emitEvent(new PlayerTickEvent(info));
    }
}
