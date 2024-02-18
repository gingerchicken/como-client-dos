package net.como.client.module.misc;

import net.como.client.ComoClient;
import net.como.client.event.EventHandler;
import net.como.client.event.impl.PlayerTickEvent;
import net.como.client.module.Module;
import net.minecraft.util.Hand;

public class Dancer extends Module {
    public Dancer() {
        this.setDescription("Swing your arms from side to side!");
    }

    private int i = 0;
    private boolean mainHand = true;

    @EventHandler
    public void onTick(PlayerTickEvent event) {
        // Check that your hands are empty (this is annoying on servers)
        if (!ComoClient.getInstance().me().getMainHandStack().isEmpty() || !ComoClient.getInstance().me().getOffHandStack().isEmpty()) return;

        i++;

        if (i % 4 != 0) return; // Every 4 ticks

        Hand hand = mainHand ? Hand.MAIN_HAND : Hand.OFF_HAND; // Get the hand
        mainHand = !mainHand; // Switch hands

        // Swing the arm
        ComoClient.getInstance().me().swingHand(hand);
    }
}
