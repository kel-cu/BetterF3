package me.cominixo.betterf3.utils;

import net.minecraft.state.Property;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.text.*;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;
import java.util.Map;

public class Utils {


    // Animation stuff
    public static final int START_X_POS = 200;
    public static int xPos = START_X_POS;
    public static long lastAnimationUpdate = 0;
    public static boolean closingAnimation = false;

    public static int getFpsColor(int currentFps) {

        if (currentFps >= 60) {
            return 0;
        } else if (currentFps >= 20) {
            return 1;
        } else {
            return 2;
        }
    }

    public static TextFormatting getPercentColor(int percent) {

        if (percent >= 90) {
            return TextFormatting.RED;
        } else if (percent >= 60) {
            return TextFormatting.YELLOW;
        } else {
            return TextFormatting.GREEN;
        }

    }

    public static String getFacingString(Direction facing) {

        switch(facing) {
            case NORTH:
                return new TranslationTextComponent("text.betterf3.line.negative_z").getString();
            case SOUTH:
                return new TranslationTextComponent("text.betterf3.line.positive_z").getString();
            case WEST:
                return new TranslationTextComponent("text.betterf3.line.negative_x").getString();
            case EAST:
                return new TranslationTextComponent("text.betterf3.line.positive_x").getString();
            default:
                return "";
        }
    }

    public static IFormattableTextComponent getStyledText(Object string, Color color) {
        if (string == null) {
            string = "";
        }
        return new StringTextComponent(string.toString()).modifyStyle((style) -> style.setColor(color));
    }

    public static String enumToString(Enum<?> e) {
        return WordUtils.capitalizeFully(e.toString().replace("_", " "));
    }

    public static ITextComponent getFormattedFromString(String string, Color nameColor, Color valueColor) {
        String[] split = string.split(":");

        if (string.contains(":")) {

            IFormattableTextComponent name = Utils.getStyledText(split[0], nameColor);
            IFormattableTextComponent value = Utils.getStyledText(String.join(":", Arrays.asList(split).subList(1, split.length)), valueColor);

            return name.append(new StringTextComponent(":")).append(value);
        } else {
            return new StringTextComponent(string);
        }
    }

    public static String propertyToString(Map.Entry<Property<?>, Comparable<?>> propEntry) {

        Property<?> key = propEntry.getKey();
        Comparable<?> value = propEntry.getValue();

        String newValue = Util.getValueName(key, value);

        if (Boolean.TRUE.equals(value)) {
            newValue = TextFormatting.GREEN + newValue;
        } else if (Boolean.FALSE.equals(value)) {
            newValue = TextFormatting.RED + newValue;
        }

        return key.getName() + ": " + newValue;
    }


}
