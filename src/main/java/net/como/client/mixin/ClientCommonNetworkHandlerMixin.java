package net.como.client.mixin;

import net.como.client.ComoClient;
import net.como.client.event.impl.ResourcePackSendEvent;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonNetworkHandler.class)
public class ClientCommonNetworkHandlerMixin {
    @Inject(at = @At("HEAD"), method="onResourcePackSend", cancellable = true)
    private void onResourcePackSend(ResourcePackSendS2CPacket packet, CallbackInfo ci) {
        ComoClient.getInstance().getEventEmitter().emitEvent(
                new ResourcePackSendEvent(packet, ci)
        );
    }
}
