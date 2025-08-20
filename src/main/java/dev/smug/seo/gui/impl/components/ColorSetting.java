package dev.smug.seo.gui.impl.components;

public class ColorSetting extends Settings {
    private int color; // 0xAARRGGBB

    public ColorSetting(String name, int red, int green, int blue, int alpha) {
        super(name);
        this.color = ((alpha & 0xFF) << 24) |
                     ((red & 0xFF) << 16) |
                     ((green & 0xFF) << 8)  |
                     (blue & 0xFF);
    }

    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }

    public int getR() { return (color >> 16) & 0xFF; }
    public int getG() { return (color >> 8) & 0xFF; }
    public int getB() { return color & 0xFF; }
    public int getA() { return (color >> 24) & 0xFF; }

    public void setR(int r) { color = (color & 0xFF00FFFF) | ((r & 0xFF) << 16); }
    public void setG(int g) { color = (color & 0xFFFF00FF) | ((g & 0xFF) << 8); }
    public void setB(int b) { color = (color & 0xFFFFFF00) | (b & 0xFF); }
    public void setA(int a) { color = (color & 0x00FFFFFF) | ((a & 0xFF) << 24); }
    
    public void set(int red, int green, int blue, int alpha) {
        this.color = ((alpha & 0xFF) << 24) |
                     ((red & 0xFF) << 16) |
                     ((green & 0xFF) << 8)  |
                     (blue & 0xFF);
    }

    public void reset() {
        set(255, 255, 255, 255);
    }

    public void negate() {
        set(255 - getR(), 255 - getG(), 255 - getB(), 255 - getA());
    }

    public void toColor(ColorSetting color) {
        set(color.getR(), color.getG(), color.getB(), color.getA());
    }


    public void toGradient(ColorSetting color1, ColorSetting color2, float progress) {
        int r = (int) (color1.getR() + (color2.getR() - color1.getR()) * progress);
        int g = (int) (color1.getG() + (color2.getG() - color1.getG()) * progress);
        int b = (int) (color1.getB() + (color2.getB() - color1.getB()) * progress);
        int a = (int) (color1.getA() + (color2.getA() - color1.getA()) * progress);
        
        set(r, g, b, a);
    }

    public void toRandom() {
        set((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
    }

    public void toRainbow(float progress) {
        float hue = progress * 360;
        float saturation = 1;
        float brightness = 1;
        
        int r = (int) (Math.cos(hue * Math.PI / 180) * 255 * saturation * brightness);
        int g = (int) (Math.sin(hue * Math.PI / 180) * 255 * saturation * brightness);
        int b = (int) (Math.sin((hue + 120) * Math.PI / 180) * 255 * saturation * brightness);
        
        set(r, g, b, 255);
    }
}
