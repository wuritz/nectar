package dev.nectar.modules.player;

import dev.nectar.events.world.TickEvent;
import dev.nectar.modules.Module;
import dev.nectar.modules.util.settings.NumberSetting;
import dev.nectar.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import static dev.nectar.Nectar.mc;

public class FreeCam extends Module {
    public static boolean shouldFreeze = false;
    private static NumberSetting speed = new NumberSetting("Speed", 1.0, 10.0, 3.5, 0.1);

    Vec3d storedPos;
    float storedYaw, storedPitch;

    public FreeCam() {
        super("FreeCam", "Allows you to do astral projection.", Category.PLAYER);
        addSetting(speed);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (Utils.isToggleable()) {
            shouldFreeze = true;

            storedPos = mc.player.getEntityPos();
            storedYaw = mc.player.getYaw();
            storedPitch = mc.player.getPitch();

            OtherClientPlayerEntity freecamEntity = new OtherClientPlayerEntity(mc.world, mc.player.getGameProfile());

            freecamEntity.copyPositionAndRotation(mc.player);
            freecamEntity.setHeadYaw(mc.player.getHeadYaw());
            freecamEntity.setBodyYaw(mc.player.getBodyYaw());

            freecamEntity.lastHeadYaw = freecamEntity.getHeadYaw();
            freecamEntity.lastBodyYaw = freecamEntity.getBodyYaw();
            freecamEntity.lastYaw = freecamEntity.getYaw();
            freecamEntity.lastPitch = freecamEntity.getPitch();

            freecamEntity.setNoGravity(true);
            freecamEntity.noClip = true;
            freecamEntity.setOnGround(false);

            mc.setCameraEntity(freecamEntity);
        }
    }

    @EventHandler
    public void onTick(TickEvent.Pre event) {
        if (!FreeCam.shouldFreeze) return;

        Entity camera = mc.getCameraEntity();
        if (camera == null || mc.player == camera) return;

        Vec3d forward = Vec3d.fromPolar(0, camera.getYaw());
        Vec3d right = Vec3d.fromPolar(0, camera.getYaw() + 90);

        Vec3d velocity = Vec3d.ZERO;

        if (mc.options.forwardKey.isPressed()) velocity = velocity.add(forward);
        if (mc.options.backKey.isPressed()) velocity = velocity.subtract(forward);
        if (mc.options.leftKey.isPressed()) velocity = velocity.subtract(right);
        if (mc.options.rightKey.isPressed()) velocity = velocity.add(right);
        if (mc.options.jumpKey.isPressed()) velocity = velocity.add(0, 1, 0);
        if (mc.options.sneakKey.isPressed()) velocity = velocity.add(0, -1, 0);

        if (velocity.lengthSquared() > 0) {
            velocity = velocity.normalize().multiply(speed.getValue());
            Vec3d pos = camera.getEntityPos();

            double newX = pos.x + velocity.x;
            double newY = pos.y + velocity.y;
            double newZ = pos.z + velocity.z;

            camera.setPos(newX, newY, newZ);

            camera.lastX = newX;
            camera.lastY = newY;
            camera.lastZ = newZ;

            camera.lastRenderX = newX;
            camera.lastRenderY = newY;
            camera.lastRenderZ = newZ;
        }
    }

    @Override
    public void onDisable() {
        if (mc.player == null) return;

        shouldFreeze = false;

        mc.setCameraEntity(mc.player);

        mc.player.updatePosition(storedPos.x, storedPos.y, storedPos.z);
        mc.player.setYaw(storedYaw);
        mc.player.setPitch(storedPitch);
    }
}
