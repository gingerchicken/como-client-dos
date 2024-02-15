package net.como.client.mixin.botch.lecterncrash;

import net.como.client.ComoClient;
import net.como.client.module.exploit.LecternCrash;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.LecternScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LecternScreen.class)
public abstract class LecternScreenMixin extends Screen {
    protected LecternScreenMixin(Text title) {
        super(title);
    }

    private LecternCrash getLecternCrash() {
        return (LecternCrash)(ComoClient.getInstance().getModuleByClass(LecternCrash.class));
    }

    @Inject(at = {@At("TAIL")}, method = {"init"})
    public void init(CallbackInfo ci) {
        LecternCrash lecternCrash = this.getLecternCrash();
        if (!lecternCrash.isEnabled()) return;

        int buttonWidth = 98;
        int buttonHeight = 20;

        addDrawableChild(
            new ButtonWidget.Builder(Text.of("Crash"), button -> {
                lecternCrash.perform();

                this.close();
                button.active = false;
            })
            .position(this.width / 2 - buttonWidth / 2, 196 + buttonHeight + 4)
            .size(buttonWidth, buttonHeight)
            .build()
        );
    }
}
