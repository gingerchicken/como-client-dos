package net.como.client.interfaces;

import net.minecraft.client.network.PendingUpdateManager;

public interface IClientWorld {
    public PendingUpdateManager obtainPendingUpdateManager();
}
