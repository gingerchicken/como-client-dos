package net.como.client.mixin.botch.shulkerdupe;

import net.como.client.ComoClient;
import net.como.client.module.exploit.ShulkerDupe;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBoxScreen.class)
public class ShulkerScreenMixin extends Screen {
    ButtonWidget buttonWidget = null;

    protected ShulkerScreenMixin(Text title) {
        super(title);
    }

    private ShulkerDupe getShulkerDupe() {
        return (ShulkerDupe)(ComoClient.getInstance().getModuleByClass(ShulkerDupe.class));
    }

    // TODO this is extremely hacky and I don't like it.
    // It didn't like init sooo
    private void renderDupeButton() {
        if (this.buttonWidget != null) this.remove(buttonWidget);;

        Text buttonText = this.getShulkerDupe().getDupeButtonText();

        double padding = 5;
        double width = this.textRenderer.getWidth(buttonText) + padding*2;
        double height = 20;

        double x = ComoClient.getInstance().getClient().getWindow().getScaledWidth() / 2 - width/2;
        double y = ((this.height - 166) / 2) - height - padding;

        this.buttonWidget = this.addDrawableChild(
                new ButtonWidget.Builder(Text.of("Dupe"), (button) -> {
                    this.getShulkerDupe().performDupe = true;
                })
                        .position((int)x, (int)y)
                        .size((int)width, (int)height)
                        .build()
        );

        this.buttonWidget.active = getShulkerDupe().shouldActivateButton();
    }

    @Inject(at = @At("TAIL"), method = "render")
    public void renderScreen(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ShulkerDupe shulkerDupe = this.getShulkerDupe();

        // Make sure that the dupe is enabled.
        if (!shulkerDupe.isEnabled()) return;

        this.renderDupeButton();
    }
}
