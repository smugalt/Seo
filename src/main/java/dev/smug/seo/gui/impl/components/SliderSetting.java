package dev.smug.seo.gui.impl.components;

public class SliderSetting extends Settings {

    private Number value;
    private final Number min;
    private final Number max;
    private final Number step;
    private final boolean isInteger;

    public SliderSetting(String name, double value, double min, double max, double step) {
        super(name);

        this.isInteger = isWhole(value) && isWhole(min) && isWhole(max) && isWhole(step);

        if (isInteger) {
            this.value = (int) value;
            this.min = (int) min;
            this.max = (int) max;
            this.step = (int) step;
        } else {
            this.value = value;
            this.min = min;
            this.max = max;
            this.step = step;
        }
    }

    private boolean isWhole(double d) {
        return d == Math.floor(d) && !Double.isInfinite(d);
    }

    public boolean isInteger() {
        return isInteger;
    }

    public void setValue(double newValue) {
        if (isInteger) {
            int intValue = (int) Math.round(newValue);
            if (intValue < min.intValue()) intValue = min.intValue();
            if (intValue > max.intValue()) intValue = max.intValue();
            this.value = intValue;
        } else {
            double doubleValue = Math.round(newValue / step.doubleValue()) * step.doubleValue();
            if (doubleValue < min.doubleValue()) doubleValue = min.doubleValue();
            if (doubleValue > max.doubleValue()) doubleValue = max.doubleValue();
            this.value = doubleValue;
        }
    }

    public int getValueAsInt() {
        return value.intValue();
    }

    public double getValueAsDouble() {
        return value.doubleValue();
    }

    public Number getValue() {
        return value;
    }

    public Number getMin() {
        return min;
    }

    public Number getMax() {
        return max;
    }

    public Number getStep() {
        return step;
    }
}
