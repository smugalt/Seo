package dev.smug.seo.gui;

import dev.smug.seo.Seo;
import dev.smug.seo.api.module.Category;
import dev.smug.seo.api.module.Module;
import dev.smug.seo.gui.impl.components.BindSetting;
import dev.smug.seo.gui.impl.components.BooleanSetting;
import dev.smug.seo.gui.impl.components.ColorSetting;
import dev.smug.seo.gui.impl.components.Settings;
import dev.smug.seo.gui.impl.components.SliderSetting;
import dev.smug.seo.gui.impl.components.StringSetting;
import dev.smug.seo.manager.ModuleManager;
import dev.smug.seo.modules.client.ClickGui;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SeoClickGui extends Screen {

    private static final int Width = 60;
    private static final int Height = 12;
    private static final int Padding = 2;
    private static final int Button_Width = 60;
    private static final int Button_Height = 12;
    private static final int Radius = 5;

    private final Map<Category.ModuleCategory, Boolean> expanded = new EnumMap<>(Category.ModuleCategory.class);
    private final Map<Module, int[]> moduleButtonMap = new HashMap<>();
    private final Map<Settings, int[]> settingBounds = new HashMap<>();

    // UI state
    private SliderSetting draggingSlider = null;
    private ColorSetting openColorPicker = null; // which color is expanded

    public SeoClickGui() {
        super(Text.literal(Seo.VERSION + "(" + Seo.Build + ")" + " - click-gui"));
        for (Category.ModuleCategory cat : Category.ModuleCategory.values()) {
            expanded.put(cat, false);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        moduleButtonMap.clear();
        settingBounds.clear();

        int x = Padding;
        for (Category.ModuleCategory category : Category.ModuleCategory.values()) {
            drawRoundedFrame(context, category, x, Padding, mouseX, mouseY);
            x += Width + Padding;
        }

        // Render color picker popup if any
        if (openColorPicker != null) {
            renderColorPicker(context, openColorPicker, mouseX, mouseY);
        }
    }

    private void drawRoundedFrame(DrawContext context, Category.ModuleCategory category, int x, int y, int mouseX, int mouseY) {
        int[] f = getXandY(x, y);
        drawSquare(context, f[0], f[1] + Button_Height, f[2], f[3]);

        drawButton(context, category.name(), f[0] + Radius, f[1] + (Button_Height - 10) / 2, Button_Width, Button_Height, colorForCategory(category));

        if (isHovering(f[0], f[1], Button_Width, Button_Height, mouseX, mouseY)) {
            if (Objects.requireNonNull(this.client).mouse.wasLeftButtonClicked()) {
                expanded.put(category, !expanded.get(category));
            }
        }

        if (expanded.get(category)) {
            int by = f[1] + Button_Height + Padding;
            for (var module : ModuleManager.getModules()) {
                if (module.getCategory() == category) {
                    int moduleX = f[0] + Radius;
                    int moduleY = by;
                    drawButton(context, module.getName(), moduleX, moduleY, Width, Button_Height, 0xFFCCCCCC);

                    moduleButtonMap.put(module, new int[]{moduleX, moduleY, Width, Button_Height});

                    BindSetting bind = module.getBindSetting();
                    bind.render(context, moduleX + Width + 5, moduleY);

                    by += Button_Height + Padding;

                    // Render module settings stack under the module button
                    List<Settings> settings = safeSettings(module);
                    for (Settings setting : settings) {
                        int sx = moduleX + 6;
                        int sy = by;
                        int sw = Width - 12;
                        int sh = 14;
                        if (setting instanceof BooleanSetting b) {
                            drawBoolean(context, b, sx, sy, sw, sh, mouseX, mouseY);
                            settingBounds.put(setting, new int[]{sx, sy, sw, sh});
                            by += sh + Padding;
                        } else if (setting instanceof SliderSetting s) {
                            drawSlider(context, s, sx, sy, sw, sh, mouseX, mouseY);
                            settingBounds.put(setting, new int[]{sx, sy, sw, sh});
                            by += sh + Padding;
                        } else if (setting instanceof StringSetting s) {
                            drawString(context, s, sx, sy, sw, sh, mouseX, mouseY);
                            settingBounds.put(setting, new int[]{sx, sy, sw, sh});
                            by += sh + Padding;
                        } else if (setting instanceof ColorSetting c) {
                            drawColor(context, c, sx, sy, sw, sh, mouseX, mouseY);
                            settingBounds.put(setting, new int[]{sx, sy, sw, sh});
                            by += sh + Padding;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Close color picker when clicking outside
        if (openColorPicker != null) {
            if (!isHovering(colorPickerX(), colorPickerY(), 140, 90, (int) mouseX, (int) mouseY)) {
                openColorPicker = null;
            }
        }

        for (Module module : ModuleManager.getModules()) {
            BindSetting bind = module.getBindSetting();
            if (bind.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }

        for (Map.Entry<Module, int[]> entry : moduleButtonMap.entrySet()) {
            int[] pos = entry.getValue();
            if (isHovering(pos[0], pos[1], pos[2], pos[3], (int) mouseX, (int) mouseY)) {
                entry.getKey().toggle();
                return true;
            }
        }

        int x = Padding;
        for (Category.ModuleCategory category : Category.ModuleCategory.values()) {
            int[] f = getXandY(x, Padding);
            if (isHovering(f[0], f[1], Button_Width, Button_Height, (int) mouseX, (int) mouseY)) {
                expanded.put(category, !expanded.get(category));
                return true;
            }
            x += Width + Padding;
        }

        // Settings interactions
        for (Map.Entry<Settings, int[]> e : settingBounds.entrySet()) {
            int[] b = e.getValue();
            if (isHovering(b[0], b[1], b[2], b[3], (int) mouseX, (int) mouseY)) {
                Settings s = e.getKey();
                if (s instanceof BooleanSetting bs && button == 0) {
                    bs.toggle();
                    return true;
                } else if (s instanceof SliderSetting ss && button == 0) {
                    draggingSlider = ss;
                    updateSliderFromMouse(ss, mouseX, b[0], b[2]);
                    return true;
                } else if (s instanceof ColorSetting cs && button == 0) {
                    openColorPicker = cs;
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (Module module : ModuleManager.getModules()) {
            if (module.getBindSetting().keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }

        if (keyCode == getClickGuiModule().getBindSetting().getKeyCode()) {
            this.close();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && draggingSlider != null) {
            draggingSlider = null;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingSlider != null && button == 0) {
            int[] b = settingBounds.get(draggingSlider);
            if (b != null) updateSliderFromMouse(draggingSlider, mouseX, b[0], b[2]);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        // Scroll on hovered slider to nudge value (use vertical scroll)
        for (Map.Entry<Settings, int[]> e : settingBounds.entrySet()) {
            int[] b = e.getValue();
            if (isHovering(b[0], b[1], b[2], b[3], (int) mouseX, (int) mouseY) && e.getKey() instanceof SliderSetting ss) {
                double step = ss.getStep().doubleValue();
                if (step <= 0) step = ss.isInteger() ? 1 : 0.1;
                double sign = verticalAmount > 0 ? 1 : -1;
                ss.setValue(ss.getValue().doubleValue() + sign * step);
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    public void close() {
        if (this.client != null) {
            this.client.setScreen(null);
        }
    }

    public ClickGui getClickGuiModule() {
        return ModuleManager.getClickGui();
    }

    private void drawSquare(DrawContext ctx, int x1, int y1, int x2, int y2) {
        int bodyColor = 0xFF222222;
        ctx.fill(x1, y1, x2, y2, bodyColor);
    }

    private void drawButton(DrawContext ctx, String label, int x, int y, int width, int height, int bgColor) {
        ctx.fill(x, y, x + width, y + height, bgColor);
        int textX = x + width / 2;
        int textY = y + (height - 8) / 2;
        ctx.drawCenteredTextWithShadow(this.textRenderer, Text.literal(label), textX, textY, 0xFFFFFFFF);
    }

    private int[] getXandY(int x, int y) {
        return new int[]{x, y, x + Width, y + Height};
    }

    private boolean isHovering(int x, int y, int w, int h, int mx, int my) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }

    private int colorForCategory(Category.ModuleCategory cat) {
        return switch (cat) {
            case Combat -> 0xFFFF5555;
            case Movement -> 0xFF55FFFF;
            case Render -> 0xFFFFAA00;
            case World -> 0xFF55FF55;
            case Misc -> 0xFFFFFF55;
            case Dev -> 0xFFAAAAAA;
            default -> 0xFFFF00FF;
        };
    }

    // Helpers for settings rendering
    private List<Settings> safeSettings(Module m) {
        List<Settings> list = m.getSettings();
        return list == null ? new ArrayList<>() : list;
    }

    private void drawBoolean(DrawContext ctx, BooleanSetting b, int x, int y, int w, int h, int mx, int my) {
        int bg = 0xFF333333;
        ctx.fill(x, y, x + w, y + h, bg);
        int box = h - 4;
        int bx = x + 4;
        int by = y + 2;
        int fill = b.get() ? 0xFF55FF55 : 0xFF555555;
        ctx.fill(bx, by, bx + box, by + box, fill);
        ctx.drawTextWithShadow(this.textRenderer, Text.literal(b.getName() + ": " + (b.get() ? "ON" : "OFF")), bx + box + 6, y + 3, 0xFFFFFFFF);
    }

    private void drawSlider(DrawContext ctx, SliderSetting s, int x, int y, int w, int h, int mx, int my) {
        int bg = 0xFF333333;
        ctx.fill(x, y, x + w, y + h, bg);
        int trackX = x + 6;
        int trackY = y + h / 2 - 2;
        int trackW = w - 12;
        ctx.fill(trackX, trackY, trackX + trackW, trackY + 4, 0xFF555555);

        double min = s.getMin().doubleValue();
        double max = s.getMax().doubleValue();
        double val = s.getValue().doubleValue();
        double ratio = max == min ? 0 : (val - min) / (max - min);
        ratio = Math.max(0, Math.min(1, ratio));
        int knobX = (int) (trackX + ratio * trackW);
        ctx.fill(knobX - 3, y + 2, knobX + 3, y + h - 2, draggingSlider == s ? 0xFF8888FF : 0xFFAAAAAA);

        String label = String.format("%s: %s", s.getName(), s.isInteger() ? Integer.toString(s.getValue().intValue()) : Double.toString(s.getValue().doubleValue()));
        ctx.drawTextWithShadow(this.textRenderer, Text.literal(label), x + 6, y + 2, 0xFFFFFFFF);
    }

    private void updateSliderFromMouse(SliderSetting s, double mouseX, int x, int w) {
        int trackX = x + 6;
        int trackW = w - 12;
        double px = Math.max(trackX, Math.min(trackX + trackW, mouseX));
        double t = (px - trackX) / trackW;
        double min = s.getMin().doubleValue();
        double max = s.getMax().doubleValue();
        double newVal = min + t * (max - min);
        s.setValue(newVal);
    }

    private void drawString(DrawContext ctx, StringSetting s, int x, int y, int w, int h, int mx, int my) {
        int bg = 0xFF333333;
        ctx.fill(x, y, x + w, y + h, bg);
        String text = s.get();
        ctx.drawTextWithShadow(this.textRenderer, Text.literal(text.isEmpty() ? s.placeholder() : text), x + 6, y + 3, text.isEmpty() ? 0x55FFFFFF : 0xFFFFFFFF);
    }

    private void drawColor(DrawContext ctx, ColorSetting c, int x, int y, int w, int h, int mx, int my) {
        int bg = 0xFF333333;
        ctx.fill(x, y, x + w, y + h, bg);
        int sw = h - 4;
        int sx = x + 4;
        int sy = y + 2;
        int col = c.getColor();
        ctx.fill(sx, sy, sx + sw, sy + sw, col);
        ctx.drawTextWithShadow(this.textRenderer, Text.literal("Color"), sx + sw + 6, y + 3, 0xFFFFFFFF);
    }

    // Simple color picker popup at fixed position
    private int colorPickerX() {
        return 20;
    }

    private int colorPickerY() {
        return 40;
    }

    private void renderColorPicker(DrawContext ctx, ColorSetting c, int mouseX, int mouseY) {
        int px = colorPickerX();
        int py = colorPickerY();
        int pw = 140;
        int ph = 90;
        ctx.fill(px, py, px + pw, py + ph, 0xCC000000);

        // Draw 4 bars for RGBA
        int barX = px + 8;
        int barW = pw - 16;
        int barH = 12;
        int gy = py + 8;

        int[] vals = new int[]{c.getR(), c.getG(), c.getB(), c.getA()};
        String[] names = new String[]{"R", "G", "B", "A"};
        for (int i = 0; i < 4; i++) {
            int by = gy + i * (barH + 6);
            ctx.fill(barX, by, barX + barW, by + barH, 0xFF444444);
            int knobX = barX + (int) (vals[i] / 255.0 * barW);
            ctx.fill(knobX - 2, by - 2, knobX + 2, by + barH + 2, 0xFFAAAAAA);
            ctx.drawTextWithShadow(this.textRenderer, Text.literal(names[i] + ": " + vals[i]), barX, by - 10, 0xFFFFFFFF);
        }
    }
}