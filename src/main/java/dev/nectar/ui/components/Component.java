package dev.nectar.ui.components;

import net.minecraft.client.gui.DrawContext;

public abstract class Component {

    public int x;
    public int y;
    public final int width;
    public final int height;

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
    public abstract void render(DrawContext context, int mouseX, int mouseY);

    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY)) {
            if (mouseButton == 0) return onLeftClick(mouseX, mouseY);
            if (mouseButton == 1) return onRightClick(mouseX, mouseY);
        }

        return false;
    }

    /**
     * Optional void for handling mouse release
     *
     * @param mouseX
     * @param mouseY
     * @param mouseButton
     * @return
     */
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        return false;
    }

    /**
     * Called when the component is left-clicked
     *
     * @return Boolean
     */
    public boolean onLeftClick(double mouseX, double mouseY) {
        return false;
    };

    /**
     * Called when the component is right-clicked
     *
     * @return Boolean
     */
    public boolean onRightClick(double mouseX, double mouseY) {
        return false;
    }

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
