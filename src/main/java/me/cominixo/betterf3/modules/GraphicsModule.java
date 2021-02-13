package me.cominixo.betterf3.modules;

import me.treyruffy.betterf3.betterf3forge.mixin.chunk.WorldRendererAccessor;
import me.cominixo.betterf3.utils.DebugLine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.CloudOption;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.StringUtils;

public class GraphicsModule extends BaseModule {

    public GraphicsModule() {


        this.defaultNameColor = Color.fromTextFormatting(TextFormatting.GOLD);
        this.defaultValueColor = Color.fromTextFormatting(TextFormatting.AQUA);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;


        lines.add(new DebugLine("render_distance"));
        lines.add(new DebugLine("graphics"));
        lines.add(new DebugLine("clouds"));
        lines.add(new DebugLine("biome_blend_radius"));
        lines.add(new DebugLine("shader"));

    }

    public void update(Minecraft client) {

        WorldRendererAccessor worldRendererMixin = (WorldRendererAccessor) client.worldRenderer;

        String cloudString = client.gameSettings.cloudOption == CloudOption.OFF ? I18n.format("text" +
                ".betterf3.line.off")
                             : (client.gameSettings.cloudOption == CloudOption.FAST ? I18n.format("text.betterf3.line" +
                ".fast") : I18n.format("text.betterf3.line.fancy"));

        // Render Distance
        lines.get(0).setValue(worldRendererMixin.getRenderDistanceChunks());
        // Graphics
        lines.get(1).setValue(StringUtils.capitalize(client.gameSettings.graphicFanciness.toString()));
        // Clouds
        lines.get(2).setValue(cloudString);
        // Biome Blend Radius
        lines.get(3).setValue(client.gameSettings.biomeBlendRadius);

        // Shader
        ShaderGroup shaderEffect = client.gameRenderer.getShaderGroup();
        if (shaderEffect != null) {
            lines.get(4).setValue(shaderEffect.getShaderGroupName());
        } else {
            lines.get(4).active = false;
        }

        lines.get(0).inReducedDebug = true;
        lines.get(3).inReducedDebug = true;
    }

}
