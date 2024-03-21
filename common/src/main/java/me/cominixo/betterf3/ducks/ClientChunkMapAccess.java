package me.cominixo.betterf3.ducks;

import java.util.concurrent.atomic.AtomicReferenceArray;
import net.minecraft.world.level.chunk.LevelChunk;

/**
 * The Client Chunk Map Accessor.
 */
@SuppressWarnings("checkstyle:MethodName")
public interface ClientChunkMapAccess {

  /**
   * Gets the chunk array.
   *
   * @return Gets Chunks
   */
  AtomicReferenceArray<LevelChunk> getChunks();

}
