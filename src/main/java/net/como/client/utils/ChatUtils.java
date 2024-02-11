package net.como.client.utils;

import net.como.client.ComoClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;

public class ChatUtils {
    public static boolean hideNextChat = false;

    public final static String BLACK        = "\u00a70";
    public final static String DARKBLUE     = "\u00a71";
    public final static String DARKGREEN    = "\u00a72";
    public final static String DARKAQUA     = "\u00a73";;
    public final static String DARKRED      = "\u00a74";
    public final static String DARKPURPLE   = "\u00a75";
    public final static String GOLD         = "\u00a76";
    public final static String GRAY         = "\u00a77";
    public final static String DARKGRAY     = "\u00a78";
    public final static String BLUE         = "\u00a79";
    public final static String GREEN        = "\u00a7a";
    public final static String AQUA         = "\u00a7b";
    public final static String RED          = "\u00a7c";
    public final static String LIGHTPURPLE  = "\u00a7d";
    public final static String YELLOW       = "\u00a7e";
    public final static String WHITE        = "\u00a7f";

    private final static String DEFAULT_PREFIX = "Como Client";

    /**
     * Log a message to the chat
     * @param message The message
     */
    public static void log(String message) {
        if (hideNextChat) {
            hideNextChat = false;

            return;
        }

        // Make sure that they are in game.
        if (!ClientUtils.isInGame()) return;

        MinecraftClient client = ComoClient.getInstance().getClient();
        ChatHud chatHud = client.inGameHud.getChatHud();

        chatHud.addMessage(Text.of(message));
    }

    /**
     * Log a message with a custom prefix
     * @param name The prefix
     * @param message The message
     */
    public static void error(String name, String message) {
        String prefix = createPrefix(name);
        String formatted = prefix + ChatUtils.RED + message;

        log(formatted);
    }

    /**
     * Log a message with the default prefix
     * @param message The message
     */
    public static void error(String message) {
        error(DEFAULT_PREFIX, message);
    }

    public static void info(String name, String message) {
        String prefix = createPrefix(name);
        String formatted = prefix + ChatUtils.WHITE + message;

        log(formatted);
    }

    public static void info(String message) {
        info(DEFAULT_PREFIX, message);
    }

    public static String createPrefix(String name) {
        return String.format("%s[%s%s%s] ", ChatUtils.WHITE, ChatUtils.GREEN, name, ChatUtils.WHITE);
    }
}
