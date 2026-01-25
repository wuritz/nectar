package dev.nectar.ui;

import dev.nectar.modules.Modules;
import dev.nectar.modules.render.RainbowHue;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class UIUtils {
    public static int light = 0xF000F0;
    public static boolean isRainbowEnabled = false;

    public static final Color PRIMARY = new Color(0xAF1E8AA9, true);
    public static final Color ACCENT = new Color(0xAF9271D9, true);
    public static final Color BACKGROUND_BASE = new Color(0xAF0D0D0D, true);
    public static final Color BACKGROUND_LAYER = new Color(0xAF141414, true);
    public static final Color BACKGROUND_MID = new Color(0xAF333333, true);
    public static final Color LIGHT = new Color(0x9D9D9D);
    public static final Color DARK = new Color(0x626262);

    private static final int BASE_COLOR = 0xAF1E8AA9;
    private static int selectedPrimary = BASE_COLOR;

    public static int margin = 8;

    public static Color getSelectedPrimaryColor() {
        if (isRainbowEnabled) {
            selectedPrimary = Modules.get().get(RainbowHue.class).getPrimaryColor();
        } else {
            selectedPrimary = BASE_COLOR;
        }

        return new Color(selectedPrimary);
    }

    public static int getColor(float seconds, float saturation, float brightness) {
        float hue = (System.currentTimeMillis() % (int)(seconds * 1000)) / (seconds * 1000f);

        return Color.HSBtoRGB(hue, saturation, brightness);
    }

    public static Color getEntityColor(Entity e) {
        if (e instanceof PlayerEntity) return PRIMARY;
        if (e instanceof HostileEntity) return ACCENT;
        if (e instanceof PassiveEntity) return BACKGROUND_MID.brighter().brighter();

        return BACKGROUND_LAYER;
    }

}