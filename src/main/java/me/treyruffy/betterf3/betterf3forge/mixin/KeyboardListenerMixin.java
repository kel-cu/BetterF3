package me.treyruffy.betterf3.betterf3forge.mixin;

import me.cominixo.betterf3.config.GeneralOptions;
import me.cominixo.betterf3.config.gui.ModConfigScreen;
import net.minecraft.client.KeyboardListener;
import net.minecraft.client.Minecraft;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.cominixo.betterf3.utils.Utils.*;

@Mixin(KeyboardListener.class)
public class KeyboardListenerMixin {
    @Shadow @Final private Minecraft mc;

    @Inject(method = "processKeyF3", at = @At("HEAD"))
    public void processF3(int key, CallbackInfoReturnable<Boolean> cir) {
        if (key == 77) {
            mc.displayGuiScreen(new ModConfigScreen());
        }
    }

    @Inject(method="onKeyEvent", at=@At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "net/minecraft/client/GameSettings.showDebugInfo:Z"), cancellable = true)
    public void onDebugActivate(long window, int key, int scancode, int i, int j, CallbackInfo ci) {


        if (GeneralOptions.enableAnimations) {
            if (this.mc.gameSettings.showDebugInfo) {
                closingAnimation = true;
            } else {
                closingAnimation = false;
                xPos = START_X_POS;
            }
        }

    }

}
