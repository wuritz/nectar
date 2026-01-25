package dev.nectar.modules.setting.settings;

import dev.nectar.modules.setting.Setting;
import net.minecraft.nbt.NbtCompound;

import java.util.function.Consumer;

public class IntSetting extends Setting<Integer> {

    public final int min, max;

    public IntSetting(String name, String description, Integer defaultValue, Consumer<Integer> onChange, int min, int max) {
        super(name, description, defaultValue, onChange);

        this.min = min;
        this.max = max;
    }

    @Override
    protected boolean isValueValid(Integer value) {
        return value >= min && value <= max;
    }

    @Override
    protected Integer parseImpl(String str) {
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    @Override
    public NbtCompound save(NbtCompound tag) {
        tag.putInt("value", get());

        return tag;
    }

    @Override
    public Integer load(NbtCompound tag) {
        set(tag.getInt("value", 0));

        return get();
    }

    public static class Builder extends SettingBuilder<Builder, Integer, IntSetting> {
        private int min = Integer.MIN_VALUE, max = Integer.MAX_VALUE;

        public Builder() {
            super(0);
        }

        public Builder min(int min) {
            this.min = min;
            return this;
        }

        public Builder max(int max) {
            this.max = max;
            return this;
        }

        public Builder range(int min, int max) {
            this.min = Math.min(min, max);
            this.max = Math.max(min, max);
            return this;
        }

        @Override
        public IntSetting build() {
            return new IntSetting(name, description, defaultValue, onChange, min, max);
        }
    }
}
