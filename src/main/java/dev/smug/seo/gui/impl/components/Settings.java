package dev.smug.seo.gui.impl.components;


public class Settings {

    protected String name;

    public Settings(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double double_1, double double_2) {
        return false;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double double_1) {
        return false;
    }

    public boolean mouseMoved(double mouseX, double mouseY) {
        return false;
    }

    public boolean mouseWheel(double mouseX, double mouseY, double double_1) {
        return false;
    }
    
}
