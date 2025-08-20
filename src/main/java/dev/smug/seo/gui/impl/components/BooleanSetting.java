package dev.smug.seo.gui.impl.components;


public class BooleanSetting extends Settings {

    public boolean on;

    public BooleanSetting(String name, boolean on) {
        super(name);
        this.on = on;
    }

    public void toggle() {
        on = !on;
    }

    public void invert() {
       toggle();
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public void enable() {
        this.on = true;
    }

    public void disable() {
        this.on = false;
    }

    public boolean get() {
        return on;
    }

}
