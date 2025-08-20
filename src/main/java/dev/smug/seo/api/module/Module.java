package dev.smug.seo.api.module;

import dev.smug.seo.gui.impl.components.BindSetting;
import dev.smug.seo.gui.impl.components.Settings;

public class Module extends Category {

    public static final ModuleCategory Client = ModuleCategory.Client;
    public static final ModuleCategory Combat = ModuleCategory.Combat;
    public static final ModuleCategory Render = ModuleCategory.Render;
    public static final ModuleCategory World = ModuleCategory.World;
    public static final ModuleCategory Misc = ModuleCategory.Misc;
    public static final ModuleCategory Dev = ModuleCategory.Dev;


    private final String name;
    private boolean enabled;
    private final ModuleCategory category;
    private final String description;
    private final BindSetting bindSetting;
    private final java.util.List<Settings> settings = new java.util.ArrayList<>();

    public Module(String name, ModuleCategory category, String description) {
        this(name, category, description, -1);
    }

    public Module(String name, ModuleCategory category, String description, int defaultKey) {
        this.name = name;
        this.enabled = false;
        this.category = category;
        this.description = description;
        this.bindSetting = new BindSetting("Bind", defaultKey);
    }

    public ModuleCategory getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getDescription() {
        return description;
    }

    public BindSetting getBindSetting() {
        return bindSetting;
    }

    public java.util.List<Settings> getSettings() {
        return java.util.Collections.unmodifiableList(settings);
    }

    protected void addSetting(Settings setting) {
        if (setting != null) settings.add(setting);
    }

    public void toggle() {
        this.enabled = !this.enabled;
        if (this.enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    protected void onEnable() {
    }

    protected void onDisable() {
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            if (enabled) {
                onEnable();
            } else {
                onDisable();
            }
        }
    }
}