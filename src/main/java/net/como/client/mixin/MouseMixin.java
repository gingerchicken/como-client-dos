package net.como.client.mixin;

import net.como.client.ComoClient;
import net.como.client.event.impl.MouseButtonEvent;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(at = @At("HEAD"), method="onMouseButton", cancellable = true)
    private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        // ComoClient.getInstance().emitter.triggerEvent(new OnMouseButtonEvent(window, button, action, mods, ci));
        ComoClient.getInstance().getEventEmitter().emitEvent(
                new MouseButtonEvent(window, button, action, mods, ci)
        );
    }
}
