package net.como.client.mixin.botch.shulkerdupe;

import net.como.client.ComoClient;
import net.como.client.module.exploit.ShulkerDupe;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    // The shulker dupe needs to be tick perfect hence we are directly hooking it.
    @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/packet/Packet;)V", cancellable = true)
    private void onSendPacket(Packet<?> packet, CallbackInfo info) {
        ShulkerDupe shulkerDupe = (ShulkerDupe)(ComoClient.getInstance().getModuleByClass(ShulkerDupe.class));

        if (shulkerDupe.isEnabled()) shulkerDupe.handlePacket(packet);
    }
}
