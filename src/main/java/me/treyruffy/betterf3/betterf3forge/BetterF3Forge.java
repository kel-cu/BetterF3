package me.treyruffy.betterf3.betterf3forge;

import me.cominixo.betterf3.config.ModConfigFile;
import me.cominixo.betterf3.config.gui.ForgeModMenu;
import me.cominixo.betterf3.modules.*;
import me.cominixo.betterf3.utils.PositionEnum;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("betterf3forge")
public class BetterF3Forge {

	// Directly reference a log4j logger.
	private static final Logger LOGGER = LogManager.getLogger();

	public BetterF3Forge() {
		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);

		if (ModList.get().isLoaded("cloth-config"))
			DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ForgeModMenu::registerModsPage);
		else
			LOGGER.info(I18n.format("config.betterf3.need_cloth_config"));

		// Make sure the mod being absent on the other network side does not cause the client to display the server
		// as incompatible
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
	}

	private void setup(final FMLCommonSetupEvent event) {
		LOGGER.info("[BetterF3] Loading...");

		// Init all modules and add spaces (default order)
		new MinecraftModule().init();
		new FpsModule().init();
		new GraphicsModule().init();
		new ServerModule().init();
		new CoordsModule().init();
		new ChunksModule().init();
		new LocationModule().init();
		new EntityModule().init();
		new SoundModule().init();
		new HelpModule().init();
		BaseModule.modules.add(EmptyModule.INSTANCE);
		new MiscLeftModule().init();

		new SystemModule().init(PositionEnum.RIGHT);
		new MiscRightModule().init(PositionEnum.RIGHT);
		BaseModule.modulesRight.add(EmptyModule.INSTANCE);
		new TargetModule().init(PositionEnum.RIGHT);

		// Config
		ModConfigFile.load();

		LOGGER.info("[BetterF3] All done!");
	}
}