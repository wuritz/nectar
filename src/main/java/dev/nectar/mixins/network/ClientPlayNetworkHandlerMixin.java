package dev.nectar.mixins.network;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import dev.nectar.Nectar;
import dev.nectar.events.game.GameJoinedEvent;
import dev.nectar.events.game.GameLeftEvent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void onGameJoinTail(GameJoinS2CPacket packet, CallbackInfo info, @Share("worldNotNull") LocalBooleanRef worldNotNull) {
        if (worldNotNull.get()) {
            Nectar.EVENT_BUS.post(GameLeftEvent.get());
        }

        Nectar.EVENT_BUS.post(GameJoinedEvent.get());
    }

}
