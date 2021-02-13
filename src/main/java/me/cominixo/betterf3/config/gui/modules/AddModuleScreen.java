package me.cominixo.betterf3.config.gui.modules;

import me.cominixo.betterf3.config.ModConfigFile;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.modules.EmptyModule;
import me.shedaniel.clothconfig2.forge.api.ConfigBuilder;
import me.shedaniel.clothconfig2.forge.api.ConfigCategory;
import me.shedaniel.clothconfig2.forge.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.forge.gui.entries.DropdownBoxEntry;
import me.shedaniel.clothconfig2.forge.impl.builders.DropdownMenuBuilder;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class AddModuleScreen {


    public static ConfigBuilder getConfigBuilder(ModulesScreen parent) {

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent);

        builder.setSavingRunnable(ModConfigFile.saveRunnable);


        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(new TranslationTextComponent("config.betterf3.category.general"));



        DropdownBoxEntry<BaseModule> dropdownEntry = entryBuilder.startDropdownMenu(new TranslationTextComponent("config.betterf3.add_button.module_name"),
                DropdownMenuBuilder.TopCellElementBuilder.of(new EmptyModule(true),
                        BaseModule::getModule,
                        (object) -> new StringTextComponent(object.toString()))).setSelections(BaseModule.allModules)
                .setSaveConsumer((BaseModule newValue) -> {
                    try {
                        parent.modulesListWidget.addModule(newValue.getClass().newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        parent.modulesListWidget.addModule(newValue);
                    }
                })
                .build();

        general.addEntry(dropdownEntry);
        builder.transparentBackground();

        builder.transparentBackground();

        return builder;
    }
}
