package me.cominixo.betterf3.mixin;

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
   * @param context Draw Context
   * @param ci CallbackInfo
   */
  @Inject(method = "method_51746", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;drawRightText(Lnet/minecraft/client/gui/DrawContext;)V", shift = At.Shift.AFTER))
  public void renderFontScaleRightAfter(final DrawContext context, final CallbackInfo ci) {
    if (GeneralOptions.disableMod) {
      return;
    }
    context.getMatrices().pop();
  }
}
