package dev.smug.seo.gui.impl.components;

import dev.smug.seo.util.input.KeyboardKey;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

public class BindSetting {
    private String name;
    private int keyCode;
    private int x, y;
    private boolean listening;

    public BindSetting(String name, int defaultKey) {
        this.name = name;
        this.keyCode = defaultKey;
    }

    public void render(DrawContext context, int x, int y) {
        this.x = x;
        this.y = y;

        context.fill(x, y, x + 70, y + 20, listening ? 0xFF5555FF : 0xFF8888FF);

        String keyName = listening ? "..." : KeyboardKey.getKeyName(keyCode);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX >= x && mouseX <= x + 70 && mouseY >= y && mouseY <= y + 20) {
            listening = !listening;
            return true;
        }
        return false;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
        listening = false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (listening) {
            if (keyCode != GLFW.GLFW_KEY_ESCAPE) {
                this.keyCode = keyCode;
            }
            listening = false;
            return true;
        }
        return false;
    }

    public int getKeyCode() {
        return keyCode;
    }
}