package dev.nectar.ui.window.windows;

import dev.nectar.modules.Module;
import dev.nectar.ui.screens.clickgui.SettingsRenderer;
import dev.nectar.ui.window.Window;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

import static dev.nectar.Nectar.mc;

public class ModuleWindow extends Window {

    public final Module module;
    private final SettingsRenderer settingsRenderer;

    public ModuleWindow(int x, int y, int width, int height, Module module) {
        super(x, y, width, height, module.getDisplayName(), true);

        this.module = module;
        this.settingsRenderer = new SettingsRenderer(module, this);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        if (!isOpened()) return;
        super.render(context, mouseX, mouseY);

        renderInfo(context);

        settingsRenderer.updatePos(offsetX, offsetY);
        settingsRenderer.renderSettings(context, mouseX, mouseY);
    }

    private void renderInfo(DrawContext context) {
        String moduleDesc = module.getDescription();

        context.drawTextWithShadow(mc.textRenderer, moduleDesc, offsetX, offsetY + 10, Color.WHITE.getRGB());

        offsetY += 30;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (!isOpened()) return false;
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (isHovered(mouseX, mouseY)) settingsRenderer.mouseClicked(mouseX, mouseY, mouseButton);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        if (!isOpened()) return false;
        super.mouseReleased(mouseX, mouseY, mouseButton);

        settingsRenderer.mouseReleased(mouseX, mouseY, mouseButton);
        return true;
    }

}
