package dev.nectar.modules.world;

import dev.nectar.events.core.render.Render2DEvent;
import dev.nectar.modules.Module;
import dev.nectar.modules.setting.Setting;
import dev.nectar.modules.setting.settings.EnumSetting;
import dev.nectar.ui.UIUtils;
import meteordevelopment.orbit.EventHandler;

import java.util.Objects;

import static dev.nectar.Nectar.mc;

public class Coordinates extends Module {
    private final Setting<Mode> type = new EnumSetting.Builder<Mode>()
            .name("Type").description("")
            .defaultValue(Mode.Extended)
            .build();

    public Coordinates() {
        super("Coordinates", "Displays player coordinates.", Category.WORLD);

        addSetting(type);
    }

    @EventHandler
    public void onRender(Render2DEvent event) {
        int height = mc.getWindow().getScaledHeight();

        if (type.get() == Mode.Basic) {
            event.drawContext.fill(0, height - mc.textRenderer.fontHeight - UIUtils.margin, mc.textRenderer.getWidth("[XYZ]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin + (UIUtils.margin / 4), height, UIUtils.BACKGROUND_BASE.getRGB());
            event.drawContext.fill(mc.textRenderer.getWidth("[XYZ]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin, height - mc.textRenderer.fontHeight - UIUtils.margin, mc.textRenderer.getWidth("[XYZ]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin + (UIUtils.margin / 4), height, UIUtils.getSelectedPrimaryColor().getRGB());

            event.drawContext.drawTextWithShadow(mc.textRenderer , "[XYZ]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ(), UIUtils.margin / 2, height - mc.textRenderer.fontHeight - (UIUtils.margin / 2), UIUtils.getSelectedPrimaryColor().getRGB());
        } else {
            if (Objects.equals(mc.player.getEntityWorld().getRegistryKey().getValue().getPath(), "overworld")) {
                event.drawContext.fill(0, height - mc.textRenderer.fontHeight - UIUtils.margin, mc.textRenderer.getWidth("[Nether]: " + mc.player.getBlockX() / 8 + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ() / 8) + UIUtils.margin + (UIUtils.margin / 4), height, UIUtils.BACKGROUND_BASE.getRGB());
                event.drawContext.fill(mc.textRenderer.getWidth("[Nether]: " + mc.player.getBlockX() / 8 + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ() / 8) + UIUtils.margin, height - mc.textRenderer.fontHeight - UIUtils.margin, mc.textRenderer.getWidth("[Nether]: " + mc.player.getBlockX() / 8 + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ() / 8) + UIUtils.margin + (UIUtils.margin / 4), height, UIUtils.getSelectedPrimaryColor().getRGB());

                event.drawContext.fill(0, height - (2 * (mc.textRenderer.fontHeight + UIUtils.margin)), mc.textRenderer.getWidth("[Overworld]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin + (UIUtils.margin / 4), height - mc.textRenderer.fontHeight - UIUtils.margin, UIUtils.BACKGROUND_BASE.getRGB());
                event.drawContext.fill(mc.textRenderer.getWidth("[Overworld]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin, height - mc.textRenderer.fontHeight - UIUtils.margin, mc.textRenderer.getWidth("[Overworld]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin + (UIUtils.margin / 4), height - (2 * (mc.textRenderer.fontHeight + UIUtils.margin)), UIUtils.getSelectedPrimaryColor().getRGB());

                event.drawContext.drawTextWithShadow(mc.textRenderer , "[Overworld]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ(), UIUtils.margin / 2, height - (2 * (mc.textRenderer.fontHeight + (UIUtils.margin / 2) + (UIUtils.margin / 4))), UIUtils.getSelectedPrimaryColor().getRGB());
                event.drawContext.drawTextWithShadow(mc.textRenderer , "[Nether]: " + mc.player.getBlockX() / 8 + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ() / 8, UIUtils.margin / 2, height - mc.textRenderer.fontHeight - (UIUtils.margin / 2), UIUtils.LIGHT.darker().getRGB());
            } else if (Objects.equals(mc.player.getEntityWorld().getRegistryKey().getValue().getPath(), "the_nether")) {
                event.drawContext.fill(0, height - mc.textRenderer.fontHeight - UIUtils.margin, mc.textRenderer.getWidth("[Overworld]: " + mc.player.getBlockX() * 8 + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ() * 8) + UIUtils.margin + (UIUtils.margin / 4), height, UIUtils.BACKGROUND_BASE.getRGB());
                event.drawContext.fill(mc.textRenderer.getWidth("[Overworld]: " + mc.player.getBlockX() * 8 + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ() * 8) + UIUtils.margin, height - mc.textRenderer.fontHeight - UIUtils.margin, mc.textRenderer.getWidth("[Overworld]: " + mc.player.getBlockX() * 8 + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ() * 8) + UIUtils.margin + (UIUtils.margin / 4), height, UIUtils.getSelectedPrimaryColor().getRGB());

                event.drawContext.fill(0, height - (2 * (mc.textRenderer.fontHeight + UIUtils.margin)), mc.textRenderer.getWidth("[Nether]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin + (UIUtils.margin / 4), height - mc.textRenderer.fontHeight - UIUtils.margin, UIUtils.BACKGROUND_BASE.getRGB());
                event.drawContext.fill(mc.textRenderer.getWidth("[Nether]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin, height - mc.textRenderer.fontHeight - UIUtils.margin, mc.textRenderer.getWidth("[Nether]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin + (UIUtils.margin / 4), height - (2 * (mc.textRenderer.fontHeight + UIUtils.margin)), UIUtils.getSelectedPrimaryColor().getRGB());

                event.drawContext.drawTextWithShadow(mc.textRenderer , "[Nether]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ(), UIUtils.margin / 2, height - (2 * (mc.textRenderer.fontHeight + (UIUtils.margin / 2) + (UIUtils.margin / 4))), UIUtils.getSelectedPrimaryColor().getRGB());
                event.drawContext.drawTextWithShadow(mc.textRenderer , "[Overworld]: " + mc.player.getBlockX() * 8 + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ() * 8, UIUtils.margin / 2, height - mc.textRenderer.fontHeight - (UIUtils.margin / 2), UIUtils.LIGHT.darker().getRGB());
            } else {
                event.drawContext.fill(0, height - mc.textRenderer.fontHeight - UIUtils.margin, mc.textRenderer.getWidth("[XYZ]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin + (UIUtils.margin / 4), height, UIUtils.BACKGROUND_BASE.getRGB());
                event.drawContext.fill(mc.textRenderer.getWidth("[XYZ]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin, height - mc.textRenderer.fontHeight - UIUtils.margin, mc.textRenderer.getWidth("[XYZ]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ()) + UIUtils.margin + (UIUtils.margin / 4), height, UIUtils.getSelectedPrimaryColor().getRGB());

                event.drawContext.drawTextWithShadow(mc.textRenderer , "[XYZ]: " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ(), UIUtils.margin / 2, height - mc.textRenderer.fontHeight - (UIUtils.margin / 2), UIUtils.getSelectedPrimaryColor().getRGB());
            }
        }
    }

    private enum Mode {
        Extended, Basic
    }
}