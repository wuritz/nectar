package dev.nectar.modules.render;

import dev.nectar.core.Color;
import dev.nectar.core.renderer.ShapeMode;
import dev.nectar.events.core.render.Render3DEvent;
import dev.nectar.modules.Module;
import dev.nectar.modules.util.settings.ModeSetting;
import dev.nectar.modules.util.settings.NumberSetting;
import dev.nectar.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

import static dev.nectar.Nectar.mc;

public class MobESP extends Module {
    private static NumberSetting range = new NumberSetting("Range", 10.0f, 125.0f, 32.0f, 1.0f);
    private static ModeSetting type = new ModeSetting("Target", "Hostile", "All", "Hostile", "Passive", "Player");

    public MobESP() {
        super("MobESP", "Highlights mobs with bounding boxes.", Category.RENDER);
        addSettings(range, type);
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        if (!Utils.isToggleable()) return;

        /*Camera camera = context.gameRenderer().getCamera();
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
        consumers.draw();*/

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
            event.renderer.box(box, Color.BLUE.a(10), Color.WHITE, ShapeMode.Both, 0);
        }
    }
}