package me.cominixo.betterf3.mixin;

import me.cominixo.betterf3.config.GeneralOptions;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.cominixo.betterf3.utils.Utils.START_X_POS;
import static me.cominixo.betterf3.utils.Utils.closingAnimation;
import static me.cominixo.betterf3.utils.Utils.lastAnimationUpdate;
import static me.cominixo.betterf3.utils.Utils.xPos;

/**
 * The Debug Screen Overlay.
 */
@Mixin(DebugScreenOverlay.class)
public abstract class DebugMixin {

  /**
   * Toggles the debug HUD.
   */
  @Shadow private boolean renderDebug;

  /**
   * Disables the unneeded math for allocation rate.
   *
   * @param instance the allocation rate calculator
   * @param allocatedBytes the allocated bytes
   * @return nothing
   */
  @Redirect(method = "getSystemInformation", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/DebugScreenOverlay$AllocationRateCalculator;bytesAllocatedPerSecond(J)J"))
  public long allocationRateCalculatorGet(final DebugScreenOverlay.AllocationRateCalculator instance, final long allocatedBytes) {
    return 0;
  }

  /**
   * Ensures that the TPS graph works.
   *
   * @param context Draw Context
   * @param ci Callback info
   */
  @Inject(method = "render", at = @At(value = "HEAD"))
  public void renderBefore(final GuiGraphics context, final CallbackInfo ci) {
    if (GeneralOptions.disableMod) {
      return;
    }
    context.pose().pushPose();
  }

  /**
   * Modifies the font scale.
   *
   * @param context Draw Context
   * @param ci Callback info
   */
  @Inject(method = "method_51746", at = @At(value = "HEAD"))
  public void renderFontScaleBefore(final GuiGraphics context, final CallbackInfo ci) {
    if (!GeneralOptions.disableMod) {
      context.pose().scale((float) GeneralOptions.fontScale, (float) GeneralOptions.fontScale, 1F);
    }
  }

  /**
   * Renders the animation.
   *
   * @param context Draw Context
   * @param ci Callback info
   */
  @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;push(Ljava/lang/String;)V"))
  public void renderAnimation(final GuiGraphics context, final CallbackInfo ci) {

    if (GeneralOptions.disableMod) {
      return;
    }
    if (!GeneralOptions.enableAnimations) {
      return;
    } // Only displays the animation if set to true

    final long time = Util.getMillis();
    if (time - lastAnimationUpdate >= 10 && (xPos != 0 || closingAnimation)) {

      int i = ((START_X_POS / 2 + xPos) / 10) - 9;

      if (xPos != 0 && !closingAnimation) {
        xPos = (int) (xPos / GeneralOptions.animationSpeed);
        xPos -= i;
      }

      if (i == 0) {
        i = 1;
      }

      if (closingAnimation) {

        xPos += i;
        xPos = (int) (xPos * GeneralOptions.animationSpeed);

        if (xPos >= 300) {
          this.renderDebug = false;
          closingAnimation = false;
        }

      }

      lastAnimationUpdate = time;
    }
  }

}
