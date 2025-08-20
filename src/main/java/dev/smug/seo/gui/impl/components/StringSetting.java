package dev.smug.seo.gui.impl.components;

public class StringSetting extends Settings {

    private String text;

    public StringSetting(String name, String text) {
        super(name);
        set(text);
    }

    public String get() {
        return text;
    }

    public void set(String text) {
        if (text == null || text.trim().isEmpty()) {
            this.text = "";
        } else {
            this.text = text;
        }
    }

    public void reset() {
        set("");
    }

    public boolean isEmpty() {
        return text.isEmpty();
    }

    public boolean isNotEmpty() {
        return !text.isEmpty();
    }

    public String placeholder() {
        return "lorem ipsum";
    }
}
