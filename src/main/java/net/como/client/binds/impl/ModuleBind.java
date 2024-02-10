package net.como.client.binds.impl;

import net.como.client.binds.Bind;
import net.como.client.module.Module;

public class ModuleBind implements Bind {
    private final Module module;

    public ModuleBind(Module module) {
        this.module = module;
    }

    @Override
    public void fire() {
        module.toggle();
    }
}
