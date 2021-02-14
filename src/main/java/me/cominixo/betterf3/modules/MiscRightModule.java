package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLineList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MiscRightModule extends BaseModule{

    private static final List<String> VANILLA_DEBUG_RIGHT = Arrays.asList("Java:", "Mem:", "Allocated:", "CPU:", "Display:");
    private final int rightSideSize = 0;

    public MiscRightModule() {
        this.defaultNameColor = Color.fromInt(0xfdfd96);
        this.defaultValueColor = Color.fromTextFormatting(TextFormatting.AQUA);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        DebugLineList rightDebugLines = new DebugLineList("misc_right");
        rightDebugLines.inReducedDebug = true;

        lines.add(rightDebugLines);

    }

    public void update(Minecraft client) {
        // Do nothing
    }

    public void update(List<String> lines) {
        // Parse lines to find non-vanilla lines, it's a mess
        if (lines.size() != rightSideSize) {
            // boolean that indicates if we're on the "targeted" section, we have no other way of knowing when it starts/ends
            boolean inTargeted = false;

            // copy of list for .remove()
            List<String> listCopy = new ArrayList<>(lines);

            for (String s : listCopy) {
                if (s.isEmpty()) {
                    inTargeted = false;
                    lines.remove(s);
                    continue;
                }

                if (s.startsWith(TextFormatting.UNDERLINE + "Targeted Block") || s.startsWith(TextFormatting.UNDERLINE +
                        "Targeted Fluid") || s.startsWith(TextFormatting.UNDERLINE + "Targeted Entity")) {
                    inTargeted = true;
                    lines.remove(s);
                    continue;
                }

                if (inTargeted) {
                    lines.remove(s);
                    continue;
                }

                for (String vanilla : VANILLA_DEBUG_RIGHT) {
                    int index = listCopy.indexOf(s);

                    if (s.startsWith(vanilla)) {
                        lines.remove(s);

                    } else if (index > 1) {
                        // pretty bad, this is to include GPU info in the vanilla list
                        if (listCopy.get(index-1).startsWith("Display:") || listCopy.get(index-2).startsWith("Display:")) {
                            lines.remove(s);

                        }
                    }
                }
            }

        }
        ((DebugLineList)this.lines.get(0)).setValues(lines);
    }
}
