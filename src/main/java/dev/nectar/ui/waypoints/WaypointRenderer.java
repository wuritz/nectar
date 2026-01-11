package dev.nectar.ui.waypoints;

import dev.nectar.Nectar;
import dev.nectar.ui.UIUtils;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class WaypointRenderer {
    private static float[] getColor(int color) {
        float a = ((color >> 24) & 0xFF) / 255.0f;
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;

        return new float[]{r, g, b, a};
    }

    public static void register(WaypointManager waypointManager) {
        WorldRenderEvents.AFTER_ENTITIES.register(worldRenderContext -> {
            if (Nectar.mc.player == null) return;

            for (Waypoint waypoint : waypointManager.getCurrentWorldWaypoints()) {
                String currentDimension = Nectar.mc.player.getEntityWorld().getRegistryKey().getValue().toString();
                if (!waypoint.dimension.equals(currentDimension)) continue;

                renderWaypoint(worldRenderContext, waypoint);
            }
        });
    }

    private static void renderWaypoint(WorldRenderContext context, Waypoint waypoint) {
        Camera camera = context.gameRenderer().getCamera();

        Vec3d targetPos = new Vec3d(waypoint.x, waypoint.y, waypoint.z);
        Vec3d cameraPos = camera.getCameraPos();

        double distance = cameraPos.distanceTo(targetPos);

        MatrixStack matrixStack = context.matrices();
        matrixStack.push();

        matrixStack.translate(waypoint.x - cameraPos.x, (waypoint.y + 1.25d) - cameraPos.y, waypoint.z - cameraPos.z);
        matrixStack.multiply(camera.getRotation());

        float scale = 0.025f + (float) (distance * 0.003f);
        matrixStack.scale(-scale, -scale, scale);

        VertexConsumerProvider consumers = context.consumers();

        VertexConsumer buffer = consumers.getBuffer(RenderLayers.textBackgroundSeeThrough());
        Matrix4f positionMatrix = matrixStack.peek().getPositionMatrix();

        float[] rgba = getColor(waypoint.color);
        float size = 5.0f;
        float offset = -10.0f;

        buffer.vertex(positionMatrix, -size, offset + size, 0).color(rgba[0], rgba[1], rgba[2], 1.0f).light(UIUtils.light);
        buffer.vertex(positionMatrix, -size, offset - size, 0).color(rgba[0], rgba[1], rgba[2], 1.0f).light(UIUtils.light);
        buffer.vertex(positionMatrix, size, offset - size, 0).color(rgba[0], rgba[1], rgba[2], 1.0f).light(UIUtils.light);
        buffer.vertex(positionMatrix, size, offset + size, 0).color(rgba[0], rgba[1], rgba[2], 1.0f).light(UIUtils.light);

        matrixStack.pop();
    }
}
