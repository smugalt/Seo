package dev.smug.seo.gui.impl.components;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class EnumSetting<E> extends Settings {
    private final List<E> values;
    private int index;

    public EnumSetting(String name, Class<E> enumClass) {
        super(name);
        this.values = Arrays.asList(enumClass.getEnumConstants());
        if (this.values.isEmpty()) {
            throw new IllegalArgumentException("Enum must have at least one value.");
        }
        this.index = 0;
    }

    @SafeVarargs
    public EnumSetting(String name, E... values) {
        super(name);
        if (values.length == 0) {
            throw new IllegalArgumentException("EnumSetting must have at least one value.");
        }
        this.values = Arrays.asList(values);
        this.index = 0;
    }

    public static EnumSetting<String> of(String name, String... values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("EnumSetting must have at least one value.");
        }

        List<String> capitalizedValues = new ArrayList<>();
        for (String value : values) {
            if (value == null || value.isEmpty()) {
                capitalizedValues.add(value);
            } else {
                String capitalized = value.substring(0, 1).toUpperCase() +
                        (value.length() > 1 ? value.substring(1).toLowerCase() : "");
                capitalizedValues.add(capitalized);
            }
        }

        return new EnumSetting<>(name, capitalizedValues);
    }

    private EnumSetting(String name, List<E> values) {
        super(name);
        this.values = values;
        this.index = 0;
    }

    public void setIndex(int index) {
        if (index < 0 || index >= values.size()) {
            throw new IndexOutOfBoundsException("Invalid index for EnumSetting");
        }
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public E get() {
        return values.get(index);
    }

    public void set(E value) {
        int newIndex = values.indexOf(value);
        if (newIndex == -1) {
            throw new IllegalArgumentException("Value not part of this EnumSetting");
        }
        this.index = newIndex;
    }

    public void setIgnoreCase(String value) {
        if (values.isEmpty() || !(values.get(0) instanceof String)) {
            throw new UnsupportedOperationException("setIgnoreCase only works with String-based EnumSettings");
        }

        for (int i = 0; i < values.size(); i++) {
            if (((String) values.get(i)).equalsIgnoreCase(value)) {
                this.index = i;
                return;
            }
        }
        throw new IllegalArgumentException("Value not part of this EnumSetting (case-insensitive)");
    }

    public boolean equalsIgnoreCase(String value) {
        if (values.isEmpty() || !(values.getFirst() instanceof String)) {
            return false;
        }
        return ((String) get()).equalsIgnoreCase(value);
    }

    public void previous() {
        index = (index + values.size() - 1) % values.size();
    }

    public void next() {
        index = (index + 1) % values.size();
    }

    public List<E> getValues() {
        return values;
    }
}