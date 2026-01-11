package dev.nectar.utils;

import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.nectar.Nectar;
import dev.nectar.ui.screens.clickgui.ClickGUI;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.render.ProjectionMatrix2;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import static dev.nectar.Nectar.mc;

public class Utils {

    public static boolean rendering3D = true;
    private static final ProjectionMatrix2 matrix = new ProjectionMatrix2("nectar-projection-matrix", -10, 100, true);

    public static boolean canUpdate() {
        return mc != null && mc.world != null && mc.player != null;
    }

    public static boolean canOpenGui() {
        if (canUpdate()) return mc.currentScreen == null;

        return mc.currentScreen instanceof TitleScreen || mc.currentScreen instanceof MultiplayerScreen || mc.currentScreen instanceof SelectWorldScreen;
    }

    public static boolean canCloseGui() {
        return mc.currentScreen instanceof ClickGUI;
    }

    public static boolean isToggleable() {
        if (mc.player == null || mc.world == null) {
            return false;
        } else {
            return true;
        }
    }

    public static void unscaledProjection() {
        float width = mc.getWindow().getFramebufferWidth();
        float height = mc.getWindow().getFramebufferHeight();

        RenderSystem.setProjectionMatrix(matrix.set(width, height), ProjectionType.ORTHOGRAPHIC);
        RenderUtils.projection.set(((ProjectionMatrix2Accessor) matrix).meteor$callGetMatrix(width, height));

        rendering3D = false;
    }

    public static void scaledProjection() {
        float width = (float) (mc.getWindow().getFramebufferWidth() / mc.getWindow().getScaleFactor());
        float height = (float) (mc.getWindow().getFramebufferHeight() / mc.getWindow().getScaleFactor());

        RenderSystem.setProjectionMatrix(matrix.set(width, height), ProjectionType.PERSPECTIVE);
        RenderUtils.projection.set(((ProjectionMatrix2Accessor) matrix).meteor$callGetMatrix(width, height));

        rendering3D = true;
    }

    public static byte[] readBytes(InputStream in) {
        try {
            return in.readAllBytes();
        } catch (IOException e) {
            Nectar.LOG.error("Error reading from stream.", e);
            return new byte[0];
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

}
