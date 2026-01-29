package dev.nectar.ui.window;

import dev.nectar.ui.components.DraggableComponent;
import dev.nectar.ui.components.generic.CloseButton;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

import static dev.nectar.Nectar.mc;

public abstract class Window extends DraggableComponent {

    private final CloseButton closeButton;
    private final boolean canClose;
    private final String title;

    private boolean opened;
    protected int offsetX, offsetY;

    public Window(int x, int y, int width, int height, String title, boolean canClose) {
        super(x, y, width, height);

        this.canClose = canClose;
        this.title = title;

        this.offsetX = x;
        this.offsetY = y;

        this.opened = false;

        if (canClose) {
            this.closeButton = new CloseButton(x+width-20, y, 20, 20, this);
            components.add(closeButton);
        } else this.closeButton = null;
    }

    public void open() {
        resetOffsets();
        opened = true;
    }

    public void close() {
        opened = false;
        resetOffsets();
    }

    public boolean isOpened() { return opened; }

    private void resetOffsets() {
        offsetX = x;
        offsetY = y;
    }

    /**
     * The rendering process of the Component
     * Don't forget to use .super!
     *
     * @param context DrawContext
     * @param mouseX Mouse X pos
     * @param mouseY Mouse Y pos
     */
    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        if (!opened) return;

        resetOffsets();

        if (dragging && canClose) {
            closeButton.x = x+width-20;
            closeButton.y = y;
        }

        // Background
        context.fill(x, y, x+width, y+height, new Color(33, 33, 33, 255).getRGB());

        // Header
        renderHeader(context, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (!opened) return false;
        super.mouseClicked(mouseX, mouseY, mouseButton);

        return true;
    }

    private void renderHeader(DrawContext context, int mouseX, int mouseY) {
        int headerWidth = canClose ? x+width-20 : x+width;

        if (isHoveredHeader(mouseX, mouseY)) context.fill(x, y, headerWidth, y+20, new Color(255, 218, 78, 199).getRGB());
        else context.fill(x, y, headerWidth, y+20, new Color(248, 208, 46, 160).getRGB());

        context.drawTextWithShadow(mc.textRenderer,
                title,
                x+width/2-(mc.textRenderer.getWidth(title)/2),
                y+20/2-(mc.textRenderer.fontHeight/2),
                Color.WHITE.getRGB());

        if (canClose) closeButton.render(context, mouseX, mouseY);

        offsetX += 10;
        offsetY += 20;
    }

    @Override
    protected void updatePosAgain() {
        if (!canClose) return;

        closeButton.x = x+width-20;
        closeButton.y = y;
    }

    public boolean isHoveredHeader(double mouseX, double mouseY) {
        int headerWidth = canClose ? x+width-20 : x+width;
        return mouseX > x && mouseX < x + headerWidth && mouseY > y && mouseY < y + 20;
    }

}
