package net.como.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.ComoClient;
import net.como.client.event.impl.TitleScreenRenderEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

@Mixin(TitleScreen.class)
public abstract class TitleScreenLogoMixin extends Screen {
    protected TitleScreenLogoMixin(Text title) {
        super(title);
    }
    
    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo info) {
        ComoClient.getInstance().getEventEmitter().emitEvent(new TitleScreenRenderEvent(context, mouseX, mouseY, delta, info));
    }
}
