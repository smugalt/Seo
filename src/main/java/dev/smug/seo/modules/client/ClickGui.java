package dev.smug.seo.modules.client;

import dev.smug.seo.Seo;
import dev.smug.seo.api.module.Module;
import dev.smug.seo.manager.ChatManager;
import net.minecraft.util.Formatting;

public class ClickGui extends Module {

    public ClickGui() {
        super("ClickGui", Client, "A custom click gui to manage modules", 344);
    }

    @Override
    public void onEnable() {
        if (mc.player == null) {
            setEnabled(false);
            return;
        }
        mc.setScreen(Seo.ClickGui);
        ChatManager.sendMessage("ClickGui enabled!", Formatting.GRAY);
    }

    @Override
    public void onDisable() {
        ChatManager.sendMessage("ClickGui disabled!", Formatting.RED);
        mc.setScreen(null);
    }
}
