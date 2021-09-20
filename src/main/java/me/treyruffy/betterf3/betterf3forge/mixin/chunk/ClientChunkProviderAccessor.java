package me.treyruffy.betterf3.betterf3forge.mixin.chunk;

import net.minecraft.client.multiplayer.ClientChunkCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientChunkCache.class)
public interface ClientChunkProviderAccessor {
    @Accessor
    ClientChunkCache.Storage getStorage();
}
