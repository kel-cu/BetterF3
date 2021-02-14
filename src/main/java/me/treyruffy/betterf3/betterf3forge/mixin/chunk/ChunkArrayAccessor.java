package me.treyruffy.betterf3.betterf3forge.mixin.chunk;

import net.minecraft.client.multiplayer.ClientChunkProvider.ChunkArray;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.concurrent.atomic.AtomicReferenceArray;

@Mixin(ChunkArray.class)
public interface ChunkArrayAccessor {

    @Accessor
    AtomicReferenceArray<Chunk> getChunks();
}
