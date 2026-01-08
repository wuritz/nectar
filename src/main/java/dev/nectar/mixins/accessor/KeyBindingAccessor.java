package dev.nectar.mixins.accessor;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(KeyBinding.class)
public interface KeyBindingAccessor {

    @Accessor("KEYS_BY_ID")
    static Map<String, KeyBinding> getKeysById() { return null; }

    @Accessor("boundKey")
    InputUtil.Key sedna$getKey();

    @Accessor("timesPressed")
    int sedna$getTimesPressed();

    @Accessor("timesPressed")
    void sedna$setTimesPressed(int timesPressed);

    @Invoker("reset")
    void sedna$invokeReset();

}
