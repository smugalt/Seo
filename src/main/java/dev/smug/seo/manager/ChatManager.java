package dev.smug.seo.manager;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;


public class ChatManager extends Manager {
    private static final Text Prefix = Text.literal("[")
            .styled(style -> style.withColor(Formatting.GOLD))
            .append(Text.literal("Seo").styled(style -> style.withColor(Formatting.LIGHT_PURPLE)))
            .append(Text.literal("]").styled(style -> style.withColor(Formatting.GOLD)));


    public static void sendMessage(String message) {
        sendMessage(message, null);
    }

    public static void sendMessage(String message, Formatting formatting) {
        if (player == null || mc.inGameHud == null) return;

        String rawMessage = Objects.requireNonNullElse(message, "null");
        Text text = Text.literal("")
                .append(Prefix)
                .append(Text.literal(" " + rawMessage));

        if (formatting != null) {
            text = text.copy().setStyle(text.getStyle().withFormatting(formatting));
        }

        mc.inGameHud.getChatHud().addMessage(text);
    }
}
