package dev.smug.seo.manager;

public class Manager {

    public static void loadManagers() {
        FileManager fileManager = new FileManager();
        ConfigManager configManager = new ConfigManager();
        ModuleManager moduleManager = new ModuleManager();
    }
}
