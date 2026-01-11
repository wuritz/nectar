package dev.nectar.gui;

import dev.nectar.Nectar;
import dev.nectar.core.Color;
import net.minecraft.client.gui.DrawContext;

import static dev.nectar.Nectar.mc;

public class NectarTitleScreen {

    public static void render(DrawContext context) {
        String text = "Nectar - " + Nectar.VERSION.toString();

        int y = 3;
        int x = 3;

        context.drawTextWithShadow(mc.textRenderer, text, x, y, new Color(252, 186, 3, 255).getPacked());
    }

}
