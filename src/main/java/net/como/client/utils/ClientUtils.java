package net.como.client.utils;

import net.como.client.ComoClient;

public class ClientUtils {
    public static boolean isInGame() {
        return  ComoClient.getInstance().me() != null 
        &&      ComoClient.getInstance().getClient().getNetworkHandler() != null;
    }
}
