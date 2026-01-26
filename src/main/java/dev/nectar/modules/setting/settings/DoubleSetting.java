package dev.nectar.modules.setting.settings;

import dev.nectar.modules.setting.Setting;
import net.minecraft.nbt.NbtCompound;

import java.util.function.Consumer;

public class DoubleSetting extends Setting<Double> {

    public final double min, max;

    public DoubleSetting(String name, String description, Double defaultValue, Consumer<Double> onChange, double min, double max) {
        super(name, description, defaultValue, onChange);

        this.min = min;
        this.max = max;
    }

    @Override
    protected boolean isValueValid(Double value) {
        return value >= min && value <= max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    @Override
    protected Double parseImpl(String str) {
        try {
            return Double.parseDouble(str.trim());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    @Override
    protected NbtCompound save(NbtCompound tag) {
        tag.putDouble("value", get());

        return tag;
    }

    @Override
    public Double load(NbtCompound tag) {
        set(tag.getDouble("value", 0.0));

        return get();
    }

    public static class Builder extends SettingBuilder<Builder, Double, DoubleSetting> {
        public double min = Double.NEGATIVE_INFINITY, max = Double.POSITIVE_INFINITY;

        public Builder() {
            super(0D);
        }

        public Builder min(double min) {
            this.min = min;
            return this;
        }

        public Builder max(double max) {
            this.max = max;
            return this;
        }

        public Builder range(double min, double max) {
            this.min = Math.min(min, max);
            this.max = Math.max(min, max);
            return this;
        }

        public DoubleSetting build() {
            return new DoubleSetting(name, description, defaultValue, onChange, min, max);
        }
    }
}
