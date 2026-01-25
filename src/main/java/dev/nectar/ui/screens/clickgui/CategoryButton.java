package dev.nectar.ui.screens.clickgui;

import dev.nectar.modules.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.cursor.Cursor;

import java.awt.*;

import static dev.nectar.Nectar.mc;

public class CategoryButton extends Component{

    private final String categoryName;
    public final Module.Category category;

    private boolean selected = false;

    public CategoryButton(int x, int y, int width, int height, Module.Category category) {
        super(x, y, width, height);

        this.category = category;
        this.categoryName = category.name();
    }

    public void select() {
        selected = true;
    }
    public void deselect() { selected = false; }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        if (selected && !isHovered(mouseX, mouseY)) context.fill(x, y, x+width, y+height, new Color(145, 145, 145, 170).getRGB());
        else if (selected && isHovered(mouseX, mouseY)) context.fill(x, y, x+width, y+height, new Color(145, 145, 145, 130).getRGB());
        else if (isHovered(mouseX, mouseY)) context.fill(x, y, x+width, y+height, new Color(36, 36, 36, 255).getRGB());
        else context.fill(x, y, x+width, y+height, new Color(36, 36, 36, 100).getRGB());

        context.drawTextWithShadow(
                mc.textRenderer,
                categoryName,
                x+(width/8),
                y+(height/2)-(mc.textRenderer.fontHeight/2),
                Color.WHITE.getRGB()
        );
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY)) {
            if (mouseButton == 0) {
                select();
                return true;
            }
        }

        return false;
    }

}
