package dev.nectar.ui.components.generic;

import dev.nectar.ui.components.Component;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class HorizontalSeparator extends Component {

    public HorizontalSeparator(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        render(context);
    }

    public void render(DrawContext context) {
        context.fill(x, y, x+width, y+height, new Color(173, 173, 173, 68).getRGB());
    }
}
