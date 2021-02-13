package me.treyruffy.betterf3.betterf3forge.mixin.chunk;

import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Queue;

@Mixin(ChunkRenderDispatcher.class)
public interface ChunkRenderDispatcherAccessor {

    @Accessor
    int getCountRenderTasks();

    @Accessor
    Queue<Runnable> getUploadTasks();

    @Accessor
    int getCountFreeBuilders();

}
