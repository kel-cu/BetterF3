package me.cominixo.betterf3.config.gui.modules;

import me.cominixo.betterf3.config.ModConfigFile;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.modules.CoordsModule;
import me.cominixo.betterf3.modules.FpsModule;
import me.cominixo.betterf3.utils.DebugLine;
import me.shedaniel.clothconfig2.forge.api.ConfigBuilder;
import me.shedaniel.clothconfig2.forge.api.ConfigCategory;
import me.shedaniel.clothconfig2.forge.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.forge.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.forge.gui.entries.ColorEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.text.WordUtils;

public class EditModulesScreen {

    public static ConfigBuilder getConfigBuilder(BaseModule module) {

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(Minecraft.getInstance().currentScreen);



        builder.setSavingRunnable(ModConfigFile.saveRunnable);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(new TranslationTextComponent("config.betterf3.category.general"));


        BooleanListEntry moduleEnabled = entryBuilder.startBooleanToggle(new TranslationTextComponent("config.betterf3.module.enable"), module.enabled)
                .setDefaultValue(true)
                .setTooltip(new TranslationTextComponent("config.betterf3.module.enable.tooltip"))
                .setSaveConsumer(newValue -> {
                    module.enabled = newValue;
                    module.setEnabled(newValue);
                })
                .build();


        general.addEntry(moduleEnabled);

        if (module instanceof CoordsModule) {
            CoordsModule coordsModule = (CoordsModule) module;

            if (coordsModule.colorX != null && coordsModule.defaultColorX != null) {
                ColorEntry colorX = entryBuilder.startColorField(new TranslationTextComponent("config.betterf3.color" +
                        ".x"), coordsModule.colorX.getColor())
                        .setDefaultValue(coordsModule.defaultColorX.getColor())
                        .setTooltip(new TranslationTextComponent("config.betterf3.color.x.tooltip"))
                        .setSaveConsumer(newValue -> coordsModule.colorX = Color.fromInt(newValue))
                        .build();

                general.addEntry(colorX);
            }
            if (coordsModule.colorY != null && coordsModule.defaultColorY != null) {
                ColorEntry colorY = entryBuilder.startColorField(new TranslationTextComponent("config.betterf3.color" +
                        ".y"), coordsModule.colorY.getColor())
                        .setDefaultValue(coordsModule.defaultColorY.getColor())
                        .setTooltip(new TranslationTextComponent("config.betterf3.color.y.tooltip"))
                        .setSaveConsumer(newValue -> coordsModule.colorY = Color.fromInt(newValue))
                        .build();

                general.addEntry(colorY);
            }
            if (coordsModule.colorZ != null && coordsModule.defaultColorZ != null) {
                ColorEntry colorZ = entryBuilder.startColorField(new TranslationTextComponent("config.betterf3.color" +
                        ".z"), coordsModule.colorZ.getColor())
                        .setDefaultValue(coordsModule.defaultColorZ.getColor())
                        .setTooltip(new TranslationTextComponent("config.betterf3.color.z.tooltip"))
                        .setSaveConsumer(newValue -> coordsModule.colorZ = Color.fromInt(newValue))
                        .build();

                general.addEntry(colorZ);
            }

        }

        if (module instanceof FpsModule) {
            FpsModule fpsModule = (FpsModule) module;

            if (fpsModule.colorHigh != null && fpsModule.defaultColorHigh != null) {
                ColorEntry colorHigh = entryBuilder.startColorField(new TranslationTextComponent("config.betterf3" +
                        ".color.fps.high"), fpsModule.colorHigh.getColor())
                        .setDefaultValue(fpsModule.defaultColorHigh.getColor())
                        .setTooltip(new TranslationTextComponent("config.betterf3.color.fps.high.tooltip"))
                        .setSaveConsumer(newValue -> fpsModule.colorHigh = Color.fromInt(newValue))
                        .build();

                general.addEntry(colorHigh);
            }
            if (fpsModule.colorMed != null && fpsModule.defaultColorMed != null) {
                ColorEntry colorMed = entryBuilder.startColorField(new TranslationTextComponent("config.betterf3" +
                        ".color.fps.medium"), fpsModule.colorMed.getColor())
                        .setDefaultValue(fpsModule.defaultColorMed.getColor())
                        .setTooltip(new TranslationTextComponent("config.betterf3.color.fps.medium.tooltip"))
                        .setSaveConsumer(newValue -> fpsModule.colorMed = Color.fromInt(newValue))
                        .build();

                general.addEntry(colorMed);
            }
            if (fpsModule.colorLow != null && fpsModule.defaultColorLow != null) {
                ColorEntry colorLow = entryBuilder.startColorField(new TranslationTextComponent("config.betterf3" +
                        ".color.fps.low"), fpsModule.colorLow.getColor())
                        .setDefaultValue(fpsModule.defaultColorLow.getColor())
                        .setTooltip(new TranslationTextComponent("config.betterf3.color.fps.low.tooltip"))
                        .setSaveConsumer(newValue -> fpsModule.colorLow = Color.fromInt(newValue))
                        .build();

                general.addEntry(colorLow);
            }

        }


        if (module.nameColor != null && module.defaultNameColor != null) {
            ColorEntry nameColor = entryBuilder.startColorField(new TranslationTextComponent("config.betterf3.color" +
                    ".name"), module.nameColor.getColor())
                    .setDefaultValue(module.defaultNameColor.getColor())
                    .setTooltip(new TranslationTextComponent("config.betterf3.color.name.tooltip"))
                    .setSaveConsumer(newValue -> module.nameColor = Color.fromInt(newValue))
                    .build();

            general.addEntry(nameColor);
        }

        if (module.valueColor != null && module.defaultValueColor != null) {
            ColorEntry valueColor = entryBuilder.startColorField(new TranslationTextComponent("config.betterf3.color" +
                    ".value"), module.valueColor.getColor())
                    .setDefaultValue(module.defaultValueColor.getColor())
                    .setTooltip(new TranslationTextComponent("config.betterf3.color.value.tooltip"))
                    .setSaveConsumer(newValue -> module.valueColor = Color.fromInt(newValue))
                    .build();


            general.addEntry(valueColor);
        }


        if (module.getLines().size() > 1) {
            for (DebugLine line : module.getLines()) {

                if (line.getId().equals("")) {
                    continue;
                }

                ITextComponent name = new TranslationTextComponent("text.betterf3.line." + line.getId());

                if (name.getString().equals("")) {
                    name = new StringTextComponent(WordUtils.capitalizeFully(line.getId().replace("_", " ")));
                }

                BooleanListEntry enabled = entryBuilder.startBooleanToggle(name, line.enabled)
                        .setDefaultValue(true)
                        .setTooltip(new TranslationTextComponent("config.betterf3.disable_line.tooltip"))
                        .setSaveConsumer(newValue -> line.enabled = newValue)
                        .build();

                general.addEntry(enabled);

            }
        }


        builder.transparentBackground();

        return builder;

    }
}
