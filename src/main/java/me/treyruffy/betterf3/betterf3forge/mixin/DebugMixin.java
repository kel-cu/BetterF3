package me.treyruffy.betterf3.betterf3forge.mixin;

import com.google.common.base.Strings;
import com.mojang.blaze3d.vertex.PoseStack;
import me.cominixo.betterf3.config.GeneralOptions;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.modules.MiscLeftModule;
import me.cominixo.betterf3.modules.MiscRightModule;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(DebugScreenOverlay.class)
public abstract class DebugMixin {

    @Shadow @Final private Minecraft minecraft;
    @Shadow @Final private Font font;

    @Shadow protected abstract List<String> getGameInformation();

    @Shadow protected abstract List<String> getSystemInformation();

    public List<Component> getNewLeftText() {
        List<Component> list = new ArrayList<>();

        for (BaseModule module : BaseModule.modules) {
            if (!module.enabled) {
                continue;
            }
            if (module instanceof MiscLeftModule) {
              ((MiscLeftModule) module).update(getGameInformation());
            } else {
                module.update(minecraft);
            }

            list.addAll(module.getLinesFormatted(minecraft.showOnlyReducedInfo()));
            if (GeneralOptions.spaceEveryModule) {
                list.add(new TextComponent(""));
            }
        }
        return list;
    }

    public List<Component> getNewRightText() {
        List<Component> list = new ArrayList<>();

        for (BaseModule module : BaseModule.modulesRight) {
            if (!module.enabled) {
                continue;
            }
            if (module instanceof MiscRightModule) {
                ((MiscRightModule) module).update(getSystemInformation());
            } else {
                module.update(minecraft);
            }

            list.addAll(module.getLinesFormatted(minecraft.showOnlyReducedInfo()));
            if (GeneralOptions.spaceEveryModule) {
                list.add(new TextComponent(""));
            }
        }
        return list;
    }

    @Inject(method = "drawGameInformation", at = @At("HEAD"), cancellable = true)
    public void drawGameInformation(PoseStack matrixStack, CallbackInfo ci) {
        if (GeneralOptions.disableMod) {
            return;
        }
        List<Component> list = getNewRightText();

        for (int i = 0; i < list.size(); i++) {
            if (!Strings.isNullOrEmpty(list.get(i).getString())) {
                int height = 9;
                int width = this.font.width(list.get(i).getString());
                int windowWidth = this.minecraft.getWindow().getGuiScaledWidth() - 2 - width;
                if (GeneralOptions.enableAnimations) {
                    windowWidth += Utils.xPos;
                }
                int y = 2 + height * i;

                GuiComponent.fill(matrixStack, windowWidth - 1, y - 1, windowWidth + width + 1, y + height - 1,
                        GeneralOptions.backgroundColor);

                if (GeneralOptions.shadowText) {
                    this.font.drawShadow(matrixStack, list.get(i), windowWidth, (float)y, 0xE0E0E0);
                } else {
                    this.font.draw(matrixStack, list.get(i), windowWidth, (float)y, 0xE0E0E0);
                }
            }
        }
        ci.cancel();
    }

    @Inject(method = "drawSystemInformation", at = @At("HEAD"), cancellable = true)
    public void drawSystemInformation(PoseStack matrixStack, CallbackInfo ci) {
        if (GeneralOptions.disableMod) {
            return;
        }
        List<Component> list = getNewLeftText();

        for (int i = 0; i < list.size(); i++) {
            if (!Strings.isNullOrEmpty(list.get(i).getString())) {
                int height = 9;
                int width = this.font.width(list.get(i).getString());
                int y = 2 + height * i;
                int xPosLeft = 2;

                if (GeneralOptions.enableAnimations) {
                    xPosLeft -= Utils.xPos;
                }

                GuiComponent.fill(matrixStack, 1 + xPosLeft, y - 1, width + 3 + xPosLeft, y + height - 1,
                        GeneralOptions.backgroundColor);

                if (GeneralOptions.shadowText) {
                    this.font.drawShadow(matrixStack, list.get(i), xPosLeft, (float)y, 0xE0E0E0);
                } else {
                    this.font.draw(matrixStack, list.get(i), xPosLeft, (float) y, 0xE0E0E0);
                }
            }
        }
        ci.cancel();
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void renderAnimation(PoseStack matrices, CallbackInfo ci) {
        if (!GeneralOptions.enableAnimations) {
            return;
        }
        long time = Util.getMillis();
        if (time - Utils.lastAnimationUpdate >= 10 && (Utils.xPos != 0 || Utils.closingAnimation)) {

            int i = ((Utils.START_X_POS/2 + Utils.xPos) / 10)-9;

            if (Utils.xPos != 0 && !Utils.closingAnimation) {
                Utils.xPos /= GeneralOptions.animationSpeed;
                Utils.xPos -= i;
            }

            if (i == 0) {
                i = 1;
            }

            if (Utils.closingAnimation) {
                Utils.xPos += i;
                Utils.xPos *= GeneralOptions.animationSpeed;

                if (Utils.xPos >= 300) {
                    this.minecraft.options.renderDebug = false;
                    Utils.closingAnimation = false;
                }
            }
            Utils.lastAnimationUpdate = time;
        }
    }
}
