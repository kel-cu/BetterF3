package me.cominixo.betterf3.config.gui.modules;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.modules.CoordsModule;
import me.cominixo.betterf3.modules.FpsModule;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ModuleListWidget extends ExtendedList<ModuleListWidget.ModuleEntry> {

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
    public class ModuleEntry extends ExtendedList.AbstractListEntry<ModuleListWidget.ModuleEntry> {
        private final ModulesScreen screen;
        private final Minecraft client;
        public final BaseModule module;

        protected ModuleEntry(ModulesScreen screen, BaseModule module) {
            this.screen = screen;
            this.module = module;
            this.client = Minecraft.getInstance();
        }

        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.client.fontRenderer.drawString(matrices, this.module.toString(), (float)(x + 32 + 3), (float)(y + 1), 0xffffff);

            ITextComponent exampleText;

            if (this.module instanceof CoordsModule) {
                CoordsModule coordsModule = (CoordsModule) this.module;
                exampleText =  Utils.getStyledText("X", coordsModule.colorX).appendSibling(Utils.getStyledText("Y", coordsModule.colorY)).appendSibling(Utils.getStyledText("Z", coordsModule.colorZ)).appendSibling(Utils.getStyledText(": ", coordsModule.nameColor))
                                .appendSibling(Utils.getStyledText("100 ", coordsModule.colorX).appendSibling(Utils.getStyledText("200 ", coordsModule.colorY)).appendSibling(Utils.getStyledText("300", coordsModule.colorZ)));

            } else if (this.module instanceof FpsModule) {
                FpsModule fpsModule = (FpsModule) this.module;
                exampleText =  Utils.getStyledText("60 fps  ", fpsModule.colorHigh).appendSibling(Utils.getStyledText("40 fps  ", fpsModule.colorMed)).appendSibling(Utils.getStyledText("10 fps", fpsModule.colorLow));
            } else if (this.module.nameColor != null && this.module.valueColor != null){
                exampleText = Utils.getStyledText("Name: ", this.module.nameColor).appendSibling(Utils.getStyledText("Value", this.module.valueColor));
            } else {
                exampleText = new StringTextComponent("");
            }

            this.client.fontRenderer.drawText(matrices, exampleText, (float)(x + 40 + 3), (float)(y + 13), 0xffffff);

            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.client.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);

            if (this.client.gameSettings.touchscreen || hovered) {
                this.client.getTextureManager().bindTexture(new ResourceLocation("textures/gui/server_selection.png"));
                int v = mouseX - x;
                int w = mouseY - y;

                if (index > 0) {
                    if (v < 16 && w < 16) {
                        AbstractGui.blit(matrices, x, y, 96.0F, 32.0F, 32, 32, 256, 256);
                    } else {
                        AbstractGui.blit(matrices, x, y, 96.0F, 0.0F, 32, 32, 256, 256);
                    }
                }

                if (index < moduleEntries.size() - 1) {
                    if (v < 16 && w > 16) {
                        AbstractGui.blit(matrices, x, y, 64.0F, 32.0F, 32, 32, 256, 256);
                    } else {
                        AbstractGui.blit(matrices, x, y, 64.0F, 0.0F, 32, 32, 256, 256);
                    }
                }
            }
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            double d = mouseX - (double)this.screen.modulesListWidget.getRowLeft();
            double e = mouseY - (double)ModuleListWidget.this.getRowTop(ModuleListWidget.this.getEventListeners().indexOf(this));

            if (d <= 32.0D) {
                int i = this.screen.modulesListWidget.getEventListeners().indexOf(this);
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
    }
}
