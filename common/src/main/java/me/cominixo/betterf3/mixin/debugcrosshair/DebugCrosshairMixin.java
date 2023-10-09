package me.cominixo.betterf3.mixin.debugcrosshair;

import me.cominixo.betterf3.config.GeneralOptions;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Main debug crosshair removal mixin.
 */
@Mixin(value = InGameHud.class, priority = 1100)
public class DebugCrosshairMixin {

  @Redirect(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;shouldShowDebugHud()Z"))
  private boolean removeDebugCrosshair(final DebugHud instance) {
    if (!GeneralOptions.disableMod) {
      if (GeneralOptions.hideDebugCrosshair)
        return false;
    }
    return instance.shouldShowDebugHud();
  }
}
