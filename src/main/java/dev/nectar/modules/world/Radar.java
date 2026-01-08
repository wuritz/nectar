package dev.nectar.modules.world;

import dev.nectar.modules.Module;
import dev.nectar.modules.util.settings.NumberSetting;
import dev.nectar.ui.UIUtils;
import dev.nectar.utils.Utils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;

import static dev.nectar.Nectar.mc;

public class Radar extends Module {
    public Radar() {
        super("Radar", "Locates entities around you.", Category.WORLD);

        addSetting(range);
    }

    public static boolean isRendering;
    private static final int RADIUS = 50;
    private static NumberSetting range = new NumberSetting("Range", 20.0f, 40.0f, 32.0f, 1.0f);

    @Override
    public void onEnable() {
        super.onEnable();
        isRendering = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        isRendering = false;
    }

    public static void render(DrawContext drawContext) {
        if (!Utils.isToggleable()) return;

        int width = mc.getWindow().getScaledWidth();

        int centerX = width - RADIUS;
        int centerY = mc.textRenderer.fontHeight + UIUtils.margin + RADIUS;

        drawContext.fill(width - (2 * RADIUS), 0, width, (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.BACKGROUND_BASE.getRGB());
        drawContext.drawTextWithShadow(mc.textRenderer, "Radar", centerX - (mc.textRenderer.getWidth("Radar") / 2), UIUtils.margin / 2, UIUtils.getSelectedPrimaryColor().getRGB());

        drawContext.fill(centerX - RADIUS, centerY - RADIUS, centerX + RADIUS, centerY + RADIUS, UIUtils.BACKGROUND_BASE.getRGB());

        drawIndicator(drawContext, centerX, centerY, UIUtils.getSelectedPrimaryColor());

        java.util.List<Entity> entities = new ArrayList<>();
        mc.world.getEntities().forEach(entities::add);

        Vec3d playerPos = mc.player.getEntityPos();

        for (Entity e : entities) {
            if (e == mc.player) continue;
            if (!(e instanceof LivingEntity)) continue;

            double dist = e.getEntityPos().distanceTo(playerPos);
            if (dist > range.getValue()) continue;

            Vec3d delta = e.getEntityPos().subtract(playerPos);
            double scale = RADIUS / range.getValue();

            int indicatorX = centerX + (int)(delta.x * scale);
            int indicatorY = centerY + (int)(delta.z * scale);

            Color color = UIUtils.getEntityColor(e);
            drawIndicator(drawContext, indicatorX, indicatorY, color);

            String displayName = e instanceof PlayerEntity ? e.getName().getString() : e.getType().getName().getString();
            drawContext.drawTextWithShadow(mc.textRenderer, Text.literal(displayName), indicatorX - mc.textRenderer.getWidth(displayName) / 2, indicatorY + 3, UIUtils.LIGHT.getRGB());
        }
    }

    private static void drawIndicator(DrawContext context, int x, int y, Color color) {
        context.fill(x - 2, y - 2, x + 2,  y + 2, color.getRGB());
    }
}
