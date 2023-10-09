package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

/**
 * The Help module.
 */
public class HelpModule extends BaseModule {

  /**
   * Default enabled color.
   */
  public final TextColor defaultEnabledColor = TextColor.fromFormatting(Formatting.GREEN);

  /**
   * Default disabled color.
   */
  public final TextColor defaultDisabledColor = TextColor.fromFormatting(Formatting.RED);

  /**
   * Enabled color.
   */
  public TextColor enabledColor;

  /**
   * Disabled color.
   */
  public TextColor disabledColor;

  /**
   * Instantiates a new Help module.
   */
  public HelpModule() {
    this.defaultNameColor = TextColor.fromRgb(0xfdfd96);
    this.defaultValueColor = TextColor.fromFormatting(Formatting.AQUA);

    this.nameColor = defaultNameColor;
    this.valueColor = defaultValueColor;
    this.enabledColor = this.defaultEnabledColor;
    this.disabledColor = this.defaultDisabledColor;

    lines.add(new DebugLine("pie_graph_new"));
    lines.add(new DebugLine("fps_tps_new"));
    lines.add(new DebugLine("ping"));
    lines.add(new DebugLine("help"));

    for (final DebugLine line : lines) {
      line.inReducedDebug = true;
    }
  }

  /**
   * Updates the Help module.
   *
   * @param client the Minecraft client
   */
  public void update(final MinecraftClient client) {

    final String visible = I18n.translate("text.betterf3.line.visible");
    final String hidden = I18n.translate("text.betterf3.line.hidden");

    // Pie Graph (F3+1)
    lines.get(0).value(client.getDebugHud().shouldShowRenderingChart() ? Utils.styledText(visible, this.enabledColor)
      : Utils.styledText(hidden, this.disabledColor));

    // FPS / TPS (F3+2)
    lines.get(1).value(client.getDebugHud().renderingAndTickChartsVisible ? Utils.styledText(visible, this.enabledColor)
      : Utils.styledText(hidden, this.disabledColor));

    // Ping / Bandwidth (F3+3)
    lines.get(2).value(client.getDebugHud().shouldShowPacketSizeAndPingCharts() ? Utils.styledText(visible, this.enabledColor)
      : Utils.styledText(hidden, this.disabledColor));

    // For help
    lines.get(3).value(I18n.translate("text.betterf3.line.help_press"));
  }
}
