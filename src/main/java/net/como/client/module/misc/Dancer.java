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

    private Arm originalArm;

    private SimpleOption<Arm> getArmOption() {
        return ComoClient.getInstance().getClient().options.getMainArm();
    }

    @Override
    protected void onEnable() {
        originalArm = getArmOption().getValue();
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        getArmOption().setValue(originalArm);
    }

    @EventHandler
    public void onTick(PlayerTickEvent event) {
        // Check that your hands are empty (this is annoying on servers)
        if (!ComoClient.getInstance().me().getMainHandStack().isEmpty() || !ComoClient.getInstance().me().getOffHandStack().isEmpty()) return;

        // There you go you smelly little sausage
        SimpleOption<Arm> option = getArmOption();
        option.setValue(option.getValue().getOpposite());
        ComoClient.getInstance().me().swingHand(Hand.MAIN_HAND);
    }
}
