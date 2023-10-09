package me.cominixo.betterf3.mixin;

import me.cominixo.betterf3.config.GeneralOptions;
import me.cominixo.betterf3.config.gui.ModConfigScreen;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
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
@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
  @Shadow
  @Final
  private MinecraftClient client;

  @Shadow
  protected abstract void debugLog(String key, Object... args);

  /**
   * Adds the config menu by pressing f3 + m.
   *
   * @param key key pressed with f3
   * @param cir Callback info
   */
  @Inject(method = "processF3", at = @At("HEAD"), cancellable = true)
  public void processF3(final int key, final CallbackInfoReturnable<Boolean> cir) {
    if (key == 77) { // Key m
      this.client.setScreen(new ModConfigScreen(null));
      cir.setReturnValue(true);
    } else if (key == 70) {
      if (Screen.hasControlDown()) {
        this.client.options.getSimulationDistance().setValue(MathHelper.clamp((this.client.options.getSimulationDistance().getValue() + (Screen.hasShiftDown() ? -1 : 1)), 5, (this.client.is64Bit() && Runtime.getRuntime().maxMemory() >= 1000000000L) ? 32 : 16));
        this.debugLog("debug.betterf3.cycle_simulationdistance.message", this.client.options.getSimulationDistance().getValue());
      } else {
        this.client.options.getViewDistance().setValue(MathHelper.clamp((this.client.options.getViewDistance().getValue() + (Screen.hasShiftDown() ? -1 : 1)), 2, (this.client.is64Bit() && Runtime.getRuntime().maxMemory() >= 1000000000L) ? 32 : 16));
        this.debugLog("debug.betterf3.cycle_renderdistance.message", this.client.options.getViewDistance().getValue());
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
  @Inject(method = "processF3", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;)V", shift = At.Shift.AFTER, ordinal = 14))
  public void processF3Messages(final int key, final CallbackInfoReturnable<Boolean> cir) {
    if (key == 81) {
      this.client.inGameHud.getChatHud().addMessage(Text.literal(""));
      this.client.inGameHud.getChatHud().addMessage(Text.translatable("debug.betterf3.cycle_renderdistance.help"));
      this.client.inGameHud.getChatHud().addMessage(Text.translatable("debug.betterf3.cycle_simulationdistance.help"));
      this.client.inGameHud.getChatHud().addMessage(Text.translatable("debug.betterf3.modmenu.help"));
    }
  }

  @Inject(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;toggleDebugHud()V", opcode = Opcodes.PUTFIELD, ordinal = 0), cancellable = true)
  private void animationAndAlwaysEnableProfiler(final long window, final int key, final int scancode, final int action, final int modifiers, final CallbackInfo ci) {
    if (!GeneralOptions.disableMod) {
      if (GeneralOptions.enableAnimations) {
        if (this.client.getDebugHud().shouldShowDebugHud()) {
          closingAnimation = true;
          ci.cancel();
        } else {
          closingAnimation = false;
          xPos = START_X_POS;
          this.client.getDebugHud().toggleDebugHud();
        }
      } else {
        this.client.getDebugHud().toggleDebugHud();
      }
      if (GeneralOptions.alwaysEnableProfiler) {
        this.client.getDebugHud().renderingChartVisible = this.client.getDebugHud().shouldShowDebugHud();
      }
      if (GeneralOptions.alwaysEnableTPS) {
        this.client.getDebugHud().renderingAndTickChartsVisible = this.client.getDebugHud().shouldShowDebugHud();
      }
      if (GeneralOptions.alwaysEnablePing) {
        if (this.client.getDebugHud().shouldShowDebugHud() && !this.client.getDebugHud().renderingAndTickChartsVisible &&
                !this.client.getDebugHud().shouldShowPacketSizeAndPingCharts()) {
          this.client.getDebugHud().togglePacketSizeAndPingCharts();
        }
      }
    } else {
      this.client.getDebugHud().toggleDebugHud();
    }
    ci.cancel();
  }

}
