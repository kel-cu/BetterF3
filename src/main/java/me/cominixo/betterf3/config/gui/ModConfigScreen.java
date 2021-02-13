package me.cominixo.betterf3.config.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.cominixo.betterf3.config.gui.modules.ModulesScreen;
import me.cominixo.betterf3.utils.PositionEnum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;


public class ModConfigScreen extends Screen {


    public ModConfigScreen() {
        super(new TranslationTextComponent("config.betterf3.title.config"));
    }
    @Override
    public void init() {

        Minecraft client = Minecraft.getInstance();


        this.addButton(new Button(this.width / 2 - 130, this.height/4, 120, 20, new TranslationTextComponent(
                "config.bettef3.order_left_button"),
                (buttonWidget) -> client.displayGuiScreen(new ModulesScreen(client.currentScreen, PositionEnum.LEFT))));
        this.addButton(new Button(this.width / 2 + 10, this.height/4, 120, 20, new TranslationTextComponent(
                "config.bettef3.order_right_button"), (buttonWidget) -> client.displayGuiScreen(new ModulesScreen(client.currentScreen, PositionEnum.RIGHT))));
        this.addButton(new Button(this.width / 2 - 130, this.height/4 - 24, 260, 20,
                new TranslationTextComponent("config.bettef3.general_settings"), (buttonWidget) -> client.displayGuiScreen(GeneralOptionsScreen.getConfigBuilder().build())));

        this.addButton(new Button(this.width / 2 - 130, this.height - 50, 260, 20,
                new TranslationTextComponent("config.betterf3.modules.done_button"), (buttonWidget) -> client.displayGuiScreen(null)));


    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredString(matrices, this.font, this.title, this.width / 2, 20, 16777215);
        super.render(matrices, mouseX, mouseY, delta);

    }


}
