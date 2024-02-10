package net.como.client.event.impl;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.event.Event;

public class KeyEvent extends Event {
    public final long window;
    public final int key;
    public final int scancode;
    public final int action;
    public final int modifiers;
    public final CallbackInfo info;

    public KeyEvent(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {
        this.window = window;
        this.key = key;
        this.scancode = scancode;
        this.action = action;
        this.modifiers = modifiers;
        this.info = info;
    }
}
