package me.treyruffy.betterf3;

import me.cominixo.betterf3.config.gui.ModConfigScreen;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

/**
 * Used to create mod configuration in the NeoForge mod menu.
 */
public final class NeoForgeModMenu {

  private NeoForgeModMenu() {
    // Do nothing
  }

  /**
   * Registers BetterF3 in the mod menu.
   */
  public static void registerModsPage() {
    ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class,
    () -> (client, parent) -> new ModConfigScreen(parent));
  }
}
