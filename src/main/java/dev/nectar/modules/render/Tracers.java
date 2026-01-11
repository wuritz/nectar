package dev.nectar.modules.render;

import dev.nectar.modules.Module;
import dev.nectar.modules.util.settings.ModeSetting;
import dev.nectar.modules.util.settings.NumberSetting;
import dev.nectar.ui.UIUtils;
import dev.nectar.utils.SednaRenderLayers;
import dev.nectar.utils.Utils;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import static dev.nectar.Nectar.mc;

public class Tracers extends Module {
    private static boolean shouldRender = false;
    private static NumberSetting range = new NumberSetting("Range", 20.0f, 40.0f, 32.0f, 1.0f);
    private static ModeSetting type = new ModeSetting("Target", "All", "All", "Hostile", "Passive", "Player");

    public Tracers() {
        super("Tracers", "Points to the entities nearby.", Category.RENDER);
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

            Vec3d cameraPos = context.gameRenderer().getCamera().getCameraPos();
            Vec3d forward = mc.player.getRotationVec(1.0f);

            MatrixStack matrixStack = context.matrices();

            VertexConsumerProvider.Immediate vertexConsumerEntity = mc.getBufferBuilders().getEntityVertexConsumers();
            VertexConsumer consumer = vertexConsumerEntity.getBuffer(SednaRenderLayers.ESP_DEFAULT_LINES);
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

                Vec3d start = cameraPos.add(forward.multiply(0.75));

                double x = entity.lastX + (entity.getX() - entity.lastX) * mc.getRenderTickCounter().getDynamicDeltaTicks();
                double y = entity.lastY + (entity.getY() - entity.lastY) * mc.getRenderTickCounter().getDynamicDeltaTicks();
                double z = entity.lastZ + (entity.getZ() - entity.lastZ) * mc.getRenderTickCounter().getDynamicDeltaTicks();

                Vec3d end = new Vec3d(x, y, z);

                UIUtils.tracerLine(matrixStack, consumer, start, end, UIUtils.getEntityColor(entity));
            }

            matrixStack.pop();
            vertexConsumerEntity.draw();
        }
    }
}
