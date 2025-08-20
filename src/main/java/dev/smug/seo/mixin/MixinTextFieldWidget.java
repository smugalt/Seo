package dev.smug.seo.mixin;

import dev.smug.seo.modules.misc.Chat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TextFieldWidget.class)
public abstract class MixinTextFieldWidget {

    @Inject(at = @At("RETURN"), method = "charTyped")
    public void type(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            MinecraftClient mc = MinecraftClient.getInstance();
            if(!Chat.displayTyping.get()) {
                return;
            }

            if (mc != null && mc.player != null) {
                String message = Chat.prefix.get() + "is" + " typing...";
                mc.player.sendMessage(Text.of(message), true);
            }
        }
    }
}