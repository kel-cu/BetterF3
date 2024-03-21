package me.cominixo.betterf3.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * FPS count accessor.
 */
@Mixin(Minecraft.class)
public interface ClientAccessor {
  /**
   * Gets the current client FPS.
   *
   * @return the current client FPS
   */
  @Accessor("fps")
  static int betterF3$getFps() {
    throw new AssertionError();
  }
}
