package me.treyruffy.betterf3.betterf3forge.mixin.chunk;

import net.minecraft.client.multiplayer.ClientChunkProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientChunkProvider.class)
public interface ClientChunkProviderAccessor {
    @Accessor
    ClientChunkProvider.ChunkArray getArray();
}
