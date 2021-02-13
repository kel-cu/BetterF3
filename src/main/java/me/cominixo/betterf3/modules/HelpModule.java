package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;


public class HelpModule extends BaseModule {


    public HelpModule() {
        this.defaultNameColor = Color.fromInt(0xfdfd96);
        this.defaultValueColor = Color.fromTextFormatting(TextFormatting.AQUA);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;


        lines.add(new DebugLine("pie_graph"));
        lines.add(new DebugLine("fps_tps"));
        lines.add(new DebugLine("help"));

        for (DebugLine line : lines) {
            line.inReducedDebug = true;
        }

    }

    public void update(Minecraft client) {

        String visible = I18n.format("text.betterf3.line.visible");
        String hidden = I18n.format("text.betterf3.line.hidden");

        // Pie Graph (Shift+F3)
        lines.get(0).setValue(client.gameSettings.showDebugProfilerChart ? Utils.getStyledText(visible,
                Color.fromTextFormatting(TextFormatting.GREEN))
                :  Utils.getStyledText(hidden, Color.fromTextFormatting(TextFormatting.RED)));

        // FPS / TPS (Alt+F3)
        lines.get(1).setValue(client.gameSettings.showLagometer ? Utils.getStyledText(visible,
                Color.fromTextFormatting(TextFormatting.GREEN))
                :  Utils.getStyledText(hidden, Color.fromTextFormatting(TextFormatting.RED)));

        // For help
        lines.get(2).setValue(I18n.format("text.betterf3.line.help_press"));

    }
}
