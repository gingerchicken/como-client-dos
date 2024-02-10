package net.como.client.event.impl;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.event.Event;

public class PlayerTickEvent extends Event {
    public final CallbackInfo info;
    
    public PlayerTickEvent(CallbackInfo info) {
        this.info = info;
    }
}
