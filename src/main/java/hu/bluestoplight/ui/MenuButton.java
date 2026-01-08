package hu.bluestoplight.ui;

import hu.bluestoplight.SednaClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;

public class MenuButton extends ButtonWidget{
    public MenuButton(int x, int y, int width, int height, String message, PressAction onPress) {
        super(x, y, width, height, net.minecraft.text.Text.of(message), onPress, DEFAULT_NARRATION_SUPPLIER);
    }

    @Override
    protected void drawIcon(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        int color = this.isHovered() ? UIUtils.PRIMARY.getRGB() : UIUtils.BACKGROUND_BASE.getRGB();
        context.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), color);

        int x = this.getX() + 4;
        int y = this.getY() + (this.getHeight() - 8) / 2;

        context.drawText(SednaClient.mc.textRenderer, this.getMessage(), x, y, UIUtils.LIGHT.brighter().getRGB(), false);
    }
}
