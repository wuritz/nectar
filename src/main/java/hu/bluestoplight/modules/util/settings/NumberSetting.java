package hu.bluestoplight.modules.util.settings;

import net.minecraft.nbt.NbtCompound;

public class NumberSetting extends Setting<Double> {
    private double minimum, maximum, increment;

    public NumberSetting(String name, double minimum, double maximum, double defaultValue, double increment) {
        super(name, defaultValue);

        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
    }

    public void increment(boolean positive) {
        if (positive) {
            setValue(getValue() + getIncrement());
        } else {
            setValue(getValue() - getIncrement());
        }
    }

    public static double clamp(double value, double minimum, double maximum) {
        value = Math.max(minimum, value);
        value = Math.min(maximum, value);

        return value;
    }

    public double getValue() {
        return value;
    }

    public float getFloatValue() {
        return (float) getValue();
    }

    public int getIntValue() {
        return (int) getValue();
    }

    public double getIncrement() {
        return increment;
    }

    public void setValue(double value) {
        value = clamp(value, this.minimum, this.maximum);
        value = Math.round(value * (1.0 / this.increment)) / (1.0 / this.increment);
        this.value = value;
    }

    public double getMinimum() {
        return minimum;
    }

    public double getMaximum() {
        return maximum;
    }

    @Override
    protected NbtCompound save(NbtCompound tag) {
        tag.putDouble("value", getValue());

        return tag;
    }

    @Override
    public Double load(NbtCompound tag) {
        setValue(tag.getDouble("value", value));

        return getValue();
    }
}