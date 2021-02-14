package me.cominixo.betterf3.config.gui;

import me.cominixo.betterf3.config.GeneralOptions;
import me.cominixo.betterf3.config.ModConfigFile;
import me.shedaniel.clothconfig2.forge.api.ConfigBuilder;
import me.shedaniel.clothconfig2.forge.api.ConfigCategory;
import me.shedaniel.clothconfig2.forge.api.ConfigEntryBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TranslationTextComponent;

public class GeneralOptionsScreen {
    public static ConfigBuilder getConfigBuilder() {

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(Minecraft.getInstance().currentScreen)
                .setTitle(new TranslationTextComponent("config.betterf3.title"));

        builder.setSavingRunnable(ModConfigFile.saveRunnable);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(new TranslationTextComponent("config.betterf3.title.general"));

        general.addEntry(entryBuilder.startBooleanToggle(new TranslationTextComponent("config.betterf3.disable"), GeneralOptions.disableMod)
                .setDefaultValue(false)
                .setTooltip(new TranslationTextComponent("config.betterf3.disable.tooltip"))
                .setSaveConsumer(newValue -> GeneralOptions.disableMod = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(new TranslationTextComponent("config.betterf3.space_modules"), GeneralOptions.spaceEveryModule)
                .setDefaultValue(false)
                .setTooltip(new TranslationTextComponent("config.betterf3.space_modules.tooltip"))
                .setSaveConsumer(newValue -> GeneralOptions.spaceEveryModule = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(new TranslationTextComponent("config.betterf3.shadow_text"), GeneralOptions.shadowText)
                .setDefaultValue(true)
                .setTooltip(new TranslationTextComponent("config.betterf3.shadow_text.tooltip"))
                .setSaveConsumer(newValue -> GeneralOptions.shadowText = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(new TranslationTextComponent("config.betterf3.animations"), GeneralOptions.enableAnimations)
                .setDefaultValue(true)
                .setTooltip(new TranslationTextComponent("config.betterf3.animations.tooltip"))
                .setSaveConsumer(newValue -> GeneralOptions.enableAnimations = newValue)
                .build());

        general.addEntry(entryBuilder.startDoubleField(new TranslationTextComponent("config.betterf3.animationSpeed"), GeneralOptions.animationSpeed)
                .setDefaultValue(1)
                .setMin(1).setMax(3)
                .setTooltip(new TranslationTextComponent("config.betterf3.animationSpeed.tooltip"))
                .setSaveConsumer(newValue -> GeneralOptions.animationSpeed = newValue)
                .build());

        general.addEntry(entryBuilder.startColorField(new TranslationTextComponent("config.betterf3.color.background"), GeneralOptions.backgroundColor)
                .setDefaultValue(0x6F505050)
                .setAlphaMode(true)
                .setTooltip(new TranslationTextComponent("config.betterf3.color.background.tooltip"))
                .setSaveConsumer(newValue -> GeneralOptions.backgroundColor = newValue)
                .build());

        builder.transparentBackground();
        return builder;
    }
}
