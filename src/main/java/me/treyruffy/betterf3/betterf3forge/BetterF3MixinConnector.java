package me.treyruffy.betterf3.betterf3forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class BetterF3MixinConnector implements IMixinConnector {

	private static final Logger logger = LogManager.getLogger("BetterF3");

	@Override
	public void connect() {
		logger.info("Invoking Mixin Connector");
		if (FMLEnvironment.dist == Dist.CLIENT) {
			Mixins.addConfiguration(
					"mixins.betterf3.json"
			);
		}
	}
}
