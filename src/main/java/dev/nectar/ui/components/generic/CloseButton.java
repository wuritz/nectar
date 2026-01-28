package dev.nectar.ui.components.generic;

import dev.nectar.ui.components.Component;
import dev.nectar.ui.components.DraggableComponent;
import dev.nectar.ui.screens.clickgui.settings.ModuleWindow;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

import static dev.nectar.Nectar.mc;

public class CloseButton extends Component {

    private final DraggableComponent parentComponent;

    public CloseButton(int x, int y, int width, int height, DraggableComponent parentComponent) {
        super(x, y, width, height);

        this.parentComponent = parentComponent;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        if (isHovered(mouseX, mouseY)) context.fill(x, y, x+width, y+height, new Color(255, 0, 0, 255).getRGB());
        else context.fill(x, y, x+width, y+height, new Color(255, 0, 0, 180).getRGB());

        context.drawTextWithShadow(mc.textRenderer, "X",
                x+width/2-(mc.textRenderer.getWidth("X")/2),
                y+height/2-(mc.textRenderer.fontHeight/2),
                Color.WHITE.getRGB()
        );
    }

    @Override
    public boolean onLeftClick(double mouseX, double mouseY) {
        if (parentComponent instanceof ModuleWindow moduleWindow) moduleWindow.close();
        return true;
    }
}
