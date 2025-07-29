package dev.smug.seo.api.module;

import dev.smug.seo.gui.impl.components.BindSetting;
import net.minecraft.client.MinecraftClient;

public class Module extends Category {

    public static final ModuleCategory Client = ModuleCategory.Client;
    public static final ModuleCategory Combat = ModuleCategory.Combat;
    public static final ModuleCategory Render = ModuleCategory.Render;
    public static final ModuleCategory World = ModuleCategory.World;
    public static final ModuleCategory Misc = ModuleCategory.Misc;
    public static final ModuleCategory Dev = ModuleCategory.Dev;

    protected static final MinecraftClient mc = MinecraftClient.getInstance();

    private final String name;
    private boolean enabled;
    private final ModuleCategory category;
    private final String description;
    private int keyCode;
    private final BindSetting bindSetting;

    public Module(String name, int key, ModuleCategory category, String description) {
        this.name = name;
        this.enabled = false;
        this.category = category;
        this.description = description;
        this.keyCode = key;
        this.bindSetting = new BindSetting("Keybind", key);
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

    // Replaced KeyBinding with custom bind handling
    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public BindSetting getBindSetting() {
        return bindSetting;
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