package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;

public class SoundModule extends BaseModule {

    public final Color totalColor = Color.fromTextFormatting(TextFormatting.DARK_AQUA);

    public SoundModule() {

        this.defaultNameColor = Color.fromTextFormatting(TextFormatting.GOLD);
        this.defaultValueColor = Color.fromTextFormatting(TextFormatting.AQUA);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;


        lines.add(new DebugLine("sounds", "format.betterf3.total", true));
        lines.add(new DebugLine("ambient_sounds", "format.betterf3.total", true));
    }

    public void update(Minecraft client) {

        // String.format("Sounds: %d/%d + %d/%d", this.staticHandler.getActiveSoundSourceCount(), this.staticHandler
        // .getMaxSoundSources(), this.streamingHandler.getActiveSoundSourceCount(), this.streamingHandler
        // .getMaxSoundSources())
        String debugString = client.getSoundHandler().getDebugString();
        String[] staticHandlerList =
                debugString.substring(8).substring(0, debugString.indexOf(" ")).replace(" +", "").split("/");
        String[] streamingHandlerList = debugString.substring(debugString.indexOf("+") + 1).replace(" ", "").split("/");

        String playing = I18n.format("text.betterf3.line.playing");
        String maximum = I18n.format("text.betterf3.line.maximum");

        // Sound
        lines.get(0).setValue(Arrays.asList(Utils.getStyledText(playing, valueColor), Utils.getStyledText(maximum,
                totalColor), Utils.getStyledText(staticHandlerList[0], valueColor),
                Utils.getStyledText(staticHandlerList[1], totalColor)));

        // Ambient Sound
        lines.get(1).setValue(Arrays.asList(Utils.getStyledText(playing, valueColor), Utils.getStyledText(maximum,
                totalColor), Utils.getStyledText(streamingHandlerList[0], valueColor),
                Utils.getStyledText(streamingHandlerList[1], totalColor)));

    }

}
