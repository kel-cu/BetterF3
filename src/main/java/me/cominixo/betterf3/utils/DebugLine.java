package me.cominixo.betterf3.utils;

import net.minecraft.util.text.*;

import java.util.ArrayList;
import java.util.List;

public class DebugLine {

    private Object value;
    private String format;
    private final String id;

    public boolean active = true;
    public boolean enabled = true;
    public boolean isCustom = false;
    public boolean inReducedDebug = false;

    public DebugLine(String id) {
        if (id.startsWith("nothing")) {
            this.id = "";
        } else {
            this.id = id;
        }
        this.format = "format.betterf3.default_format";
        this.value = "";
    }


    public DebugLine(String id, String formatString, boolean isCustom) {

        this.id = id;
        this.value = "";
        this.format = formatString;
        this.isCustom = isCustom;

    }

    public ITextComponent toText(Color nameColor, Color valueColor) {

        String name = this.getName();

        ITextComponent nameStyled = Utils.getStyledText(name, nameColor);
        ITextComponent valueStyled;

        if (this.value instanceof ITextComponent) {
            valueStyled = (ITextComponent) this.value;
        } else {
            valueStyled = Utils.getStyledText(this.value, valueColor);
        }

        if (this.value == null || this.value.toString().equals("") || this.value.toString().isEmpty()) {
            this.active = false;
        }


        return new TranslationTextComponent(format, nameStyled, valueStyled);
    }


    public ITextComponent toTextCustom(Color nameColor) {

        String name = this.getName();

        if (value instanceof List) {
            // format properly if value is a List (bad)
            List<Object> values = new ArrayList<>();
            List<?> value = (List<?>) this.value;


            if (!name.equals("")) {
                values.add(Utils.getStyledText(name, nameColor));
            }

            values.addAll(value);
            return new TranslationTextComponent(format, values.toArray()).modifyStyle(style -> style.setColor(nameColor));
        } else {
            return new TranslationTextComponent(format, name, value);
        }

    }


    public void setValue(Object value) {
        this.active = true;
        this.value = value;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getName() {
        if (id.isEmpty()) {
            this.format = "%s%s";
            return "";
        }
        LanguageMap language = LanguageMap.getInstance();
        return language.func_230503_a_("text.betterf3.line." + id);
    }

    public String getId() {
        return id;
    }


}
