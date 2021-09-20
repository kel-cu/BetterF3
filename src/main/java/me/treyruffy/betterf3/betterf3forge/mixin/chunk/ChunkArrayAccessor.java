package me.treyruffy.betterf3.betterf3forge.mixin.chunk;

import net.minecraft.client.multiplayer.ClientChunkCache.Storage;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.concurrent.atomic.AtomicReferenceArray;

@Mixin(Storage.class)
public interface ChunkArrayAccessor {

    @Accessor
    AtomicReferenceArray<LevelChunk> getChunks();
}
