package dev.nectar.systems.hud;

import dev.nectar.events.core.render.Render2DEvent;
import dev.nectar.misc.References;
import dev.nectar.systems.System;
import dev.nectar.ui.UIUtils;
import dev.nectar.utils.misc.interfaces.ISerializable;
import meteordevelopment.orbit.EventHandler;

import static dev.nectar.Nectar.mc;

public class HUD extends System<HUD> implements ISerializable<HUD> {

    private int margin = 8;

    public HUD() {
        super("hud");
    }

    @EventHandler
    public void onRender2D(Render2DEvent event) {
        if (!mc.inGameHud.getDebugHud().shouldShowDebugHud()) {
            event.drawContext.fill(0, 0, mc.textRenderer.getWidth(References.CLIENT_NAME + " " + References.CLIENT_VERSION) + margin + (margin / 4), mc.textRenderer.fontHeight + margin, UIUtils.BACKGROUND_BASE.getRGB());
            event.drawContext.fill(mc.textRenderer.getWidth(References.CLIENT_NAME + " " + References.CLIENT_VERSION) + margin, 0, mc.textRenderer.getWidth(References.CLIENT_NAME + " " + References.CLIENT_VERSION) + margin + (margin / 4), mc.textRenderer.fontHeight + margin, UIUtils.getSelectedPrimaryColor().getRGB());
            event.drawContext.drawTextWithShadow(mc.textRenderer, References.CLIENT_NAME + " " + References.CLIENT_VERSION, margin / 2, margin / 2, UIUtils.getSelectedPrimaryColor().getRGB());
        }
    }
}