package me.treyruffy.betterf3.mixin;

import me.cominixo.betterf3.config.GeneralOptions;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Debug Lambda Mixin.
 */
@Mixin(DebugHud.class)
public class DebugLambdaMixin {
  /**
   * Fixes the font scale.
   *
   * @param arg Draw Context
   * @param ci CallbackInfo
   */
  @Inject(method = "drawText(Lnet/minecraft/client/gui/DrawContext;)V", at = @At(value = "TAIL"))
  public void renderFontScaleRightAfter(final DrawContext arg, final CallbackInfo ci) {
    if (GeneralOptions.disableMod) {
      return;
    }
    arg.getMatrices().pop();
  }
}
