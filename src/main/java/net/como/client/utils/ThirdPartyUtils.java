package net.como.client.utils;

import net.fabricmc.loader.api.FabricLoader;

public class ThirdPartyUtils {
    /**
     * Check if a mod is loaded by its mod id
     * @param mod The mod id
     * @return Whether the mod is loaded
     */
    public static boolean isModLoaded(String mod) {
        return FabricLoader.getInstance().isModLoaded(mod);
    }

    /**
     * Check if the Meteor Client is loaded
     * @return Whether the Meteor Client is loaded
     */
    public static boolean isMeteorLoaded() {
        return isModLoaded("meteor-client");
    }

    /**
     * Check if Coffee Client is loaded
     * @return Whether Coffee Client is loaded
     */
    public static boolean isCoffeeLoaded() {
        return isModLoaded("coffee");
    }
}
