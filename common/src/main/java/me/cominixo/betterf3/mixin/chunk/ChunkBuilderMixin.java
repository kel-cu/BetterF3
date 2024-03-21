package me.cominixo.betterf3.mixin.chunk;

import java.util.Queue;
import me.cominixo.betterf3.ducks.ChunkBuilderAccess;
import net.minecraft.client.renderer.SectionBufferBuilderPool;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


/**
 * Mixin to access volatile "chunks" field in ClientChunkManager.
 */
@Mixin(SectionRenderDispatcher.class)
public class ChunkBuilderMixin implements ChunkBuilderAccess {

  @Shadow private volatile int toBatchCount;

  @Shadow @Final private Queue<Runnable> toUpload;

  @Final
  @Shadow private SectionBufferBuilderPool bufferPool;

  @Override
  public int betterF3$getQueuedTaskCount() {
    return this.toBatchCount;
  }

  @Override
  public Queue<Runnable> betterF3$getUploadQueue() {
    return this.toUpload;
  }

  @Override
  public int betterF3$getBufferCount() {
    return this.bufferPool.getFreeBufferCount();
  }
}
