package hu.bluestoplight.ui;

import hu.bluestoplight.modules.render.RainbowHue;
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
            selectedPrimary = RainbowHue.getPrimaryColor();
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

    public static void espBox(MatrixStack matrixStack, VertexConsumer vertexConsumer, Box box, Color color) {
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 50);

        MatrixStack.Entry entry = matrixStack.peek();

        float x1 = (float) box.minX;
        float y1 = (float) box.minY;
        float z1 = (float) box.minZ;

        float x2 = (float) box.maxX;
        float y2 = (float) box.maxY;
        float z2 = (float) box.maxZ;

        quad(entry, vertexConsumer, x1,y1,z1, x2,y1,z1, x2,y1,z2, x1,y1,z2, color);
        quad(entry, vertexConsumer, x1,y2,z1, x2,y2,z1, x2,y2,z2, x1,y2,z2, color);
        quad(entry, vertexConsumer, x1,y1,z1, x2,y1,z1, x2,y2,z1, x1,y2,z1, color);
        quad(entry, vertexConsumer, x2,y1,z1, x2,y1,z2, x2,y2,z2, x2,y2,z1, color);
        quad(entry, vertexConsumer, x2,y1,z2, x1,y1,z2, x1,y2,z2, x2,y2,z2, color);
        quad(entry, vertexConsumer, x1,y1,z2, x1,y1,z1, x1,y2,z1, x1,y2,z2, color);
    }

    public static void espOutline(MatrixStack matrixStack, VertexConsumer vertexConsumer, Box box, Color color) {
        MatrixStack.Entry entry = matrixStack.peek();

        float x1 = (float) box.minX;
        float y1 = (float) box.minY;
        float z1 = (float) box.minZ;

        float x2 = (float) box.maxX;
        float y2 = (float) box.maxY;
        float z2 = (float) box.maxZ;

        line(entry, vertexConsumer, x1, y1, z1, x2, y1, z1, color);
        line(entry, vertexConsumer, x2, y1, z1, x2, y1, z2, color);
        line(entry, vertexConsumer, x2, y1, z2, x1, y1, z2, color);
        line(entry, vertexConsumer, x1, y1, z2, x1, y1, z1, color);

        line(entry, vertexConsumer, x1, y2, z1, x2, y2, z1, color);
        line(entry, vertexConsumer, x2, y2, z1, x2, y2, z2, color);
        line(entry, vertexConsumer, x2, y2, z2, x1, y2, z2, color);
        line(entry, vertexConsumer, x1, y2, z2, x1, y2, z1, color);

        line(entry, vertexConsumer, x1, y1, z1, x1, y2, z1, color);
        line(entry, vertexConsumer, x2, y1, z1, x2, y2, z1, color);
        line(entry, vertexConsumer, x2, y1, z2, x2, y2, z2, color);
        line(entry, vertexConsumer, x1, y1, z2, x1, y2, z2, color);
    }

    private static void line(MatrixStack.Entry entry, VertexConsumer consumer, float x1, float y1, float z1, float x2, float y2, float z2, Color color) {
        consumer.vertex(entry.getPositionMatrix(), x1, y1, z1).color(color.getRGB()).normal(0, 1, 0).lineWidth(3.0f).light(light);
        consumer.vertex(entry.getPositionMatrix(), x2, y2, z2).color(color.getRGB()).normal(0, 1, 0).lineWidth(3.0f).light(light);
    }

    public static void tracerLine(MatrixStack matrixStack, VertexConsumer consumer, Vec3d start, Vec3d end, Color color) {
        MatrixStack.Entry entry = matrixStack.peek();

        consumer.vertex(entry.getPositionMatrix(), (float) start.x, (float) start.y, (float) start.z).color(color.getRGB()).normal(0, 1, 0).lineWidth(4.0f).light(light);
        consumer.vertex(entry.getPositionMatrix(), (float) end.x, (float) end.y, (float) end.z).color(color.getRGB()).normal(0, 1, 0).lineWidth(4.0f).light(light);
    }

    private static void quad(MatrixStack.Entry entry, VertexConsumer consumer, float x1,float y1,float z1, float x2,float y2,float z2, float x3,float y3,float z3, float x4,float y4,float z4, Color color) {
        consumer.vertex(entry.getPositionMatrix(), x1,y1,z1).color(color.getRGB()).light(light).normal(0, 1, 0);
        consumer.vertex(entry.getPositionMatrix(), x2,y2,z2).color(color.getRGB()).light(light).normal(0, 1, 0);
        consumer.vertex(entry.getPositionMatrix(), x3,y3,z3).color(color.getRGB()).light(light).normal(0, 1, 0);
        consumer.vertex(entry.getPositionMatrix(), x4,y4,z4).color(color.getRGB()).light(light).normal(0, 1, 0);
    }
}