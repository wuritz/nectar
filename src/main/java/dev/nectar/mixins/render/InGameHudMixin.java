package dev.nectar.mixins.render;

import dev.nectar.Nectar;
import dev.nectar.events.core.render.Render2DEvent;
import dev.nectar.utils.Utils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.profiler.Profilers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        context.createNewRootLayer();

        Profilers.get().push(Nectar.MOD_ID + "_render_2d");

        Utils.unscaledProjection();

        Nectar.EVENT_BUS.post(Render2DEvent.get(context, context.getScaledWindowWidth(), context.getScaledWindowWidth(), tickCounter.getTickProgress(true)));

        context.createNewRootLayer();
        Utils.scaledProjection();

        Profilers.get().pop();
    }

}
