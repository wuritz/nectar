package dev.nectar.ui.screens.clickgui;

import net.minecraft.client.gui.DrawContext;

public abstract class Component {

    public final int x, y, width, height;

    public Component(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected abstract void render(DrawContext context, int mouseX, int mouseY);
    protected boolean mouseClicked(double mouseX, double mouseY, int mouseButton) { return false; }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }
}
