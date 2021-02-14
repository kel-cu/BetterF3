package me.treyruffy.betterf3.betterf3forge.mixin.chunk;

import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WorldRenderer.class)
public interface WorldRendererAccessor {

    @Accessor
    ViewFrustum getViewFrustum();

    @Invoker
    int callGetRenderedChunks();

    @Accessor
    int getRenderDistanceChunks();

    @Accessor
    ChunkRenderDispatcher getRenderDispatcher();

    @Accessor
    ClientWorld getWorld();

    @Accessor
    int getCountEntitiesRendered();
}
