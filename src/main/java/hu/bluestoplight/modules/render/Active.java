package hu.bluestoplight.modules.render;

import hu.bluestoplight.modules.Mod;
import hu.bluestoplight.modules.ModManager;
import hu.bluestoplight.modules.world.Radar;
import hu.bluestoplight.ui.UIUtils;
import net.minecraft.client.gui.DrawContext;

import java.util.Comparator;
import java.util.List;

import static hu.bluestoplight.SednaClient.mc;

public class Active extends Mod {
    public Active() {
        super("Active", "Displays mod arraylist.", Category.RENDER);
    }

    public static void render(DrawContext drawContext) {
        int id = 0;
        int width = mc.getWindow().getScaledWidth();

        List<Mod> enabled = ModManager.get().getEnabled();
        enabled.sort(Comparator.comparingInt(m -> mc.textRenderer.getWidth(((Mod)m).getDisplayName())).reversed());

        int align = Radar.isRendering ? (100 + (mc.textRenderer.fontHeight + UIUtils.margin)) : 0; // 2 * RADIUS + card_height
        int limit = Radar.isRendering ? 6 : 12;

        List<Mod> renderList = enabled.stream().limit(limit).toList();

        for (Mod mod : renderList) {
            drawContext.fill(width - mc.textRenderer.getWidth(mod.getName()) - (2 * UIUtils.margin), align + id * (mc.textRenderer.fontHeight + UIUtils.margin), width, align + (id + 1) * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.BACKGROUND_BASE.getRGB());
            drawContext.fill(width - mc.textRenderer.getWidth(mod.getName()) - (2 * UIUtils.margin) + UIUtils.margin / 4, align + id * (mc.textRenderer.fontHeight + UIUtils.margin), width - mc.textRenderer.getWidth(mod.getName()) - (2 * UIUtils.margin), align + (id + 1) * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.getSelectedPrimaryColor().getRGB());

            drawContext.drawTextWithShadow(mc.textRenderer, mod.getDisplayName(), (width - UIUtils.margin) - mc.textRenderer.getWidth(mod.getDisplayName()), align + (UIUtils.margin / 2) + id * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.LIGHT.getRGB());
            id++;
        }

        if (enabled.size() > limit) {
            String renderText = "+" + (enabled.size() - renderList.size());

            drawContext.fill(width - mc.textRenderer.getWidth(renderText) - (2 * UIUtils.margin), align + id * (mc.textRenderer.fontHeight + UIUtils.margin), width, align + (id + 1) * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.BACKGROUND_BASE.getRGB());
            drawContext.fill(width - mc.textRenderer.getWidth(renderText) - (2 * UIUtils.margin) + UIUtils.margin / 4, align + id * (mc.textRenderer.fontHeight + UIUtils.margin), width - mc.textRenderer.getWidth(renderText) - (2 * UIUtils.margin), align + (id + 1) * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.BACKGROUND_BASE.darker().darker().getRGB());

            drawContext.drawTextWithShadow(mc.textRenderer, renderText, (width - UIUtils.margin) - mc.textRenderer.getWidth(renderText), align + (UIUtils.margin / 2) + id * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.DARK.getRGB());
        }
    }
}
