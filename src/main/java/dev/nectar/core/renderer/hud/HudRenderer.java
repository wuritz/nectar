package dev.nectar.core.renderer.hud;

import dev.nectar.Nectar;
import dev.nectar.core.Color;
import dev.nectar.core.renderer.Renderer2D;
import dev.nectar.core.renderer.text.VanillaTextRenderer;
import dev.nectar.utils.Utils;
import dev.nectar.utils.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static dev.nectar.Nectar.mc;

public class HudRenderer {
    public static final HudRenderer INSTANCE = new HudRenderer();

    private final List<Runnable> postTasks = new ArrayList<>();

    public DrawContext drawContext;
    public double delta;

    private HudRenderer() {
        Nectar.EVENT_BUS.subscribe(this);
    }

    public void begin(DrawContext drawContext) {
        Renderer2D.COLOR.begin();

        this.drawContext = drawContext;
        this.delta = Utils.frameTime;

        drawContext.createNewRootLayer();

        VanillaTextRenderer.INSTANCE.scaleIndividually = true;
        VanillaTextRenderer.INSTANCE.begin();
    }

    public void end() {
        Renderer2D.COLOR.render();

        VanillaTextRenderer.INSTANCE.end();
        VanillaTextRenderer.INSTANCE.scaleIndividually = false;

        for (Runnable task : postTasks) task.run();
        postTasks.clear();

        drawContext.createNewRootLayer();

        drawContext = null;
    }

    public void line(double x1, double y1, double x2, double y2, Color color) {
        Renderer2D.COLOR.line(x1, y1, x2, y2, color);
    }

    public void quad(double x, double y, double width, double height, Color color) {
        Renderer2D.COLOR.quad(x, y, width, height, color);
    }

    public void quad(double x, double y, double width, double height, Color cTopLeft, Color cTopRight, Color cBottomRight, Color cBottomLeft) {
        Renderer2D.COLOR.quad(x, y, width, height, cTopLeft, cTopRight, cBottomRight, cBottomLeft);
    }

    public void triangle(double x1, double y1, double x2, double y2, double x3, double y3, Color color) {
        Renderer2D.COLOR.triangle(x1, y1, x2, y2, x3, y3, color);
    }

    public void texture(Identifier id, double x, double y, double width, double height, Color color) {
        Renderer2D.TEXTURE.begin();
        Renderer2D.TEXTURE.texQuad(x, y, width, height, color);
        Renderer2D.TEXTURE.render(mc.getTextureManager().getTexture(id).getGlTextureView(), mc.getTextureManager().getTexture(id).getSampler());
    }

    public double text(String text, double x, double y, Color color, boolean shadow, double scale) {
        if (scale == -1) scale = 1;

        VanillaTextRenderer.INSTANCE.scale = scale * 2;
        return VanillaTextRenderer.INSTANCE.render(text, x, y, color, shadow);
    }

    public double text(String text, double x, double y, Color color, boolean shadow) {
        return text(text, x, y, color, shadow, -1);
    }

    public double textWidth(String text, boolean shadow, double scale) {
        if (text.isEmpty()) return 0;

        VanillaTextRenderer.INSTANCE.scale = (scale == -1 ? 1 : scale) * 2;
        return VanillaTextRenderer.INSTANCE.getWidth(text, shadow);
    }

    public double textWidth(String text, boolean shadow) {
        return textWidth(text, shadow, -1);
    }
    public double textWidth(String text, double scale) {
        return textWidth(text, false, scale);
    }
    public double textWidth(String text) {
        return textWidth(text, false, -1);
    }

    public double textHeight(boolean shadow, double scale) {
        VanillaTextRenderer.INSTANCE.scale = (scale == -1 ? 1 : scale) * 2;
        return VanillaTextRenderer.INSTANCE.getHeight(shadow);
    }

    public double textHeight(boolean shadow) {
        return textHeight(shadow, -1);
    }
    public double textHeight() {
        return textHeight(false, -1);
    }

    public void post(Runnable task) {
        postTasks.add(task);
    }

    public void item(ItemStack itemStack, int x, int y, float scale, boolean overlay, String countOverlay) {
        RenderUtils.drawItem(drawContext, itemStack, x, y, scale, overlay, countOverlay, true);
    }

    public void item(ItemStack itemStack, int x, int y, float scale, boolean overlay) {
        RenderUtils.drawItem(drawContext, itemStack, x, y, scale, overlay);
    }

    public void entity(LivingEntity entity, int x, int y, int width, int height, float yaw, float pitch) {
        float previousBodyYaw = entity.bodyYaw;
        float previousYaw = entity.getYaw();
        float previousPitch = entity.getPitch();
        float lastLastHeadYaw = entity.lastHeadYaw;
        float lastHeadYaw = entity.headYaw;

        float tanYaw = (float) Math.atan((yaw) / 40.0f);
        float tanPitch = (float) Math.atan((pitch) / 40.0f);
        entity.bodyYaw = 180.0f + tanYaw * 20.0f;
        entity.setYaw(180.0f + tanYaw * 40.0f);
        entity.setPitch(-tanPitch * 20.0f);
        entity.headYaw = entity.getYaw();
        entity.lastHeadYaw = entity.getYaw();

        var state = (LivingEntityRenderState) mc.getEntityRenderDispatcher().getRenderer(entity).getAndUpdateRenderState(entity, 1);

        entity.bodyYaw = previousBodyYaw;
        entity.setYaw(previousYaw);
        entity.setPitch(previousPitch);
        entity.lastHeadYaw = lastLastHeadYaw;
        entity.headYaw = lastHeadYaw;

        float s = 1.0f / mc.getWindow().getScaleFactor();
        int x1 = (int) (x * s);
        int y1 = (int) (y * s);
        int x2 = (int) ((x + width) * s);
        int y2 = (int) ((y + height) * s);

        float scale = Math.max(width, height) * s / 2f;
        Vector3f translation = new Vector3f(0, 1f, 0);
        Quaternionf rotation = new Quaternionf().rotateZ((float) Math.PI);

        drawContext.addEntity(state, scale, translation, rotation, null, x1, y1, x2, y2);
    }
}
