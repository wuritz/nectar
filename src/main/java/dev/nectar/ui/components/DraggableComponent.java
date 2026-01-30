package dev.nectar.ui.components;

import dev.nectar.ui.components.generic.CloseButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class DraggableComponent extends Component {

    private int dragX, dragY;
    protected boolean dragging;
    protected List<Component> components = new ArrayList<>();

    public DraggableComponent(int x, int y, int width, int height) {
        super(x, y, width, height);

        this.dragging = false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY)) {
            if ((mouseY <= y+20 && mouseY >= y) && mouseButton == 0) {

                // Needed to eliminate a bug
                AtomicBoolean cont = new AtomicBoolean(true);
                components.forEach(child -> {
                    if (child.isHovered(mouseX, mouseY) && child instanceof CloseButton closeButton) {
                        closeButton.onLeftClick(mouseX, mouseY);
                        cont.set(false);
                    }
                });
                if (!cont.get()) return false;

                dragging = true;

                dragX = (int) (mouseX - x);
                dragY = (int) (mouseY - y);
            }
        }
        return false;
    }

    /**
     * Eliminates a bug
     */
    protected abstract void updatePosAgain();

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        if (mouseButton == 0 && dragging) {
            dragging = false;
            updatePosAgain();
        }

        return false;
    }

    public void updatePos(double mouseX, double mouseY) {
        if (dragging) {
            x = (int) (mouseX - dragX);
            y = (int) (mouseY - dragY);
        }
    }

}
