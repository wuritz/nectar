package dev.nectar.modules.world;

import dev.nectar.modules.Module;
import dev.nectar.modules.util.settings.ModeSetting;
import dev.nectar.ui.UIUtils;
import net.minecraft.client.gui.DrawContext;

import java.util.Objects;

import static dev.nectar.Nectar.mc;

public class Coordinates extends Module {
    private static ModeSetting type = new ModeSetting("Type", "Extended", "Basic", "Extended");

    public Coordinates() {
        super("Coordinates", "Displays player coordinates.", Category.WORLD);
        addSetting(type);
    }

    public static ModeSetting getType() {
        return type;
    }

    public static void render(DrawContext drawContext) {
        int height = mc.getWindow().getScaledHeight();

        if (Objects.equals(Coordinates.getType().getMode(), "Basic")) {
            drawContext.fill(0, height - mc.textRenderer.fontHeight - UIUtils.margin, mc.textRenderer.getWidth("[XYZ]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin + (UIUtils.margin / 4), height, UIUtils.BACKGROUND_BASE.getRGB());
            drawContext.fill(mc.textRenderer.getWidth("[XYZ]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin, height - mc.textRenderer.fontHeight - UIUtils.margin, mc.textRenderer.getWidth("[XYZ]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin + (UIUtils.margin / 4), height, UIUtils.getSelectedPrimaryColor().getRGB());

            drawContext.drawTextWithShadow(mc.textRenderer , "[XYZ]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ(), UIUtils.margin / 2, height - mc.textRenderer.fontHeight - (UIUtils.margin / 2), UIUtils.getSelectedPrimaryColor().getRGB());
        } else {
            if (Objects.equals(mc.player.getEntityWorld().getRegistryKey().getValue().getPath(), "overworld")) {
                drawContext.fill(0, height - mc.textRenderer.fontHeight - UIUtils.margin, mc.textRenderer.getWidth("[Nether]: " + mc.player.getBlockX() / 8 + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ() / 8) + UIUtils.margin + (UIUtils.margin / 4), height, UIUtils.BACKGROUND_BASE.getRGB());
                drawContext.fill(mc.textRenderer.getWidth("[Nether]: " + mc.player.getBlockX() / 8 + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ() / 8) + UIUtils.margin, height - mc.textRenderer.fontHeight - UIUtils.margin, mc.textRenderer.getWidth("[Nether]: " + mc.player.getBlockX() / 8 + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ() / 8) + UIUtils.margin + (UIUtils.margin / 4), height, UIUtils.getSelectedPrimaryColor().getRGB());

                drawContext.fill(0, height - (2 * (mc.textRenderer.fontHeight + UIUtils.margin)), mc.textRenderer.getWidth("[Overworld]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin + (UIUtils.margin / 4), height - mc.textRenderer.fontHeight - UIUtils.margin, UIUtils.BACKGROUND_BASE.getRGB());
                drawContext.fill(mc.textRenderer.getWidth("[Overworld]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin, height - mc.textRenderer.fontHeight - UIUtils.margin, mc.textRenderer.getWidth("[Overworld]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin + (UIUtils.margin / 4), height - (2 * (mc.textRenderer.fontHeight + UIUtils.margin)), UIUtils.getSelectedPrimaryColor().getRGB());

                drawContext.drawTextWithShadow(mc.textRenderer , "[Overworld]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ(), UIUtils.margin / 2, height - (2 * (mc.textRenderer.fontHeight + (UIUtils.margin / 2) + (UIUtils.margin / 4))), UIUtils.getSelectedPrimaryColor().getRGB());
                drawContext.drawTextWithShadow(mc.textRenderer , "[Nether]: " + mc.player.getBlockX() / 8 + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ() / 8, UIUtils.margin / 2, height - mc.textRenderer.fontHeight - (UIUtils.margin / 2), UIUtils.LIGHT.darker().getRGB());
            } else if (Objects.equals(mc.player.getEntityWorld().getRegistryKey().getValue().getPath(), "the_nether")) {
                drawContext.fill(0, height - mc.textRenderer.fontHeight - UIUtils.margin, mc.textRenderer.getWidth("[Overworld]: " + mc.player.getBlockX() * 8 + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ() * 8) + UIUtils.margin + (UIUtils.margin / 4), height, UIUtils.BACKGROUND_BASE.getRGB());
                drawContext.fill(mc.textRenderer.getWidth("[Overworld]: " + mc.player.getBlockX() * 8 + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ() * 8) + UIUtils.margin, height - mc.textRenderer.fontHeight - UIUtils.margin, mc.textRenderer.getWidth("[Overworld]: " + mc.player.getBlockX() * 8 + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ() * 8) + UIUtils.margin + (UIUtils.margin / 4), height, UIUtils.getSelectedPrimaryColor().getRGB());

                drawContext.fill(0, height - (2 * (mc.textRenderer.fontHeight + UIUtils.margin)), mc.textRenderer.getWidth("[Nether]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin + (UIUtils.margin / 4), height - mc.textRenderer.fontHeight - UIUtils.margin, UIUtils.BACKGROUND_BASE.getRGB());
                drawContext.fill(mc.textRenderer.getWidth("[Nether]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin, height - mc.textRenderer.fontHeight - UIUtils.margin, mc.textRenderer.getWidth("[Nether]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin + (UIUtils.margin / 4), height - (2 * (mc.textRenderer.fontHeight + UIUtils.margin)), UIUtils.getSelectedPrimaryColor().getRGB());

                drawContext.drawTextWithShadow(mc.textRenderer , "[Nether]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ(), UIUtils.margin / 2, height - (2 * (mc.textRenderer.fontHeight + (UIUtils.margin / 2) + (UIUtils.margin / 4))), UIUtils.getSelectedPrimaryColor().getRGB());
                drawContext.drawTextWithShadow(mc.textRenderer , "[Overworld]: " + mc.player.getBlockX() * 8 + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ() * 8, UIUtils.margin / 2, height - mc.textRenderer.fontHeight - (UIUtils.margin / 2), UIUtils.LIGHT.darker().getRGB());
            } else {
                drawContext.fill(0, height - mc.textRenderer.fontHeight - UIUtils.margin, mc.textRenderer.getWidth("[XYZ]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin + (UIUtils.margin / 4), height, UIUtils.BACKGROUND_BASE.getRGB());
                drawContext.fill(mc.textRenderer.getWidth("[XYZ]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin, height - mc.textRenderer.fontHeight - UIUtils.margin, mc.textRenderer.getWidth("[XYZ]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin + (UIUtils.margin / 4), height, UIUtils.getSelectedPrimaryColor().getRGB());

                drawContext.drawTextWithShadow(mc.textRenderer , "[XYZ]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ(), UIUtils.margin / 2, height - mc.textRenderer.fontHeight - (UIUtils.margin / 2), UIUtils.getSelectedPrimaryColor().getRGB());
            }
        }
    }
}