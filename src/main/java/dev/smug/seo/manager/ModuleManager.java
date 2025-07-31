package dev.smug.seo.manager;

import dev.smug.seo.api.module.Module;
import dev.smug.seo.modules.client.ClickGui;
import dev.smug.seo.util.input.KeybindHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleManager  extends Manager {

    private static final Map<Module, Boolean> keyStates = new HashMap<>();
    private static final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        registerModules();
    }

    private void registerModules() {
        // Client
        modules.add(new ClickGui());
    }

    public static List<Module> getModules() {
        return new ArrayList<>(modules);
    }

    public static void handleKeyBinds() {
        for (Module module : getModules()) {
            int bind = module.getBindSetting().getKeyCode();

            if (bind == 0) continue;

            boolean pressed = KeybindHandler.isKeyPressed(bind);
            boolean wasPressed = keyStates.getOrDefault(module, false);

            if (pressed && !wasPressed) {
                module.toggle();
            }

            keyStates.put(module, pressed);
        }
    }

    public static Module getModuleByName(String name) {
        for (Module module : getModules()) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }

    public static ClickGui getClickGui() {
        Module m = getModuleByName("ClickGui");
        return m instanceof ClickGui ? (ClickGui)m : null;
    }
}