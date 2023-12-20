package net.como.client.module.render;

import net.como.client.event.EventHandler;
import net.como.client.event.impl.TitleScreenRenderEvent;
import net.como.client.module.Module;
import net.como.client.utils.ThirdPartyUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;

public class Greeter extends Module {
    @EventHandler
    public void renderTitleWatermark(TitleScreenRenderEvent event) {
        MinecraftClient client = MinecraftClient.getInstance();

        TextRenderer tr = client.textRenderer;
        String text = "Thank you for using Como Client!";

        int padding = 2;
        int x = client.getWindow().getScaledWidth() - tr.getWidth(text) - padding;
        int y = ThirdPartyUtils.isMeteorLoaded() ? 12 + padding : padding;

        event.context.drawTextWithShadow(client.textRenderer, text, x, y, 0xFF00FF00);
    }
}
