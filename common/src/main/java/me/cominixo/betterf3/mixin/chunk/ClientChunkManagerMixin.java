package me.cominixo.betterf3.mixin.chunk;

import me.cominixo.betterf3.ducks.ClientChunkManagerAccess;
import net.minecraft.client.multiplayer.ClientChunkCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Mixin to access volatile "chunks" field in ClientChunkManager.
 */
@Mixin(ClientChunkCache.class)
public class ClientChunkManagerMixin implements ClientChunkManagerAccess {
  @Shadow volatile ClientChunkCache.Storage storage;

  @Override
  public ClientChunkCache.Storage getChunks() {
    return this.storage;
  }
}
