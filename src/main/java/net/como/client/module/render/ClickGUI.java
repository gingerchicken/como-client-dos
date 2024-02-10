package net.como.client.module.render;

import net.como.client.ComoClient;
import net.como.client.binds.impl.ModuleBind;
import net.como.client.event.EventHandler;
import net.como.client.event.impl.KeyEvent;
import net.como.client.event.impl.PlayerTickEvent;
import net.como.client.event.impl.TitleScreenRenderEvent;
import net.como.client.gui.impl.ClickGUIScreen;
import net.como.client.module.Module;
import net.como.client.utils.ImGuiUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyCodes;
import net.minecraft.client.input.KeyboardInput;

public class ClickGUI extends Module {
    ClickGUIScreen clickGUIScreen;

    public ClickGUI() {
        this.setDescription("The ClickGUI module");

        // TODO perhaps move this?
        ComoClient.getInstance().getBindings().appendBind(344, new ModuleBind(this));
    }

    @Override
    protected void onEnable() {
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        clickGUIScreen = null;
    }

    @EventHandler
    public void showScreen(KeyEvent event) {
        if (clickGUIScreen == null) {
            clickGUIScreen = new ClickGUIScreen();
        }

        ComoClient.getInstance().getClient().setScreen(clickGUIScreen);
    }
}
