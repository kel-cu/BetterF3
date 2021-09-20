package me.treyruffy.betterf3.betterf3forge.mixin.chunk;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.ViewArea;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LevelRenderer.class)
public interface WorldRendererAccessor {

    @Accessor
    ViewArea getViewArea();

    @Invoker
    int callCountRenderedChunks();

    @Accessor
    int getLastViewDistance();

    @Accessor
    ChunkRenderDispatcher getChunkRenderDispatcher();

    @Accessor
    ClientLevel getLevel();

    @Accessor
    int getRenderedEntities();
}
