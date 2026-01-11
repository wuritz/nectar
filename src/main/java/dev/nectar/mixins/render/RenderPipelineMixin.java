package dev.nectar.mixins.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import dev.nectar.imixins.IRenderPipeline;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(RenderPipeline.class)
public abstract class RenderPipelineMixin implements IRenderPipeline {

    @Unique
    private boolean lineSmooth;

    @Override
    public void nectar$setLineSmooth(boolean lineSmooth) {
        this.lineSmooth = lineSmooth;
    }

    @Override
    public boolean nectar$getLineSmooth() {
        return lineSmooth;
    }

}
