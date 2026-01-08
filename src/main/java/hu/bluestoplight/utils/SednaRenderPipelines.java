/*
 * Copyright (c) 2026. TheBlueStopLight
 * All rights reserved.
 */

package hu.bluestoplight.utils;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class SednaRenderPipelines {
    public static final RenderPipeline.Snippet FOGLESS_LINES_SNIPPET = RenderPipeline.builder(RenderPipelines.FOG_SNIPPET,
            RenderPipelines.RENDERTYPE_LINES_SNIPPET)
            .withVertexShader(Identifier.tryParse("sedna:core/fogless_lines")).withFragmentShader(Identifier.tryParse("sedna:core/fogless_lines"))
            .withBlend(BlendFunction.TRANSLUCENT).withCull(false)
            .withVertexFormat(VertexFormats.POSITION_COLOR_NORMAL_LINE_WIDTH, VertexFormat.DrawMode.LINES)
            .buildSnippet();

    public static RenderPipeline ESP_DEFAULT_LINES = RenderPipelines.register(RenderPipeline.builder(FOGLESS_LINES_SNIPPET).withLocation(Identifier.tryParse("sedna:pipeline/sedna_esp_default_lines")).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).build());

    public static final RenderPipeline ESP_QUADS = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
            .withLocation(Identifier.tryParse("sedna:pipeline/sedna_esp_quads"))
            .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS)
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .withDepthWrite(false)
            .withCull(false)
            .build()
    );
}
