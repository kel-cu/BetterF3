package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.lighting.WorldLightManager;
import net.minecraft.world.server.ServerWorld;
import org.apache.commons.lang3.text.WordUtils;

import java.util.concurrent.CompletableFuture;

public class LocationModule extends BaseModule{

    public LocationModule() {
        this.defaultNameColor = Color.fromTextFormatting(TextFormatting.DARK_GREEN);
        this.defaultValueColor = Color.fromTextFormatting(TextFormatting.AQUA);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        lines.add(new DebugLine("dimension"));
        lines.add(new DebugLine("facing"));
        lines.add(new DebugLine("rotation"));
        lines.add(new DebugLine("light"));
        lines.add(new DebugLine("light_server"));
        lines.add(new DebugLine("highest_block"));
        lines.add(new DebugLine("highest_block_server"));
        lines.add(new DebugLine("biome"));
        lines.add(new DebugLine("local_difficulty"));
        lines.add(new DebugLine("days_played"));
    }

    public void update(Minecraft client) {
        Entity cameraEntity = client.getRenderViewEntity();
        IntegratedServer integratedServer = client.getIntegratedServer();

        String chunkLightString = "";
        String chunkLightServerString = "";
        String localDifficultyString= "";
        StringBuilder highestBlock = new StringBuilder();
        StringBuilder highestBlockServer = new StringBuilder();

        if (client.world != null) {
            assert cameraEntity != null;
            BlockPos blockPos = cameraEntity.getPosition();
            ChunkPos chunkPos = new ChunkPos(blockPos);

            // Biome
            lines.get(7).setValue(client.world.func_241828_r().getRegistry(Registry.BIOME_KEY).getKey(client.world.getBiome(blockPos)));

            World serverWorld = integratedServer != null ? integratedServer.getWorld(client.world.getDimensionKey()) : client.world;
            if (client.world.chunkExists(blockPos.getX(), blockPos.getZ())) {
                Chunk clientChunk = client.world.getChunk(chunkPos.x, chunkPos.z);
                if (clientChunk.isEmpty()) {
                    chunkLightString = I18n.format("text.betterf3.line.waiting_chunk");
                } else if (serverWorld != null) {
                    // Client Chunk Lights
                    int totalLight = client.world.getChunkProvider().getLightManager().getLightSubtracted(blockPos, 0);
                    int skyLight = client.world.getLightFor(LightType.SKY, blockPos);
                    int blockLight = client.world.getLightFor(LightType.BLOCK, blockPos);
                    chunkLightString = I18n.format("format.betterf3.chunklight", totalLight,
                            skyLight, blockLight);

                    // Server Chunk Lights
                    WorldLightManager lightingProvider = serverWorld.getChunkProvider().getLightManager();

                    int skyLightServer = lightingProvider.getLightEngine(LightType.SKY).getLightFor(blockPos);
                    int blockLightServer = lightingProvider.getLightEngine(LightType.BLOCK).getLightFor(blockPos);

                    chunkLightServerString =  I18n.format("format.betterf3.chunklight_server",
                            skyLightServer, blockLightServer);

                    // Heightmap stuff (Find highest block)
                    Heightmap.Type[] heightmapTypes = Heightmap.Type.values();

                    Chunk serverChunk;

                    if (serverWorld instanceof ServerWorld) {
                        CompletableFuture<Chunk> chunkCompletableFuture =
                                ((ServerWorld)serverWorld).getChunkProvider().func_217232_b(blockPos.getX(),
                                        blockPos.getZ(), ChunkStatus.FULL, false)
                                .thenApply((either) -> either.map((chunk) -> (Chunk) chunk, (unloaded) -> null));

                        serverChunk = chunkCompletableFuture.getNow(null);
                    } else {
                        serverChunk = clientChunk;
                    }

                    for(Heightmap.Type type : heightmapTypes) {
                        // Client
                        if (type.isUsageClient()) {
                            String typeString = WordUtils.capitalizeFully(type.getId().replace("_", " "));
                            int blockY = clientChunk.getTopBlockY(type, blockPos.getX(), blockPos.getZ());
                            if (blockY > -1) {
                                highestBlock.append("  ").append(typeString).append(": ").append(blockY);
                            }
                        }

                        // Server
                        if (type.isUsageNotWorldgen() && serverWorld instanceof ServerWorld) {
                            if (serverChunk == null) {
                                serverChunk = clientChunk;
                            }

                            String typeString = Utils.enumToString(type);

                            int blockY = serverChunk.getTopBlockY(type, blockPos.getX(), blockPos.getZ());
                            if (blockY > -1) {
                                highestBlockServer.append("  ").append(typeString).append(": ").append(blockY);
                            }
                        }
                    }

                    // Local Difficulty
                    if (blockPos.getY() >= 0 && blockPos.getY() < 256) {
                        float moonSize = serverWorld.getMoonFactor();
                        long inhabitedTime;

                        if (serverChunk != null) {
                            inhabitedTime = serverChunk.getInhabitedTime();
                        } else {
                            inhabitedTime = clientChunk.getInhabitedTime();
                        }

                        DifficultyInstance localDifficulty = new DifficultyInstance(serverWorld.getDifficulty(),
                                serverWorld.getDayTime(), inhabitedTime, moonSize);

                        localDifficultyString = localDifficulty.getDifficulty().getTranslationKey() + "  " + I18n.format("text.betterf3" +
                                ".line.clamped") + ": " + localDifficulty.getAdditionalDifficulty();

                    }
                }
            }
        }

        // Dimension
        if (client.world != null) {
            lines.get(0).setValue(client.world.getDimensionKey().getLocation());
        }

        if (cameraEntity != null) {
            Direction facing = cameraEntity.getHorizontalFacing();
            String facingString = Utils.getFacingString(facing);
            // Facing
            lines.get(1).setValue(String.format("%s (%s)", I18n.format("text.betterf3.line." + facing.toString().toLowerCase()), facingString));
            // Rotation
            String yaw = String.format("%.1f", MathHelper.wrapDegrees(cameraEntity.rotationYaw));
            String pitch = String.format("%.1f", MathHelper.wrapDegrees(cameraEntity.rotationPitch));
            lines.get(2).setValue(I18n.format("format.betterf3.rotation", yaw, pitch));
        }

        // Client Light
        lines.get(3).setValue(chunkLightString);
        // Server Light
        lines.get(4).setValue(chunkLightServerString);
        // Highest Block
        lines.get(5).setValue(highestBlock.toString().trim());
        // Highest Block (Server)
        lines.get(6).setValue(highestBlockServer.toString().trim());

        // Local Difficulty
        lines.get(8).setValue(localDifficultyString);
        // Days played
        lines.get(9).setValue(client.world.getDayTime() / 24000L);
    }
}
