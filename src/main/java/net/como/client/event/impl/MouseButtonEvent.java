package net.como.client.event.impl;

import net.como.client.event.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class MouseButtonEvent extends Event {
    public final long window;
    public final int button;
    public final int action;

    public final int mods;
    public final CallbackInfo info;

    public MouseButtonEvent(long window, int button, int action, int mods, CallbackInfo info) {
        this.window = window;
        this.button = button;
        this.action = action;
        this.mods = mods;
        this.info = info;
    }
}
