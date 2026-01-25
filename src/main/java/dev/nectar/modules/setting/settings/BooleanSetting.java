package dev.nectar.modules.setting.settings;

import dev.nectar.modules.setting.Setting;
import net.minecraft.nbt.NbtCompound;

import java.util.function.Consumer;

public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(String name, String description, Boolean defaultValue, Consumer<Boolean> onChange) {
        super(name, description, defaultValue, onChange);
    }

    @Override
    protected Boolean parseImpl(String str) {
        if (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("1")) return true;
        else if (str.equalsIgnoreCase("false") || str.equalsIgnoreCase("0")) return false;
        else if (str.equalsIgnoreCase("toggle")) return !get();
        return null;
    }

    @Override
    protected boolean isValueValid(Boolean value) {
        return true;
    }

    @Override
    public NbtCompound save(NbtCompound tag) {
        tag.putBoolean("value", get());

        return tag;
    }

    @Override
    public Boolean load(NbtCompound tag) {
        set(tag.getBoolean("value", false));

        return get();
    }

    public static class Builder extends SettingBuilder<Builder, Boolean, BooleanSetting> {
        public Builder() {
            super(false);
        }

        @Override
        public BooleanSetting build() {
            return new BooleanSetting(name, description, defaultValue, onChange);
        }
    }

}