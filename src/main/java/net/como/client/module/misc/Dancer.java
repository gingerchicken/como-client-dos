package net.como.client.module.misc;

import net.como.client.ComoClient;
import net.como.client.event.EventHandler;
import net.como.client.event.impl.PlayerTickEvent;
import net.como.client.module.Module;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

public class Dancer extends Module {
    public Dancer() {
        this.setDescription("Swing your arms from side to side!");
    }

    final private double packetsPerSecond = 20; // TODO make this a setting
    private DancerThread dancerThread;

    private Arm originalArm;

    private SimpleOption<Arm> getArmOption() {
        return ComoClient.getInstance().getClient().options.getMainArm();
    }

    @Override
    protected void onEnable() {
        originalArm = getArmOption().getValue();
        super.onEnable();

        // Start the thread
        dancerThread = new DancerThread();
        dancerThread.start();
    }

    @Override
    protected void onDisable() {
        super.onDisable();

        dancerThread.interrupt();

        getArmOption().setValue(originalArm);
    }

    private void sendPacket() {
        // Check that your hands are empty (this is annoying on servers)
        if (!ComoClient.getInstance().me().getMainHandStack().isEmpty() || !ComoClient.getInstance().me().getOffHandStack().isEmpty()) return;

        // Get the arm
        SimpleOption<Arm> option = getArmOption();

        // Change the arm to the opposite
        option.setValue(option.getValue().getOpposite());

        // Swing the hand
        ComoClient.getInstance().me().swingHand(Hand.MAIN_HAND);
    }

    protected class DancerThread extends Thread {
        @Override
        public void run() {
            while (isEnabled()) {
                // Sleep for the amount of time it takes to send a packet
                try {
                    Thread.sleep((long) (1000 / packetsPerSecond));
                } catch (InterruptedException e) {
                    break;
                }

                // Ensure that the module is still enabled (after the sleep)
                if (!isEnabled()) break;

                // Send the packet
                sendPacket();
            }
        }
    }
}
