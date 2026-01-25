package dev.nectar.ui.components;

import net.minecraft.client.gui.DrawContext;

public class DraggableComponent extends Component {

    private int dragX, dragY;
    private boolean dragging = false;

    public DraggableComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * The rendering process of the Component
     *
     * @param context DrawContext
     * @param mouseX  Mouse X pos
     * @param mouseY  Mouse Y pos
     */
    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {

    }

    /**
     * Called when the component is clicked
     *
     * @return Boolean
     */
    @Override
    public boolean onLeftClick() {
        return false;
    }

    public void updatePos(double mouseX, double mouseY) {
        if (dragging) {
            x = (int) (mouseX - dragX);
            y = (int) (mouseY - dragY);
        }
    }
}
