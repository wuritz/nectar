package hu.bluestoplight.ui;

import hu.bluestoplight.SednaClient;
import hu.bluestoplight.misc.References;
import hu.bluestoplight.modules.Mod;
import hu.bluestoplight.modules.ModManager;
import hu.bluestoplight.modules.combat.ArmorStatus;
import hu.bluestoplight.modules.render.Active;
import hu.bluestoplight.modules.world.BiomeInfo;
import hu.bluestoplight.modules.world.Clock;
import hu.bluestoplight.modules.world.Coordinates;
import hu.bluestoplight.modules.world.Radar;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.util.List;
import java.util.Objects;

public class HUD implements HudRenderCallback {
    private static MinecraftClient mc = SednaClient.mc;
    private int margin = 8;

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        if (!mc.inGameHud.getDebugHud().shouldShowDebugHud()) {
            drawContext.fill(0, 0, mc.textRenderer.getWidth(References.CLIENT_NAME + " " + References.CLIENT_VERSION) + margin + (margin / 4), mc.textRenderer.fontHeight + margin, UIUtils.BACKGROUND_BASE.getRGB());
            drawContext.fill(mc.textRenderer.getWidth(References.CLIENT_NAME + " " + References.CLIENT_VERSION) + margin, 0, mc.textRenderer.getWidth(References.CLIENT_NAME + " " + References.CLIENT_VERSION) + margin + (margin / 4), mc.textRenderer.fontHeight + margin, UIUtils.getSelectedPrimaryColor().getRGB());
            drawContext.drawTextWithShadow(mc.textRenderer, References.CLIENT_NAME + " " + References.CLIENT_VERSION, margin / 2, margin / 2, UIUtils.getSelectedPrimaryColor().getRGB());

            List<Mod> enabled = ModManager.get().getEnabled();

            for (Mod mod : enabled) {
                if (Objects.equals(mod.getName(), "Active")) {
                    Active.render(drawContext);
                } else if (Objects.equals(mod.getName(), "Coordinates")) {
                    Coordinates.render(drawContext);
                } else if (Objects.equals(mod.getName(), "BiomeInfo")) {
                    BiomeInfo.render(drawContext);
                } else if (Objects.equals(mod.getName(), "Clock")) {
                    Clock.render(drawContext);
                } else if (Objects.equals(mod.getName(), "Radar")) {
                    Radar.render(drawContext);
                } else if (Objects.equals(mod.getName(), "ArmorStatus")) {
                    ArmorStatus.render(drawContext);
                }
            }
        }
    }
}