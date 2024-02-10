package net.como.client.gui.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import net.como.client.ComoClient;
import net.como.client.gui.ImGuiScreen;
import net.como.client.module.Module;
import net.como.client.module.render.ClickGUI;
import net.como.client.utils.ImGuiUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class ClickGUIScreen extends ImGuiScreen {
    // Search category tones
    private float emptyBgTone = 1f;
    private final static float EMPTY_BG_TONE_SPEED = -0.05f;
    private final static float EMPTY_BG_TONE_MIN = 0.5f;

    private float backgroundDarkness = 1.0f;
    private final static float BACKGROUND_DARKNESS_SPEED = -0.10f;
    private final static float BACKGROUND_DARKNESS_MIN = 0f;

    private HashMap<String, List<Module>> categories = new HashMap<>();

    private static HashMap<Module, Boolean> openedOptions = new HashMap<>();
    
    /**
     * The current search phrase
    */
    private static String searchPhrase = "";

    /**
     * Reset the window position next time it's opened
     */
    public static boolean resetNext = false;

    @Override
    protected void init() {
        super.init();
        // ImGuiUtils.refreshStyle();

        // Clear the categories
        categories.clear();

        // Add the modules to the categories
        for (Module mod : ComoClient.getInstance().getModules()) {
            // Get the category
            String cat = mod.getCategory();

            // If the category doesn't exist, create it
            categories.putIfAbsent(cat, new ArrayList<>());

            // Add the module to the category
            categories.get(cat).add(mod);
        }
    }

    @Override
    protected void renderImGui(float tickDelta) {
        this.renderModules(tickDelta);
    }

    private float getScale() {
        return 1.0f; // TODO Add a setting
    }

    /**
     * Gets the next value for a given value, speed and min
     * @param curr the current value
     * @param speed the speed to change the value by
     * @param min the minimum value
     * @param max the maximum value
     * @return a clamped next value
     */
    private float getNext(float curr, float speed, float min, float max) {
        return Math.max(min, Math.min(max, curr + speed));
    }

    private float lerpValue(float current, float next, float tickDelta) {
        return current + (next - current) * tickDelta;
    }

    private void renderModules(float tickDelta) {
        // Default padding
        final float xPadding = 15 * this.getScale();
        final float yPadding = 15 * this.getScale();
        final float yOffset = 80 * this.getScale();

        // For default positioning
        float nextXPos = xPadding;

        // For default width/height
        final float defaultWidth = 175f * this.getScale();

        // Store previous heights
        List<Float> prevHeights = new ArrayList<>();
        int heightPtr = 0;

        float emptyBgTone = this.emptyBgTone;

        // Get next empty background tone
        float next = this.getNext(emptyBgTone, EMPTY_BG_TONE_SPEED, EMPTY_BG_TONE_MIN, 1.0f);

        // Lerp empty background tone
        emptyBgTone = this.lerpValue(this.emptyBgTone, next, tickDelta);

        for (String cat : categories.keySet()) {
            // Get the list of modules for the category as a copy
            List<Module> modules = new ArrayList<>(categories.get(cat));

            // Remove all of the modules from the list that don't contain the search phrase
            // if (searchPhrase != null && !searchPhrase.isEmpty() && !resetNext) {
            //     modules.removeIf(mod -> !mod.getName().toLowerCase().contains(searchPhrase.toLowerCase()));
            // }

            // Sort the list alphabetically
            modules.sort((a, b) -> a.getName().compareTo(b.getName()));

            // Check the category is not empty
            if (modules.isEmpty()) {
                // Change the background darkness to make it obvious that the category is empty
                ImGui.setNextWindowBgAlpha(emptyBgTone);
            }

            // First ever positioning
            float yPos = prevHeights.size() < heightPtr + 1 ? yPadding + yOffset : prevHeights.get(heightPtr) + yPadding;

            float currentHeight = (modules.size() + 1) * 30;

            // Set first ever position
            ImGui.setNextWindowSize(defaultWidth, currentHeight, getSaveCondition());
            ImGui.setNextWindowPos(nextXPos, yPos, getSaveCondition());
            
            // Set the window as expanded
            ImGui.setNextWindowCollapsed(false, getSaveCondition());

            // Calculate next positions
            nextXPos += defaultWidth + xPadding;

            // Add the height if the length is less than current pointer
            if (prevHeights.size() < heightPtr + 1) {
                prevHeights.add(yPos + currentHeight);
            } else {
                prevHeights.set(heightPtr, yPos + currentHeight);
            }

            // Increment height pointer
            heightPtr++;

            // Wrap them in case they're off the screen
            if (nextXPos + defaultWidth + xPadding > ComoClient.getInstance().getClient().getWindow().getWidth()) {
                nextXPos = xPadding;
                heightPtr = 0;
            }

            // Show collapse button
            ImGui.begin(cat);

            for (Module mod : modules) {
                this.renderModule(mod);
            }

            ImGui.end();

            // Reset the mouse count
            ImGui.resetMouseDragDelta();
        }
    }

    /**
     * Render a given module
     * @param mod the module to render
     */
    private void renderModule(Module mod) {
        // Push a new id
        ImGui.pushID(mod.getName());

        // Push style variable
        ImGui.pushStyleVar(ImGuiStyleVar.ButtonTextAlign, 0.0f, 0.5f);

        // Push enabled style
        if (mod.isEnabled()) {
            ImGui.pushStyleColor(ImGuiCol.Button, 0.10f, 0.60f, 0.10f, 0.75f);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.10f, 0.50f, 0.10f, 0.75f);
            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.30f, 0.70f, 0.30f, 0.75f);
        }

        boolean shouldToggle = ImGui.button(mod.getName(), ImGui.getWindowWidth() - ImGui.getStyle().getWindowPaddingX()*2, 0);

        // Pop enabled style
        if (mod.isEnabled()) {
            ImGui.popStyleColor(3);
        }

        // Handle if the mouse is being hovered
        if (ImGui.isItemHovered()) {
            ImGui.setTooltip(mod.getDescription() == null ? "No description, sorry :(" : mod.getDescription());

            // Handle right clicks
            if (ImGui.isMouseClicked(1)) {
                // this.toggleOptions(mod);
            }
        }

        // Render the module options
        // if (this.shouldShowOptions(mod)) this.renderModuleOptions(mod);

        // Pop the style variable
        ImGui.popStyleVar(1);

        // Handle outputs

        // Draw button that toggles the module
        if (shouldToggle) {
            // ChatUtils.hideNextChat = true;
            mod.toggle();
        }

        // Pop the id
        ImGui.popID();
    }

    /**
     * Get the save condition for the window, if it should be saved or not to the ImGui.ini file
     * @return ImGuiCondition for the window
     */
    private static int getSaveCondition() {
        return !resetNext ? ImGuiCond.FirstUseEver : ImGuiCond.Always;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    ClickGUI getClickGUI() {
        return (ClickGUI)ComoClient.getInstance().getModuleByClass(ClickGUI.class);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getClickGUI().isEnabled()) this.close();

        // Handle reset next
        if (resetNext) {
            resetNext = false;
            
            // Clear the search
            searchPhrase = "";

            // Clear the opened settings
            openedOptions.clear();
        }

        // Update the global ImGUI scale
        ImGui.getIO().setFontGlobalScale(this.getScale());

        // Background darkness
        if (this.backgroundDarkness > BACKGROUND_DARKNESS_MIN) {
            this.backgroundDarkness += BACKGROUND_DARKNESS_SPEED;
        }

        // Category tones
        if (this.emptyBgTone > EMPTY_BG_TONE_MIN) {
            this.emptyBgTone += EMPTY_BG_TONE_SPEED;
        }
    }

    @Override
    public void close() {
        // Hide the chat output

        // Disable the module
        this.getClickGUI().setEnabled(false);

        super.close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }

    private void renderInGameBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        // Current background darkness
        float current = this.backgroundDarkness;

        float next = this.getNext(current, BACKGROUND_DARKNESS_SPEED, BACKGROUND_DARKNESS_MIN, 1.0f);

        // Get lerped background darkness
        float backgroundDarkness = this.lerpValue(current, next, delta);

        // Actual background darkness
        float d = 1 - backgroundDarkness;
        
        // Colours (ARGB)
        int startColour = (int)(d * 100);
        int endColour   = (int)(d * 150);

        // Convert it from 0 -> 255 to 0 -> 0xFF
        startColour = (startColour << 24) | 0x00FFFFFF;
        endColour   = (endColour   << 24) | 0x00FFFFFF;

        // Draw the background
        context.fillGradient(0, 0, this.width, this.height, startColour, endColour);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        renderInGameBackground(context, mouseX, mouseY, delta);
    }
}
