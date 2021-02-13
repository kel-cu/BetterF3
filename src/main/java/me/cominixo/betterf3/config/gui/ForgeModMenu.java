package me.cominixo.betterf3.config.gui;

import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;

public class ForgeModMenu {

	public static void registerModsPage() {
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (client, parent) -> GeneralOptionsScreen.getConfigBuilder().setParentScreen(parent).build());
	}

}
