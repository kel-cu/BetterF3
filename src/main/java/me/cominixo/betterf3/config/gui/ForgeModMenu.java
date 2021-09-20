package me.cominixo.betterf3.config.gui;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fmlclient.ConfigGuiHandler;

public class ForgeModMenu {

	public static void registerModsPage() {
		ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class,
				() -> new ConfigGuiHandler.ConfigGuiFactory((client, parent) -> new ModConfigScreen(parent)));
	}
}
