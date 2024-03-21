package me.cominixo.betterf3.mixin.chunk;

import java.util.concurrent.atomic.AtomicReferenceArray;
import me.cominixo.betterf3.ducks.ClientChunkMapAccess;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Mixin to access volatile "chunks" field in ClientChunkManager.
 */
@Mixin(ClientChunkCache.Storage.class)
public class ClientChunkMapMixin implements ClientChunkMapAccess {

  @Final
  @Shadow AtomicReferenceArray<LevelChunk> chunks;

  @Override
  public AtomicReferenceArray<LevelChunk> getChunks() {
    return this.chunks;
  }
}
