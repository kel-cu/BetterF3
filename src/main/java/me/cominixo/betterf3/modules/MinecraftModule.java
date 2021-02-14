package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLine;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.TextFormatting;

public class MinecraftModule extends BaseModule{

    public MinecraftModule() {
        defaultNameColor = Color.fromInt(0xA0522D);
        defaultValueColor = Color.fromTextFormatting(TextFormatting.DARK_GREEN);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        lines.add(new DebugLine("minecraft", "format.betterf3.default_no_colon", false));
        lines.get(0).inReducedDebug = true;
    }

    public void update(Minecraft client) {
        lines.get(0).setValue(SharedConstants.getVersion().getName() + " (" + client.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ("release".equalsIgnoreCase(client.getVersionType()) ? "" : "/" + client.getVersionType()) + ")");
    }
}
