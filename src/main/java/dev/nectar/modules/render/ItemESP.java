package dev.nectar.modules.render;

import dev.nectar.modules.Module;
import dev.nectar.modules.util.settings.NumberSetting;
import dev.nectar.ui.UIUtils;
import dev.nectar.utils.SednaRenderLayers;
import dev.nectar.utils.Utils;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

import static dev.nectar.Nectar.mc;

public class ItemESP extends Module {
    private static boolean shouldRender = false;
    private static NumberSetting range = new NumberSetting("Range", 10.0f, 40.0f, 32.0f, 1.0f);

    public ItemESP() {
        super("ItemESP", "Highlights items with bounding boxes.", Category.RENDER);
        addSetting(range);
    }

    public static void render(WorldRenderContext context) {
        if (shouldRender) {
            if (!Utils.isToggleable()) return;

            Camera camera = context.gameRenderer().getCamera();
            Vec3d cameraPos = camera.getCameraPos();
            MatrixStack matrixStack = context.matrices();

            VertexConsumerProvider.Immediate consumers = mc.getBufferBuilders().getEntityVertexConsumers();
            VertexConsumer lines = consumers.getBuffer(SednaRenderLayers.ESP_QUADS_DEFAULT);

            matrixStack.push();
            matrixStack.translate(-cameraPos.getX(), -cameraPos.getY(), -cameraPos.getZ());

            for (Entity entity : mc.world.getEntities()) {
                if (entity == mc.player) continue;
                if (!(entity instanceof ItemEntity)) continue;

                double dist = entity.getEntityPos().distanceTo(mc.player.getEntityPos());
                if (dist > range.getValue()) continue;

                Box box = entity.getBoundingBox();
                UIUtils.espBox(matrixStack, lines, box, Color.YELLOW.brighter());
            }

            matrixStack.pop();
            consumers.draw();

            lines = consumers.getBuffer(SednaRenderLayers.ESP_DEFAULT_LINES);
            matrixStack.push();
            matrixStack.translate(-cameraPos.getX(), -cameraPos.getY(), -cameraPos.getZ());

            for (Entity entity : mc.world.getEntities()) {
                if (entity == mc.player) continue;
                if (!(entity instanceof ItemEntity)) continue;

                double dist = entity.getEntityPos().distanceTo(mc.player.getEntityPos());
                if (dist > range.getValue()) continue;

                Box box = entity.getBoundingBox();
                UIUtils.espOutline(matrixStack, lines, box, Color.YELLOW.darker());
            }

            matrixStack.pop();
            consumers.draw();
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        shouldRender = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        shouldRender = false;
    }
}
