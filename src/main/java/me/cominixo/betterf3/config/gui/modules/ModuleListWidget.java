package me.cominixo.betterf3.config.gui.modules;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.modules.CoordsModule;
import me.cominixo.betterf3.modules.FpsModule;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ModuleListWidget extends ObjectSelectionList<ModuleListWidget.ModuleEntry> {

    ModulesScreen screen;
    List<ModuleEntry> moduleEntries = new ArrayList<>();

    public ModuleListWidget(ModulesScreen screen, Minecraft client, int width, int height, int top, int bottom, int entryHeight) {
        super(client, width, height, top, bottom, entryHeight);
        this.screen = screen;
    }

    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() + 30;
    }

    public int getRowWidth() {
        return super.getRowWidth() + 85;
    }

    @Override
    @Nonnull
    public ModuleEntry getEntry(int index) {
        return this.moduleEntries.get(index);
    }

    public void setModules(List<BaseModule> modules) {
        this.moduleEntries.clear();
        this.clearEntries();

        for (BaseModule module : modules) {
           addModule(module);
        }
    }

    public void updateModules() {
        this.clearEntries();
        this.moduleEntries.forEach(this::addEntry);
    }

    public void addModule(BaseModule module) {
        ModuleEntry entry = new ModuleEntry(this.screen, module);
        this.moduleEntries.add(entry);
        this.addEntry(entry);
    }

    public void removeModule(int index) {
        ModuleEntry entry = this.moduleEntries.get(index);
        this.moduleEntries.remove(entry);
        this.removeEntry(entry);
        //BaseModule.modules.remove(index);
    }

    @OnlyIn(Dist.CLIENT)
    public class ModuleEntry extends ObjectSelectionList.Entry<ModuleListWidget.ModuleEntry> {
        private final ModulesScreen screen;
        private final Minecraft client;
        public final BaseModule module;

        protected ModuleEntry(ModulesScreen screen, BaseModule module) {
            this.screen = screen;
            this.module = module;
            this.client = Minecraft.getInstance();
        }

        public void render(@Nonnull PoseStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX,
                           int mouseY, boolean hovered, float tickDelta) {
            this.client.font.draw(matrices, this.module.toString(), (float)(x + 32 + 3), (float)(y + 1),
                    0xffffff);

            Component exampleText;

            if (this.module instanceof CoordsModule coordsModule) {
                exampleText =  Utils.getStyledText("X", coordsModule.colorX).append(Utils.getStyledText("Y",
                                coordsModule.colorY)).append(Utils.getStyledText("Z", coordsModule.colorZ)).append(Utils.getStyledText(": ", coordsModule.nameColor))
                                .append(Utils.getStyledText("100 ", coordsModule.colorX).append(Utils.getStyledText("200 "
                                        , coordsModule.colorY)).append(Utils.getStyledText("300", coordsModule.colorZ)));

            } else if (this.module instanceof FpsModule fpsModule) {
                exampleText =  Utils.getStyledText("60 fps  ", fpsModule.colorHigh).append(Utils.getStyledText("40 fps  ",
                        fpsModule.colorMed)).append(Utils.getStyledText("10 fps", fpsModule.colorLow));
            } else if (this.module.nameColor != null && this.module.valueColor != null){
                exampleText = Utils.getStyledText("Name: ", this.module.nameColor).append(Utils.getStyledText("Value",
                        this.module.valueColor));
            } else {
                exampleText = new TextComponent("");
            }

            this.client.font.draw(matrices, exampleText, (float)(x + 40 + 3), (float)(y + 13), 0xffffff);

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            this.client.getTextureManager().bindForSetup(GuiComponent.GUI_ICONS_LOCATION);

            if (this.client.options.touchscreen || hovered) {
                RenderSystem.setShaderTexture(0, new ResourceLocation("textures/gui/server_selection.png"));
                GuiComponent.fill(matrices, x, y, x + 32, y + 32, -1601138544);
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                int v = mouseX - x;
                int w = mouseY - y;

                if (index > 0) {
                    if (v < 16 && w < 16) {
                        GuiComponent.blit(matrices, x, y, 96.0F, 32.0F, 32, 32, 256, 256);
                    } else {
                        GuiComponent.blit(matrices, x, y, 96.0F, 0.0F, 32, 32, 256, 256);
                    }
                }

                if (index < moduleEntries.size() - 1) {
                    if (v < 16 && w > 16) {
                        GuiComponent.blit(matrices, x, y, 64.0F, 32.0F, 32, 32, 256, 256);
                    } else {
                        GuiComponent.blit(matrices, x, y, 64.0F, 0.0F, 32, 32, 256, 256);
                    }
                }
            }
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            double d = mouseX - (double)this.screen.modulesListWidget.getRowLeft();
            double e = mouseY - (double)ModuleListWidget.this.getRowTop(ModuleListWidget.this.children().indexOf(this));

            if (d <= 32.0D) {
                int i = this.screen.modulesListWidget.children().indexOf(this);
                if (d < 16.0D && e < 16.0D && i > 0) {
                    this.swapEntries(i, i - 1);
                    return true;
                }

                if (d < 16.0D && e > 16.0D && i < moduleEntries.size() - 1) {
                    this.swapEntries(i, i + 1);
                    return true;
                }
            }
            ModuleListWidget.this.setSelected(this);
            this.screen.updateButtons();

            return false;
        }

        private void swapEntries(int i, int j) {
            ModuleEntry temp = moduleEntries.get(i);

            moduleEntries.set(i, moduleEntries.get(j));
            moduleEntries.set(j, temp);

            //this.screen.modulesListWidget.setModules(moduleEntries);
            //ModuleEntry entry = this.screen.modulesListWidget.children().get(j);
            this.screen.modulesListWidget.setSelected(temp);
            this.screen.updateButtons();
            this.screen.modulesListWidget.updateModules();
        }

        @Override
        public Component getNarration() {
            return new TextComponent(this.module.toString());
        }
    }
}
