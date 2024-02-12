package net.como.client.utils;

import net.como.client.ComoClient;
import net.como.client.interfaces.IClientWorld;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.world.ClientWorld;

public class ClientUtils {
    public static boolean isInGame() {
        return  ComoClient.getInstance().me() != null 
        &&      ComoClient.getInstance().getClient().getNetworkHandler() != null;
    }

    /**
     * Gets the PendingUpdateManager
     * @param world The world to get the manager from
     * @return The PendingUpdateManager
     */
    public static PendingUpdateManager getUpdateManager(ClientWorld world) {
        IClientWorld iWorld = (IClientWorld)world;

        return iWorld.obtainPendingUpdateManager();
    }

    /**
     * Opens, increments and closes the PendingUpdateManager for a given world
     * @param world The world to open the manager for
     * @return The new sequence number
     */
    public static int incrementPendingUpdateManager(ClientWorld world) {
        PendingUpdateManager manager = getUpdateManager(world);

        int current = manager.getSequence();
        manager.close();

        return current;
    }
}
