package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ServerModule extends BaseModule{

    public ServerModule() {

        this.defaultNameColor = Color.fromTextFormatting(TextFormatting.GRAY);
        this.defaultValueColor = Color.fromTextFormatting(TextFormatting.YELLOW);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;


        lines.add(new DebugLine("server_tick", "format.betterf3.server_tick", true));
        lines.add(new DebugLine("packets_sent"));
        lines.add(new DebugLine("packets_received"));

        for (DebugLine line : lines) {
            line.inReducedDebug = true;
        }

    }

    public void update(Minecraft client) {

        IntegratedServer integratedServer = client.getIntegratedServer();

        String serverString = "";
        if (integratedServer != null) {
            serverString = I18n.format("text.betterf3.line.integrated_server");
        } else if (client.player != null){
            serverString = client.player.getServerBrand();
        }

        if (client.getConnection() != null) {
            NetworkManager clientConnection = client.getConnection().getNetworkManager();
            float packetsSent = clientConnection.getPacketsSent();
            float packetsReceived = clientConnection.getPacketsReceived();

            lines.get(1).setValue(Math.round(packetsSent));
            lines.get(2).setValue(Math.round(packetsReceived));

        }
        String tickString = "";
        if (integratedServer != null) {
            tickString = Integer.toString(Math.round(integratedServer.getTickTime()));
        }

        List<IFormattableTextComponent> serverStringList = new LinkedList<>(Arrays.asList(Utils.getStyledText(serverString, nameColor), Utils.getStyledText(tickString, nameColor)));

        if (tickString.isEmpty()) {
            lines.get(0).setFormat("format.betterf3.no_format");
            serverStringList.remove(1);

        }

        lines.get(0).setValue(serverStringList);



    }


}
