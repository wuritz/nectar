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

    /**
     * The rendering process of the Component
     *
     * @param context DrawContext
     * @param mouseX Mouse X pos
     * @param mouseY Mouse Y pos
     */
    protected abstract void render(DrawContext context, int mouseX, int mouseY);

    /**
     * The mouse clicking process of a Component
     *
     * @param mouseX Mouse X pos
     * @param mouseY Mouse Y pos
     * @param mouseButton Left 0 - Right 1
     * @return Boolean
     */
    protected abstract boolean mouseClicked(double mouseX, double mouseY, int mouseButton);

    /**
     * Hovering detection using the comparison of coordinates
     *
     * @param mouseX Mouse X pos
     * @param mouseY Mouse Y pos
     * @return Boolean
     */
    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }
}
