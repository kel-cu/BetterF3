package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;

public class CoordsModule extends BaseModule{


    public Color colorX;
    public Color colorY;
    public Color colorZ;

    public Color defaultColorX = Color.fromTextFormatting(TextFormatting.RED);
    public Color defaultColorY = Color.fromTextFormatting(TextFormatting.GREEN);
    public Color defaultColorZ = Color.fromTextFormatting(TextFormatting.AQUA);

    public CoordsModule() {

        this.defaultNameColor = Color.fromTextFormatting(TextFormatting.RED);

        this.nameColor = defaultNameColor;
        this.colorX = defaultColorX;
        this.colorY = defaultColorY;
        this.colorZ = defaultColorZ;


        lines.add(new DebugLine("player_coords", "format.betterf3.coords", true));
        lines.add(new DebugLine("block_coords", "format.betterf3.coords", true));
        lines.add(new DebugLine("chunk_relative_coords", "format.betterf3.coords", true));
        lines.add(new DebugLine("chunk_coords", "format.betterf3.coords", true));

        lines.get(2).inReducedDebug = true;

    }

    public void update(Minecraft client) {

        Entity cameraEntity = client.getRenderViewEntity();

        ITextComponent xyz = Utils.getStyledText("X", colorX).append(Utils.getStyledText("Y", colorY)).append(Utils.getStyledText("Z", colorZ));

        if (cameraEntity != null) {


            String cameraX = String.format("%.3f", cameraEntity.getPosX());
            String cameraY = String.format("%.5f", cameraEntity.getPosY());
            String cameraZ = String.format("%.3f", cameraEntity.getPosZ());

            // Player coords
            lines.get(0).setValue(Arrays.asList(xyz, Utils.getStyledText(cameraX, colorX),
                    Utils.getStyledText(cameraY, colorY), Utils.getStyledText(cameraZ, colorZ)));

            BlockPos blockPos = cameraEntity.getPosition();
            // Block coords
            lines.get(1).setValue(Arrays.asList(Utils.getStyledText(blockPos.getX(), colorX),
                    Utils.getStyledText(blockPos.getY(), colorY), Utils.getStyledText(blockPos.getZ(), colorZ)));
            // Chunk Relative coords
            lines.get(2).setValue(Arrays.asList(Utils.getStyledText(blockPos.getX() & 15, colorX),
                    Utils.getStyledText(blockPos.getY() & 15, colorY), Utils.getStyledText(blockPos.getZ() & 15, colorZ)));
            // Chunk coords
            lines.get(3).setValue(Arrays.asList(Utils.getStyledText(blockPos.getX() >> 4, colorX),
                    Utils.getStyledText(blockPos.getY() >> 4, colorY), Utils.getStyledText(blockPos.getZ() >> 4, colorZ)));
        }



    }

}
