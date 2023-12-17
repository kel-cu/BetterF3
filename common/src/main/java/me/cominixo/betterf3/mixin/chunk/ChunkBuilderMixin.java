package me.cominixo.betterf3.mixin.chunk;

import java.util.Queue;
import me.cominixo.betterf3.ducks.ChunkBuilderAccess;
import net.minecraft.client.render.chunk.BlockBufferBuilderPool;
import net.minecraft.client.render.chunk.ChunkBuilder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


/**
 * Mixin to access volatile "chunks" field in ClientChunkManager.
 */
@Mixin(ChunkBuilder.class)
public class ChunkBuilderMixin implements ChunkBuilderAccess {

  @Shadow private volatile int queuedTaskCount;

  @Shadow @Final private Queue<Runnable> uploadQueue;

  @Final
  @Shadow private BlockBufferBuilderPool buffersPool;

  @Override
  public int betterF3$getQueuedTaskCount() {
    return this.queuedTaskCount;
  }

  @Override
  public Queue<Runnable> betterF3$getUploadQueue() {
    return this.uploadQueue;
  }

  @Override
  public int betterF3$getBufferCount() {
    return this.buffersPool.getAvailableBuilderCount();
  }
}
