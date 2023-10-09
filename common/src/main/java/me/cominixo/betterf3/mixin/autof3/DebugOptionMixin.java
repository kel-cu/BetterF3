package me.cominixo.betterf3.mixin.autof3;

import me.cominixo.betterf3.config.GeneralOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Sets automatically opening the debug screen in the options.
 */
@Mixin(DebugHud.class)
public abstract class DebugOptionMixin {

  @Shadow
  public boolean renderingAndTickChartsVisible;
  @Shadow
  public boolean renderingChartVisible;
  @Shadow
  private boolean showDebugHud;
  @Final
  @Shadow private MinecraftClient client;

  /**
   * Toggles the packet size and ping charts.
   */
  @Shadow public abstract void togglePacketSizeAndPingCharts();

  /**
   * Sets the debug option to true.
   *
   * @param client the client
   * @param ci the callback info
   */
  @Inject(method = "<init>", at = @At("RETURN"))
  public void addAutomaticDebugOption(final MinecraftClient client, final CallbackInfo ci) {
    if (!GeneralOptions.disableMod && GeneralOptions.autoF3) {
      this.showDebugHud = true;
      if (GeneralOptions.alwaysEnableProfiler) this.renderingChartVisible = true;
      if (GeneralOptions.alwaysEnableTPS) this.renderingAndTickChartsVisible = true;
      if (GeneralOptions.alwaysEnablePing) this.togglePacketSizeAndPingCharts();
    }
  }

  @Inject(method = "clear", at = @At("RETURN"))
  private void automaticF3(final CallbackInfo ci) {
    if (!GeneralOptions.disableMod && GeneralOptions.autoF3) {
      this.showDebugHud = true;
      if (GeneralOptions.alwaysEnableProfiler) this.renderingChartVisible = true;
      if (GeneralOptions.alwaysEnableTPS) this.renderingAndTickChartsVisible = true;
      if (GeneralOptions.alwaysEnablePing) this.togglePacketSizeAndPingCharts();
    }
  }

  @Inject(method = "shouldShowDebugHud", at = @At("HEAD"), cancellable = true)
  private void shouldShowDebugHud(final CallbackInfoReturnable<Boolean> cir) {
    if (!GeneralOptions.disableMod && GeneralOptions.autoF3 && this.showDebugHud && !this.client.options.hudHidden) {
      cir.setReturnValue(this.client.world != null);
    }
  }

}
