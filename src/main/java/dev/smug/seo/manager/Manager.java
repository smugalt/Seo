package dev.smug.seo.manager;

import dev.smug.seo.api.Globals;

public class Manager implements Globals {

    public static void loadManagers() {
        FileManager fileManager = new FileManager();
        ConfigManager configManager = new ConfigManager();
        ModuleManager moduleManager = new ModuleManager();
        ChatManager chatManager = new ChatManager();
    }
}
