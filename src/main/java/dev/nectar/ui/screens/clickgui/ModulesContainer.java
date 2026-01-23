package dev.nectar.ui.screens.clickgui;

import dev.nectar.core.Color;
import dev.nectar.modules.Module;
import net.minecraft.client.gui.DrawContext;

public class ModulesContainer {

    public final int x, y, width, height;
    public Module.Category currentCategory = null;

    public ModulesContainer(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render(DrawContext context) {
        context.fill(x, y, x + width, y + height, Color.DARK_GRAY.getPacked());
    }
}
