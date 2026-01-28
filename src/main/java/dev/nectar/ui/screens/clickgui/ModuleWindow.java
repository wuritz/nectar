package dev.nectar.ui.screens.clickgui;

import dev.nectar.modules.Module;
import dev.nectar.ui.components.DraggableComponent;
import dev.nectar.ui.components.generic.CloseButton;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

import static dev.nectar.Nectar.mc;

public class ModuleWindow extends DraggableComponent {

    public final Module module;
    private final SettingsRenderer settingsRenderer;

    private final CloseButton closeButton;

    private boolean opened = false;

    private int offsetX = 0;
    private int offsetY = 0;

    public ModuleWindow(int x, int y, int width, int height, Module module) {
        super(x, y, width, height);

        this.module = module;
        this.closeButton = new CloseButton(x+width-20, y, 20, 20, this);
        children.add(closeButton);

        this.settingsRenderer = new SettingsRenderer(module, this);
    }

    public void open() {
        opened = true;
    }
    public void close() {
        opened = false;
    }

    public boolean isOpened() {
        return opened;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        if (!opened) return;

        resetOffset();

        // Update the close button's position while dragging
        if (dragging) {
            closeButton.x = x+width-20;
            closeButton.y = y;
        }

        // Background
        context.fill(x, y, x+width, y+height, new Color(33, 33, 33, 255).getRGB());

        // Header
        renderHeader(context, mouseX, mouseY);

        renderInfo(context);

        settingsRenderer.updatePos(offsetX, offsetY);
        settingsRenderer.renderSettings(context, mouseX, mouseY);
    }

    private void resetOffset() {
        offsetX = x;
        offsetY = y;
    }

    private void renderHeader(DrawContext context, int mouseX, int mouseY) {
        context.fill(x, y, x+width-20, y+20, new Color(248, 208, 46, 160).getRGB());
        context.drawTextWithShadow(mc.textRenderer,
                module.getDisplayName(),
                x+width/2-(mc.textRenderer.getWidth(module.getDisplayName())/2),
                y+20/2-(mc.textRenderer.fontHeight/2),
                Color.WHITE.getRGB());

        closeButton.render(context, mouseX, mouseY);

        offsetX += 10;
        offsetY += 20;
    }

    private void renderInfo(DrawContext context) {
        String moduleDesc = module.getDescription();

        context.drawTextWithShadow(mc.textRenderer, moduleDesc, offsetX, offsetY + 10, Color.WHITE.getRGB());

        offsetY += 30;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (isHovered(mouseX, mouseY)) settingsRenderer.mouseClicked(mouseX, mouseY, mouseButton);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);

        settingsRenderer.mouseReleased(mouseX, mouseY, mouseButton);
        return true;
    }

    @Override
    protected void updatePosAgain() {
        closeButton.x = x+width-20;
        closeButton.y = y;
    }

}
