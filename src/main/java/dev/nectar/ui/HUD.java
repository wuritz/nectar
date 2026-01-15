package dev.nectar.ui;

import dev.nectar.Nectar;
import dev.nectar.misc.References;
import dev.nectar.modules.Module;
import dev.nectar.modules.ModuleManager;
import dev.nectar.modules.combat.ArmorStatus;
import dev.nectar.modules.render.Active;
import dev.nectar.modules.world.BiomeInfo;
import dev.nectar.modules.world.Clock;
import dev.nectar.modules.world.Coordinates;
import dev.nectar.modules.world.Radar;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecated")
public class HUD implements HudRenderCallback {
    private static MinecraftClient mc = Nectar.mc;
    private int margin = 8;

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        if (!mc.inGameHud.getDebugHud().shouldShowDebugHud()) {
            drawContext.fill(0, 0, mc.textRenderer.getWidth(References.CLIENT_NAME + " " + References.CLIENT_VERSION) + margin + (margin / 4), mc.textRenderer.fontHeight + margin, UIUtils.BACKGROUND_BASE.getRGB());
            drawContext.fill(mc.textRenderer.getWidth(References.CLIENT_NAME + " " + References.CLIENT_VERSION) + margin, 0, mc.textRenderer.getWidth(References.CLIENT_NAME + " " + References.CLIENT_VERSION) + margin + (margin / 4), mc.textRenderer.fontHeight + margin, UIUtils.getSelectedPrimaryColor().getRGB());
            drawContext.drawTextWithShadow(mc.textRenderer, References.CLIENT_NAME + " " + References.CLIENT_VERSION, margin / 2, margin / 2, UIUtils.getSelectedPrimaryColor().getRGB());

            List<Module> enabled = ModuleManager.get().getEnabled();

            for (Module mod : enabled) {
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