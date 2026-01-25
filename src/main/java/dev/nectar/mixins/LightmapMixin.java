package dev.nectar.mixins;

import dev.nectar.modules.Modules;
import dev.nectar.modules.render.Fullbright;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightmapTextureManager.class)
public class LightmapMixin {
    @Redirect(method = "update(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/SimpleOption;getValue()Ljava/lang/Object;"))
    private Object forceFullbright(SimpleOption<Double> instance) {
        if (Modules.get().isActive(Fullbright.class)) {
            return 1000.0;
        }

        return instance.getValue();
    }
}
