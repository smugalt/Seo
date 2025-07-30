package dev.smug.seo.util.input;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class KeybindHandler {
    public static boolean isKeyPressed(int keyCode) {
        long window = MinecraftClient.getInstance().getWindow().getHandle();
        if (keyCode >= 0) {
            return GLFW.glfwGetKey(window, keyCode) == GLFW.GLFW_PRESS;
        } else {
            int mouseButton = -keyCode - 1;
            return GLFW.glfwGetMouseButton(window, mouseButton) == GLFW.GLFW_PRESS;
        }
    }
}