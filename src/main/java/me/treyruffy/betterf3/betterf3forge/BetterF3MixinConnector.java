package me.treyruffy.betterf3.betterf3forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class BetterF3MixinConnector implements IMixinConnector {
	@Override
	public void connect() {
		System.out.println("Invoking Mixin Connector");
		if (FMLEnvironment.dist == Dist.CLIENT) {
			Mixins.addConfiguration(
					"mixins.betterf3.json"
			);
		}
	}
}
