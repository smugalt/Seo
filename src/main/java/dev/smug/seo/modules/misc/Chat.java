package dev.smug.seo.modules.misc;

import dev.smug.seo.api.module.Module;
import dev.smug.seo.gui.impl.components.BooleanSetting;
import dev.smug.seo.gui.impl.components.SliderSetting;
import dev.smug.seo.gui.impl.components.StringSetting;

public class Chat extends Module {


    public Chat() {
        super("Chat", Misc, "Tweaks to the minecraft chat");
    }

    public static BooleanSetting displayTyping = new BooleanSetting("DisplayTyping", true);
    public SliderSetting typingSpeed = new SliderSetting("TypingSpeed", 1.0, 0.1, 5.0, 0.1);
    public static StringSetting prefix = new StringSetting("Prefix", "you");




    @Override
    public void onEnable() {

    }



}
