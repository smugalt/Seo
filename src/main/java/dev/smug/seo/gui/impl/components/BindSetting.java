package dev.smug.seo.gui.impl.components;

import dev.smug.seo.util.input.KeyboardKey;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

public class BindSetting extends Settings {
    private int keyCode;
    private int x, y;
    private boolean listening;

    public BindSetting(String name, int defaultKey) {
        super(name);
        this.keyCode = defaultKey;
    }

    public void render(DrawContext context, int x, int y) {
        this.x = x;
        this.y = y;

        context.fill(x, y, x + 70, y + 20, listening ? 0xFF5555FF : 0xFF8888FF);

        String keyName = listening ? "..." : KeyboardKey.getKeyName(keyCode);
        // Best-effort: draw key name centered (requires textRenderer from outer context normally)
        // Here we position a placeholder small rect for text contrast; ClickGUI will label modules separately.
        // If you want actual text here, pass a TextRenderer or provide a callback.
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && mouseX >= x && mouseX <= x + 70 && mouseY >= y && mouseY <= y + 20) {
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