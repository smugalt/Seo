package dev.smug.seo.util.input;

import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class KeyboardKey {

    private static final Map<Integer, String> keyCodeToName = new HashMap<>();
    private static final Map<String, Integer> keyNameToCode = new HashMap<>();

    // https://www.glfw.org/docs/3.3/group__keys.html

    static { // Numeric keys
        for (char c = '0'; c <= '9'; c++) {
            int glfwCode = GLFW.GLFW_KEY_0 + (c - '0');
            map(glfwCode, String.valueOf(c));
        }

        // Alphabetic keys
        for (char c = 'a'; c <= 'z'; c++) {
            int glfwCode = GLFW.GLFW_KEY_A + (c - 'a');
            map(glfwCode, String.valueOf(c).toUpperCase(), String.valueOf(c));
        }

        // Special keys
        map(GLFW.GLFW_KEY_UNKNOWN, "Unknown");
        map(GLFW.GLFW_KEY_ESCAPE, "Escape", "esc");
        map(GLFW.GLFW_KEY_GRAVE_ACCENT, "Grave Accent", "grave");
        map(GLFW.GLFW_KEY_WORLD_1, "World 1", "world1");
        map(GLFW.GLFW_KEY_WORLD_2, "World 2", "world2");
        map(GLFW.GLFW_KEY_PRINT_SCREEN, "Print Screen", "prtscr");
        map(GLFW.GLFW_KEY_PAUSE, "Pause", "pause");
        map(GLFW.GLFW_KEY_INSERT, "Insert", "insert");
        map(GLFW.GLFW_KEY_DELETE, "Delete", "delete");
        map(GLFW.GLFW_KEY_HOME, "Home", "home");
        map(GLFW.GLFW_KEY_PAGE_UP, "Page Up", "pgup");
        map(GLFW.GLFW_KEY_PAGE_DOWN, "Page Down", "pgdown");
        map(GLFW.GLFW_KEY_END, "End", "end");
        map(GLFW.GLFW_KEY_TAB, "Tab", "tab");
        map(GLFW.GLFW_KEY_LEFT_CONTROL, "Left Control", "lctrl");
        map(GLFW.GLFW_KEY_RIGHT_CONTROL, "Right Control", "rctrl");
        map(GLFW.GLFW_KEY_LEFT_ALT, "Left Alt", "lalt");
        map(GLFW.GLFW_KEY_RIGHT_ALT, "Right Alt", "ralt");
        map(GLFW.GLFW_KEY_LEFT_SHIFT, "Left Shift", "lshift");
        map(GLFW.GLFW_KEY_RIGHT_SHIFT, "Right Shift", "rshift");
        map(GLFW.GLFW_KEY_UP, "Arrow Up", "up");
        map(GLFW.GLFW_KEY_DOWN, "Arrow Down", "down");
        map(GLFW.GLFW_KEY_LEFT, "Arrow Left", "left");
        map(GLFW.GLFW_KEY_RIGHT, "Arrow Right", "right");
        map(GLFW.GLFW_KEY_APOSTROPHE, "Apostrophe", "apostrophe");
        map(GLFW.GLFW_KEY_BACKSPACE, "Backspace", "back");
        map(GLFW.GLFW_KEY_CAPS_LOCK, "Caps Lock", "capslock");
        map(GLFW.GLFW_KEY_MENU, "Menu", "menu");
        map(GLFW.GLFW_KEY_LEFT_SUPER, "Left Super", "lsuper");
        map(GLFW.GLFW_KEY_RIGHT_SUPER, "Right Super", "rsuper");
        map(GLFW.GLFW_KEY_ENTER, "Enter", "enter");
        map(GLFW.GLFW_KEY_KP_ENTER, "Numpad Enter", "numenter");
        map(GLFW.GLFW_KEY_NUM_LOCK, "Num Lock", "numlock");
        map(GLFW.GLFW_KEY_SPACE, "Space", "space");

        //F1 to F25
        for (int i = GLFW.GLFW_KEY_F1; i <= GLFW.GLFW_KEY_F25; i++) {
            String name = "F" + (i - GLFW.GLFW_KEY_F1 + 1);
            map(i, name, name.toLowerCase());
        }

        //Mouse buttons
        map(-1, "Left Click", "lclick");
        map(-2, "Right Click", "rclick");
        map(-3, "Middle Click", "mclick");
        map(-4, "Button 3", "button3");
        map(-5, "Button 4", "button4");
        map(0, "None", "none");
    }

    private static void map(int code, String displayName, String... aliases) {
        keyCodeToName.put(code, displayName);
        keyNameToCode.put(displayName.toLowerCase(), code);
        for (String alias : aliases) {
            keyNameToCode.put(alias.toLowerCase(), code);
        }
    }

    public static String getKeyName(int key) {
        return keyCodeToName.getOrDefault(key, "Unknown");
    }

    public static int getKeyNumber(String name) {
        return keyNameToCode.getOrDefault(name.toLowerCase(), -1);
    }
}