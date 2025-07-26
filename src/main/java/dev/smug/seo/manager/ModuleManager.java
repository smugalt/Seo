package dev.smug.seo.manager;

import dev.smug.seo.modules.client.ClickGui;

public class ModuleManager {
    public ModuleManager() {
        registerModules();
    }
    public static void registerModules() {
        ClickGui clickGui = new ClickGui();
    }
}
