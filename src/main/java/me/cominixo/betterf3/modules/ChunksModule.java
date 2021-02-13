package me.cominixo.betterf3.modules;

import com.mojang.datafixers.DataFixUtils;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import me.treyruffy.betterf3.betterf3forge.mixin.chunk.ChunkArrayAccessor;
import me.treyruffy.betterf3.betterf3forge.mixin.chunk.ChunkRenderDispatcherAccessor;
import me.treyruffy.betterf3.betterf3forge.mixin.chunk.ClientChunkProviderAccessor;
import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import me.treyruffy.betterf3.betterf3forge.mixin.chunk.WorldRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientChunkProvider;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ChunksModule extends BaseModule{

    public final Color totalColor = Color.fromTextFormatting(TextFormatting.GOLD);

    public ChunksModule() {


        this.defaultNameColor = Color.fromInt(0x00aaff);
        this.defaultValueColor = Color.fromTextFormatting(TextFormatting.YELLOW);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        lines.add(new DebugLine("chunk_sections", "format.betterf3.total", true));
        lines.add(new DebugLine("chunk_culling"));
        lines.add(new DebugLine("pending_chunks"));
        lines.add(new DebugLine("pending_uploads"));
        lines.add(new DebugLine("available_buffers"));
        lines.add(new DebugLine("client_chunk_cache"));
        lines.add(new DebugLine("loaded_chunks"));
        lines.add(new DebugLine("loaded_chunks_server"));
        lines.add(new DebugLine("forceloaded_chunks"));
        lines.add(new DebugLine("spawn_chunks"));

        lines.get(0).inReducedDebug = true;
        lines.get(2).inReducedDebug = true;
        lines.get(3).inReducedDebug = true;
        lines.get(4).inReducedDebug = true;
        lines.get(5).inReducedDebug = true;

    }

    public void update(Minecraft client) {



        WorldRendererAccessor worldRendererMixin = (WorldRendererAccessor) client.worldRenderer;
        int totalChunks = worldRendererMixin.getViewFrustum().renderChunks.length;
        int renderedChunks = worldRendererMixin.callGetRenderedChunks();

        ChunkRenderDispatcher chunkBuilder = worldRendererMixin.getRenderDispatcher();
        ChunkRenderDispatcherAccessor chunkBuilderAccessor = (ChunkRenderDispatcherAccessor) chunkBuilder;

        if (client.world != null) {
            ClientChunkProvider clientChunkManager = client.world.getChunkProvider();
            ClientChunkProviderAccessor clientChunkManagerMixin = (ClientChunkProviderAccessor) clientChunkManager;
            ChunkArrayAccessor clientChunkMapMixin = (ChunkArrayAccessor) (Object) clientChunkManagerMixin.getArray();

            // Client Chunk Cache
            lines.get(5).setValue(clientChunkMapMixin.getChunks().length());
            // Loaded Chunks
            lines.get(6).setValue(clientChunkManager.getLoadedChunksCount());

        }


        World world =
                DataFixUtils.orElse(Optional.ofNullable(client.getIntegratedServer()).flatMap((integratedServer) -> Optional.ofNullable(integratedServer.getWorld(client.world.getDimensionKey()))), client.world);
        LongSet forceLoadedChunks = world instanceof ServerWorld ? ((ServerWorld)world).getForcedChunks() : LongSets.EMPTY_SET;

        IntegratedServer integratedServer = client.getIntegratedServer();
        ServerWorld serverWorld = integratedServer != null ? integratedServer.getWorld(client.world.getDimensionKey()) : null;

        WorldEntitySpawner.EntityDensityManager info = null;
        if (serverWorld != null) {
             info = serverWorld.getChunkProvider().func_241101_k_();
        }


        String chunkCulling = client.renderChunksMany ? TextFormatting.GREEN + I18n.format("text.betterf3.line" +
                ".enabled") : TextFormatting.RED + I18n.format("text.betterf3.line.disabled");

        List<ITextComponent> chunkValues = Arrays.asList(Utils.getStyledText(I18n.format("text.betterf3.line.rendered"),
                valueColor), Utils.getStyledText(I18n.format("text.betterf3.line.total"), totalColor),
                Utils.getStyledText(Integer.toString(renderedChunks), valueColor), Utils.getStyledText(Integer.toString(totalChunks), totalColor));

        // Chunk Sections
        lines.get(0).setValue(chunkValues);
        // Chunk Culling
        lines.get(1).setValue(chunkCulling);
        // Pending Chunks
        lines.get(2).setValue(chunkBuilderAccessor.getCountRenderTasks());
        // Pending Uploads to GPU
        lines.get(3).setValue(chunkBuilderAccessor.getUploadTasks().size());
        // Available Buffers
        lines.get(4).setValue(chunkBuilderAccessor.getCountFreeBuilders());

        // Loaded Chunks (Server)
        if (serverWorld != null) {
            lines.get(7).setValue(serverWorld.getChunkProvider().getLoadedChunkCount());
        }
        // Forceloaded Chunks
        lines.get(8).setValue(forceLoadedChunks.size());
        // Spawn Chunks
        if (info != null) {
            lines.get(9).setValue(info.func_234988_a_());
        }


        
    }
}
