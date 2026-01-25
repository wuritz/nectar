package dev.nectar.modules.render;

import dev.nectar.events.core.render.Render2DEvent;
import dev.nectar.modules.Module;
import dev.nectar.modules.Modules;
import dev.nectar.modules.world.Radar;
import dev.nectar.ui.UIUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.DrawContext;

import java.util.Comparator;
import java.util.List;

import static dev.nectar.Nectar.mc;

public class Active extends Module {
    public Active() {
        super("Active", "Displays mod arraylist.", Category.RENDER);
    }

    @EventHandler
    public void onRender2D(Render2DEvent event) {
        int id = 0;
        int width = mc.getWindow().getScaledWidth();

        List<Module> enabled = Modules.get().getActive();
        enabled.sort(Comparator.comparingInt(m -> mc.textRenderer.getWidth(((Module)m).getDisplayName())).reversed());

        int align = Radar.isRendering ? (100 + (mc.textRenderer.fontHeight + UIUtils.margin)) : 0; // 2 * RADIUS + card_height
        int limit = Radar.isRendering ? 6 : 12;

        List<Module> renderList = enabled.stream().limit(limit).toList();

        for (Module mod : renderList) {
            event.drawContext.fill(width - mc.textRenderer.getWidth(mod.getName()) - (2 * UIUtils.margin), align + id * (mc.textRenderer.fontHeight + UIUtils.margin), width, align + (id + 1) * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.BACKGROUND_BASE.getRGB());
            event.drawContext.fill(width - mc.textRenderer.getWidth(mod.getName()) - (2 * UIUtils.margin) + UIUtils.margin / 4, align + id * (mc.textRenderer.fontHeight + UIUtils.margin), width - mc.textRenderer.getWidth(mod.getName()) - (2 * UIUtils.margin), align + (id + 1) * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.getSelectedPrimaryColor().getRGB());

            event.drawContext.drawTextWithShadow(mc.textRenderer, mod.getDisplayName(), (width - UIUtils.margin) - mc.textRenderer.getWidth(mod.getDisplayName()), align + (UIUtils.margin / 2) + id * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.LIGHT.getRGB());
            id++;
        }

        if (enabled.size() > limit) {
            String renderText = "+" + (enabled.size() - renderList.size());

            event.drawContext.fill(width - mc.textRenderer.getWidth(renderText) - (2 * UIUtils.margin), align + id * (mc.textRenderer.fontHeight + UIUtils.margin), width, align + (id + 1) * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.BACKGROUND_BASE.getRGB());
            event.drawContext.fill(width - mc.textRenderer.getWidth(renderText) - (2 * UIUtils.margin) + UIUtils.margin / 4, align + id * (mc.textRenderer.fontHeight + UIUtils.margin), width - mc.textRenderer.getWidth(renderText) - (2 * UIUtils.margin), align + (id + 1) * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.BACKGROUND_BASE.darker().darker().getRGB());

            event.drawContext.drawTextWithShadow(mc.textRenderer, renderText, (width - UIUtils.margin) - mc.textRenderer.getWidth(renderText), align + (UIUtils.margin / 2) + id * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.DARK.getRGB());
        }
    }
}
