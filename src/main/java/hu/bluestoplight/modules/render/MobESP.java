package hu.bluestoplight.modules.render;

import hu.bluestoplight.modules.Mod;
import hu.bluestoplight.modules.util.settings.ModeSetting;
import hu.bluestoplight.modules.util.settings.NumberSetting;
import hu.bluestoplight.ui.UIUtils;
import hu.bluestoplight.utils.SednaRenderLayers;
import hu.bluestoplight.utils.Utils;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import static hu.bluestoplight.SednaClient.mc;

public class MobESP extends Mod {
    private static boolean shouldRender = false;
    private static NumberSetting range = new NumberSetting("Range", 10.0f, 40.0f, 32.0f, 1.0f);
    private static ModeSetting type = new ModeSetting("Target", "Hostile", "All", "Hostile", "Passive", "Player");

    public MobESP() {
        super("MobESP", "Highlights mobs with bounding boxes.", Category.RENDER);
        addSettings(range, type);
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
                if (!(entity instanceof LivingEntity)) continue;

                if (type.isMode("Hostile")) {
                    if (!(entity instanceof HostileEntity)) continue;
                } else if (type.isMode("Passive")) {
                    if (!(entity instanceof PassiveEntity)) continue;
                } else if (type.isMode("Player")) {
                    if (!(entity instanceof PlayerEntity)) continue;
                }

                double dist = entity.getEntityPos().distanceTo(mc.player.getEntityPos());
                if (dist > range.getValue()) continue;

                Box box = entity.getBoundingBox();
                UIUtils.espBox(matrixStack, lines, box, UIUtils.getEntityColor(entity).brighter());
            }

            matrixStack.pop();
            consumers.draw();

            lines = consumers.getBuffer(SednaRenderLayers.ESP_DEFAULT_LINES);
            matrixStack.push();
            matrixStack.translate(-cameraPos.getX(), -cameraPos.getY(), -cameraPos.getZ());

            for (Entity entity : mc.world.getEntities()) {
                if (entity == mc.player) continue;
                if (!(entity instanceof LivingEntity)) continue;

                if (type.isMode("Hostile")) {
                    if (!(entity instanceof HostileEntity)) continue;
                } else if (type.isMode("Passive")) {
                    if (!(entity instanceof PassiveEntity)) continue;
                } else if (type.isMode("Player")) {
                    if (!(entity instanceof PlayerEntity)) continue;
                }

                double dist = entity.getEntityPos().distanceTo(mc.player.getEntityPos());
                if (dist > range.getValue()) continue;

                Box box = entity.getBoundingBox();
                UIUtils.espOutline(matrixStack, lines, box, UIUtils.getEntityColor(entity).darker());
            }

            matrixStack.pop();
            consumers.draw();
        }
    }
}