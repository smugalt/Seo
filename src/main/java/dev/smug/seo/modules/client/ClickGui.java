package dev.smug.seo.modules.client;

import dev.smug.seo.Seo;
import dev.smug.seo.api.module.Module;
import org.lwjgl.glfw.GLFW;

public class ClickGui extends Module {

    public ClickGui() {
        super("ClickGui", GLFW.GLFW_KEY_RIGHT_SHIFT, Client, "A custom click gui to manage modules");
    }

    @Override
    public void onEnable() {
        if (mc.player == null) {
            setEnabled(false);
            return;
        }

        mc.setScreen(Seo.ClickGui);
    }

    @Override
    public void onDisable() {
        mc.setScreen(null);
    }
}
