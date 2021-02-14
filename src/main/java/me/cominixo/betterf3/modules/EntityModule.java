package me.cominixo.betterf3.modules;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import me.treyruffy.betterf3.betterf3forge.mixin.chunk.WorldRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityClassification;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;

import java.util.Arrays;
import java.util.List;

public class EntityModule extends BaseModule {

    public final Color totalColor = Color.fromTextFormatting(TextFormatting.GOLD);

    public EntityModule() {
        this.defaultNameColor = Color.fromTextFormatting(TextFormatting.RED);
        this.defaultValueColor = Color.fromTextFormatting(TextFormatting.YELLOW);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        lines.add(new DebugLine("particles"));
        lines.add(new DebugLine("entities", "format.betterf3.total", true));

        // Monster, Creature, Ambient, Water Creature, Water Ambient, Misc
        for (EntityClassification spawnGroup : EntityClassification.values()) {
            String name = spawnGroup.toString().toLowerCase();
            lines.add(new DebugLine(name));
        }

        lines.get(0).inReducedDebug = true;
        lines.get(1).inReducedDebug = true;

    }
    public void update(Minecraft client) {
        WorldRendererAccessor worldRendererMixin = (WorldRendererAccessor) client.worldRenderer;

        List<ITextComponent> entityValues = Arrays.asList(Utils.getStyledText(I18n.format("text.betterf3.line.rendered"),
                valueColor), Utils.getStyledText(I18n.format("text.betterf3.line.total"), totalColor),
                Utils.getStyledText(worldRendererMixin.getCountEntitiesRendered(), valueColor),
                Utils.getStyledText(worldRendererMixin.getWorld().getCountLoadedEntities(), totalColor));

        IntegratedServer integratedServer = client.getIntegratedServer();

        if (client.world != null) {
            ServerWorld serverWorld = integratedServer != null ? integratedServer.getWorld(client.world.getDimensionKey()) : null;
            if (serverWorld != null) {
                WorldEntitySpawner.EntityDensityManager info = serverWorld.getChunkProvider().func_241101_k_();
                if (info != null) {
                    Object2IntMap<EntityClassification> spawnGroupCount = info.func_234995_b_();
                    // Entities (separated) (kinda bad)
                    for (int i = 0; i < EntityClassification.values().length; i++) {
                        EntityClassification group = EntityClassification.values()[i];
                        lines.get(i+2).setValue(spawnGroupCount.getInt(group));
                    }
                }
            }
        }
        // Particles
        lines.get(0).setValue(client.particles.getStatistics());
        // Entities
        lines.get(1).setValue(entityValues);
    }
}
