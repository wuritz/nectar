package dev.nectar.modules.setting.settings;

import dev.nectar.modules.setting.Setting;
import net.minecraft.nbt.NbtCompound;

import java.util.function.Consumer;

public class EnumSetting<T extends Enum<?>> extends Setting<T> {

    private final T[] values;

    @SuppressWarnings("unchecked")
    public EnumSetting(String name, String description, T defaultValue, Consumer<T> onChange) {
        super(name, description, defaultValue, onChange);

        values = (T[]) defaultValue.getDeclaringClass().getEnumConstants();
    }

    @Override
    protected boolean isValueValid(T value) {
        return true;
    }

    @Override
    protected T parseImpl(String str) {
        for (T possibleValue : values) {
            if (str.equalsIgnoreCase(possibleValue.toString())) return possibleValue;
        }

        return null;
    }


    @Override
    public NbtCompound save(NbtCompound tag) {
        tag.putString("value", get().toString());

        return tag;
    }

    @Override
    public T load(NbtCompound tag) {
        parse(tag.getString("value", ""));

        return get();
    }


    public static class Builder<T extends Enum<?>> extends SettingBuilder<Builder<T>, T, EnumSetting<T>> {
        public Builder() {
            super(null);
        }

        @Override
        public EnumSetting<T> build() {
            return new EnumSetting<>(name, description, defaultValue, onChange);
        }
    }
}
