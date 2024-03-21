package me.cominixo.betterf3.mixin;

import me.cominixo.betterf3.config.GeneralOptions;
import me.cominixo.betterf3.config.gui.ModConfigScreen;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.cominixo.betterf3.utils.Utils.START_X_POS;
import static me.cominixo.betterf3.utils.Utils.closingAnimation;
import static me.cominixo.betterf3.utils.Utils.xPos;

/**
 * Modifies the debug keys (f3 / f3 + m).
 */
@Mixin(KeyboardHandler.class)
public abstract class KeyboardMixin {
  @Shadow
  @Final
  private Minecraft minecraft;

  @Shadow
  protected abstract void debugFeedbackTranslated(String key, Object... args);

  /**
   * Adds the config menu by pressing f3 + m.
   *
   * @param key key pressed with f3
   * @param cir Callback info
   */
  @Inject(method = "handleDebugKeys", at = @At("HEAD"), cancellable = true)
  public void processF3(final int key, final CallbackInfoReturnable<Boolean> cir) {
    if (key == 77) { // Key m
      this.minecraft.setScreen(new ModConfigScreen(null));
      cir.setReturnValue(true);
    } else if (key == 70) {
      if (Screen.hasControlDown()) {
        this.minecraft.options.simulationDistance().set(Mth.clamp((this.minecraft.options.simulationDistance().get() + (Screen.hasShiftDown() ? -1 : 1)), 5, (this.minecraft.is64Bit() && Runtime.getRuntime().maxMemory() >= 1000000000L) ? 32 : 16));
        this.debugFeedbackTranslated("debug.betterf3.cycle_simulationdistance.message", this.minecraft.options.simulationDistance().get());
      } else {
        this.minecraft.options.renderDistance().set(Mth.clamp((this.minecraft.options.renderDistance().get() + (Screen.hasShiftDown() ? -1 : 1)), 2, (this.minecraft.is64Bit() && Runtime.getRuntime().maxMemory() >= 1000000000L) ? 32 : 16));
        this.debugFeedbackTranslated("debug.betterf3.cycle_renderdistance.message", this.minecraft.options.renderDistance().get());
      }
      cir.setReturnValue(true);
    }
  }

  /**
   * Adds BetterF3 F3 + Q messages.
   *
   * @param key the keyboard key with f3
   * @param cir the callback info
   */
  @Inject(method = "handleDebugKeys", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent;addMessage(Lnet/minecraft/network/chat/Component;)V", shift = At.Shift.AFTER, ordinal = 14))
  public void processF3Messages(final int key, final CallbackInfoReturnable<Boolean> cir) {
    if (key == 81) {
      this.minecraft.gui.getChat().addMessage(Component.literal(""));
      this.minecraft.gui.getChat().addMessage(Component.translatable("debug.betterf3.cycle_renderdistance.help"));
      this.minecraft.gui.getChat().addMessage(Component.translatable("debug.betterf3.cycle_simulationdistance.help"));
      this.minecraft.gui.getChat().addMessage(Component.translatable("debug.betterf3.modmenu.help"));
    }
  }

  @Inject(method = "keyPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/DebugScreenOverlay;toggleOverlay()V", opcode = Opcodes.PUTFIELD, ordinal = 0), cancellable = true)
  private void animationAndAlwaysEnableProfiler(final long window, final int key, final int scancode, final int action, final int modifiers, final CallbackInfo ci) {
    if (!GeneralOptions.disableMod) {
      if (GeneralOptions.enableAnimations) {
        if (this.minecraft.getDebugOverlay().showDebugScreen()) {
          closingAnimation = true;
          ci.cancel();
        } else {
          closingAnimation = false;
          xPos = START_X_POS;
          this.minecraft.getDebugOverlay().toggleOverlay();
        }
      } else {
        this.minecraft.getDebugOverlay().toggleOverlay();
      }
      if (GeneralOptions.alwaysEnableProfiler) {
        this.minecraft.getDebugOverlay().renderProfilerChart = this.minecraft.getDebugOverlay().showDebugScreen();
      }
      if (GeneralOptions.alwaysEnableTPS) {
        this.minecraft.getDebugOverlay().renderFpsCharts = this.minecraft.getDebugOverlay().showDebugScreen();
      }
      if (GeneralOptions.alwaysEnablePing) {
        if (this.minecraft.getDebugOverlay().showDebugScreen() && !this.minecraft.getDebugOverlay().renderFpsCharts &&
                !this.minecraft.getDebugOverlay().showNetworkCharts()) {
          this.minecraft.getDebugOverlay().toggleNetworkCharts();
        }
      }
    } else {
      this.minecraft.getDebugOverlay().toggleOverlay();
    }
    ci.cancel();
  }

}
