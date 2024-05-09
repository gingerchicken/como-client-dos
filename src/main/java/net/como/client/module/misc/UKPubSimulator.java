package net.como.client.module.misc;

import net.como.client.ComoClient;
import net.como.client.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class UKPubSimulator extends Module {
    public UKPubSimulator() {
        this.setDescription("Simulate going to a pub in the UK when there's some crap football match on");
    }

    @Override
    public String getName() {
        return "UK Pub Simulator";
    }

    private double packetsPerSecond = 144; // TODO make this a setting
    private UKPubSimulatorThread thread;

    @Override
    protected void onEnable() {
        super.onEnable();
        thread = new UKPubSimulatorThread();
        thread.start();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        if (thread != null)
            thread.interrupt();
    }

    private boolean entityCheck(Entity entity) {
        return true;
    }

    private void perform() {
        MinecraftClient client = ComoClient.getInstance().getClient();
        if (client == null) return; // Null check (shouldn't happen)

        // Get the crosshair target
        HitResult crosshairTarget = client.crosshairTarget;
        if (!(crosshairTarget instanceof EntityHitResult entityHitResult)) return;

        // Get the entity
        Entity entity = entityHitResult.getEntity();
        if (!entityCheck(entity)) return;

        // Hit 'n' swing
        client.interactionManager.attackEntity(client.player, entity);
        client.player.swingHand(Hand.MAIN_HAND);
    }

    protected class UKPubSimulatorThread extends Thread {
        @Override
        public void run() {
            while (isEnabled()) {
                perform();
                try {
                    Thread.sleep((long) (1000 / packetsPerSecond));
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
