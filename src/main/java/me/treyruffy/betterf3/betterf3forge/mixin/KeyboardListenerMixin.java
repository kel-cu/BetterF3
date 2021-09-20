package me.treyruffy.betterf3.betterf3forge.mixin;

import me.cominixo.betterf3.config.GeneralOptions;
import me.cominixo.betterf3.config.gui.ModConfigScreen;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextColor;
import net.minecraftforge.fml.ModList;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

import static me.cominixo.betterf3.utils.Utils.*;

@Mixin(KeyboardHandler.class)
public class KeyboardListenerMixin {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "handleDebugKeys", at = @At("HEAD"))
    public void handleDebugKeys(int key, CallbackInfoReturnable<Boolean> cir) {
        if (key == 77) {
            if (ModList.get().isLoaded("cloth_config"))
                minecraft.setScreen(new ModConfigScreen(null));
            else
                ((LocalPlayer) Objects.requireNonNull(minecraft.getCameraEntity())).displayClientMessage(Utils.getStyledText(
                        "[BetterF3] " +
                                I18n.get("config.betterf3.need_cloth_config"),
                        TextColor.fromLegacyFormat(ChatFormatting.RED)), false);
        }
    }

    @Inject(method="keyPress", at=@At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "net/minecraft/client/Options.renderDebug:Z"), cancellable = true)
    public void keyPress(long window, int key, int scancode, int i, int j, CallbackInfo ci) {
        if (GeneralOptions.enableAnimations) {
            if (this.minecraft.options.renderDebug) {
                closingAnimation = true;
                ci.cancel();
            } else {
                closingAnimation = false;
                xPos = START_X_POS;
            }
        }
    }
}
