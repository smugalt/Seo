package dev.smug.seo.gui;

import dev.smug.seo.Seo;
import dev.smug.seo.api.module.Category;
import dev.smug.seo.api.module.Module;
import dev.smug.seo.gui.impl.components.BindSetting;
import dev.smug.seo.manager.ModuleManager;
import dev.smug.seo.modules.client.ClickGui;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SeoClickGui extends Screen {

    private static final int Padding = 10;
    private static final int Radius = 8;
    private static final int Frame_Width = 80;
    private static final int Frame_Height = 150;
    private static final int Button_Width = 20;
    private static final int Button_Height = 20;
    private static final int Module_Button_Width = 20;

    private final Map<Category.ModuleCategory, Boolean> expanded = new EnumMap<>(Category.ModuleCategory.class);
    private final Map<Module, int[]> moduleButtonMap = new HashMap<>();

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

        int x = Padding;
        for (Category.ModuleCategory category : Category.ModuleCategory.values()) {
            drawRoundedFrame(context, category, x, Padding, mouseX, mouseY);
            x += Frame_Width + Padding;
        }
    }

    private void drawRoundedFrame(DrawContext context, Category.ModuleCategory category, int x, int y, int mouseX, int mouseY) {
        int[] f = getXandY(x, y, Frame_Width, Frame_Height);
        drawArc(context, Radius, f[0], f[1], f[2], f[1] + Button_Height);
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
                    drawButton(context, module.getName(), moduleX, moduleY, Module_Button_Width, Button_Height, 0xFFCCCCCC);

                    moduleButtonMap.put(module, new int[]{moduleX, moduleY, Module_Button_Width, Button_Height});

                    BindSetting bind = module.getBindSetting();
                    bind.render(context, moduleX + Module_Button_Width + 5, moduleY);

                    by += Button_Height + Padding;
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
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
            int[] f = getXandY(x, Padding, Frame_Width, Frame_Height);
            if (isHovering(f[0], f[1], Button_Width, Button_Height, (int) mouseX, (int) mouseY)) {
                expanded.put(category, !expanded.get(category));
                return true;
            }
            x += Frame_Width + Padding;
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

    private void drawArc(DrawContext ctx, int radius, int x1, int y1, int x2, int y2) {
        int headerColor = 0xFF444444;
        ctx.fill(x1 + radius, y1, x2 - radius, y2, headerColor);
        roundedOval(ctx, x1, y1, radius * 2, radius * 2, headerColor);
        roundedOval(ctx, x2 - radius * 2, y1, radius * 2, radius * 2, headerColor);
    }

    private void roundedOval(DrawContext ctx, int x, int y, int width, int height, int color) {
        int rx = width  / 2;
        int ry = height / 2;
        int cx = x + rx;
        int cy = y + ry;

        for (int dy = -ry; dy <= ry; dy++) {
            double ny = dy / (double) ry;
            double dx = rx * Math.sqrt(1 - (ny * ny));
            int span = (int) Math.ceil(dx);

            ctx.fill(cx - span, cy + dy, cx + span, cy + dy + 1, color);
        }
    }

    private void drawButton(DrawContext ctx, String label, int x, int y, int width, int height, int bgColor) {
        ctx.fill(x, y, x + width, y + height, bgColor);
        int textX = x + width / 2;
        int textY = y + (height - 8) / 2;
        ctx.drawCenteredTextWithShadow(this.textRenderer, Text.literal(label), textX, textY, 0xFFFFFFFF);
    }

    private int[] getXandY(int x, int y, int w, int h) {
        return new int[] { x, y, x + w, y + h };
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
}