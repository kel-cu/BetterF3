package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collections;

public class FpsModule extends BaseModule{

    public Color colorHigh;
    public Color colorMed;
    public Color colorLow;

    public Color defaultColorHigh = Color.fromTextFormatting(TextFormatting.GREEN);
    public Color defaultColorMed = Color.fromTextFormatting(TextFormatting.YELLOW);
    public Color defaultColorLow = Color.fromTextFormatting(TextFormatting.RED);

    public FpsModule() {

        lines.add(new DebugLine("fps", "format.betterf3.no_format", true));
        lines.get(0).inReducedDebug = true;

        colorHigh = defaultColorHigh;
        colorMed = defaultColorMed;
        colorLow = defaultColorLow;

    }

    public void update(Minecraft client) {

        int currentFps = Integer.parseInt(client.debug.split(" ")[0].split("/")[0]);

        String fpsString = I18n.format("format.betterf3.fps", currentFps,
                (double)client.gameSettings.framerateLimit == AbstractOption.FRAMERATE_LIMIT.getMaxValue() ?
                        I18n.format("text.betterf3.line" +
                        ".fps.unlimited") : client.gameSettings.framerateLimit,
                client.gameSettings.vsync ? I18n.format("text.betterf3.line.fps.vsync") : "").trim();

        Color color;

        switch (Utils.getFpsColor(currentFps)) {
            case 0:
                color = colorHigh;
                break;
            case 1:
                color = colorMed;
                break;
            case 2:
                color = colorLow;
                break;
            default:
                color = Color.fromTextFormatting(TextFormatting.RESET);
        }

        lines.get(0).setValue(Collections.singletonList(Utils.getStyledText(fpsString, color)));


    }


}
