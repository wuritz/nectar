package dev.nectar.mixins;

import dev.nectar.modules.player.FreeCam;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class TickMovementMixin {
    @Inject(method = "tickMovement", at = @At("HEAD"), cancellable = true)
    private void cancelMovement(CallbackInfo ci) {
        if (FreeCam.shouldFreeze) {
            ci.cancel();
        }
    }
}
