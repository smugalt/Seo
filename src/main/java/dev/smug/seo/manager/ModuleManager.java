package dev.smug.seo.manager;

import dev.smug.seo.modules.client.ClickGui;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private static final List<ClickGui> modules = new ArrayList<>();

    public ModuleManager() {
        registerModules();
    }

    private void registerModules() {
        // Client
        modules.add(new ClickGui());
    }

    public static List<ClickGui> getModules() {
        return modules;
    }
}
