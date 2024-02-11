package net.como.client.event.impl;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.event.Event;

public class SendChatMessageEvent extends Event {
    public final CallbackInfo info;
    public final String message;

    public SendChatMessageEvent(String message, CallbackInfo info) {
        this.message = message;
        this.info = info;
    }
}
