package hu.bluestoplight.modules.render;

import hu.bluestoplight.misc.BlockEntityIterator;
import hu.bluestoplight.modules.Mod;
import hu.bluestoplight.modules.util.settings.NumberSetting;
import hu.bluestoplight.ui.UIUtils;
import hu.bluestoplight.utils.SednaRenderLayers;
import hu.bluestoplight.utils.Utils;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.block.entity.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

import static hu.bluestoplight.SednaClient.mc;

public class StorageESP extends Mod {
    private static boolean shouldRender = false;
    private static NumberSetting range = new NumberSetting("Range", 20.0f, 40.0f, 32.0f, 1.0f);

    public StorageESP() {
        super("StorageESP", "Highlights storage TileEntities with a bounding box.", Category.RENDER);
        addSetting(range);
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

    private static boolean isStorage(BlockEntity be) {
        return be instanceof ChestBlockEntity || be instanceof EnderChestBlockEntity || be instanceof BarrelBlockEntity || be instanceof ShulkerBoxBlockEntity;
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

            for (BlockEntity be : getBlockEntities()) {
                if (!isStorage(be)) continue;

                BlockPos pos = be.getPos();
                Box box = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).shrink(0.05, 0.05, 0.05);
                Color color = new Color(UIUtils.getSelectedPrimaryColor().getRGB());

                UIUtils.espBox(matrixStack, lines, box, color);
            }

            matrixStack.pop();
            consumers.draw();

            lines = consumers.getBuffer(SednaRenderLayers.ESP_DEFAULT_LINES);
            matrixStack.push();
            matrixStack.translate(-cameraPos.getX(), -cameraPos.getY(), -cameraPos.getZ());

            for (BlockEntity be : getBlockEntities()) {
                if (!isStorage(be)) continue;

                BlockPos pos = be.getPos();
                Box box = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).shrink(0.05, 0.05, 0.05);
                Color color = new Color(UIUtils.getSelectedPrimaryColor().getRGB());

                UIUtils.espOutline(matrixStack, lines, box, color.darker());
            }

            matrixStack.pop();
            consumers.draw();
        }
    }

    private static Iterable<BlockEntity> getBlockEntities() {
        return BlockEntityIterator::new;
    }
}
