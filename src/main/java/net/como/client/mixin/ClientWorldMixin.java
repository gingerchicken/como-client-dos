package net.como.client.mixin;

import net.como.client.interfaces.IClientWorld;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientWorld.class)
public class ClientWorldMixin implements IClientWorld {
    @Shadow private PendingUpdateManager pendingUpdateManager;
    @Override
    public PendingUpdateManager obtainPendingUpdateManager() {
        return this.pendingUpdateManager;
    }
}
