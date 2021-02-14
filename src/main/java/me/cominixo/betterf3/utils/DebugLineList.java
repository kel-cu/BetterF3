package me.cominixo.betterf3.utils;

import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DebugLineList extends DebugLine {

    private List<String> values = new ArrayList<>();

    public DebugLineList(String id) {
        super(id);
    }

    public void setValues(List<String> values) {
        this.values = values;
        this.active = true;
    }

    public Collection<ITextComponent> toTexts(Color nameColor, Color valueColor) {
        List<ITextComponent> texts = new ArrayList<>();

        for (String v : values) {
            texts.add(Utils.getFormattedFromString(v, nameColor, valueColor));
        }
        return texts;
    }
}
