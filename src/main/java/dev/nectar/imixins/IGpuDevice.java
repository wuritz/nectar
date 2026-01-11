package dev.nectar.imixins;

import com.mojang.blaze3d.systems.RenderPass;

public interface IGpuDevice {
    /**
     * Currently there can only be a single scissor pushed at once.
     */
    void nectar$pushScissor(int x, int y, int width, int height);

    void nectar$popScissor();

    /**
     * This is an *INTERNAL* method, it shouldn't be called.
     */
    @Deprecated
    @SuppressWarnings("DeprecatedIsStillUsed")
    void nectar$onCreateRenderPass(RenderPass pass);
}
