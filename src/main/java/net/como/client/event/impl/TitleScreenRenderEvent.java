package net.como.client.event.impl;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.event.Event;
import net.minecraft.client.gui.DrawContext;

public class TitleScreenRenderEvent extends Event {
    public final DrawContext context;
    public final int mouseX;
    public final int mouseY;
    public final float delta;
    public final CallbackInfo info;

    public TitleScreenRenderEvent(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo info) {
        this.context = context;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.delta = delta;
        this.info = info;
    }
}
