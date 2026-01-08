/*
 * Copyright (c) 2026. TheBlueStopLight
 * All rights reserved.
 */

package hu.bluestoplight.utils;

import net.minecraft.client.render.LayeringTransform;
import net.minecraft.client.render.OutputTarget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;

public class SednaRenderLayers {
    public static final RenderLayer ESP_DEFAULT_LINES = RenderLayer.of(
            "sedna:esp_lines", RenderSetup.builder(SednaRenderPipelines.ESP_DEFAULT_LINES).layeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING).outputTarget(OutputTarget.ITEM_ENTITY_TARGET).build()
    );

    public static final RenderLayer ESP_QUADS_DEFAULT = RenderLayer.of("sedna:esp_quads", RenderSetup.builder(SednaRenderPipelines.ESP_QUADS).build());
}
