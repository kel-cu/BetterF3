package me.treyruffy.betterf3.betterf3forge.mixin;

import com.google.common.base.Strings;
import com.mojang.blaze3d.matrix.MatrixStack;
import me.cominixo.betterf3.config.GeneralOptions;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.modules.MiscLeftModule;
import me.cominixo.betterf3.modules.MiscRightModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.overlay.DebugOverlayGui;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

import static me.cominixo.betterf3.utils.Utils.*;
import static net.minecraft.client.gui.AbstractGui.fill;

@Mixin(DebugOverlayGui.class)
public abstract class DebugMixin {

    @Shadow @Final private Minecraft mc;
    @Shadow @Final private FontRenderer fontRenderer;

    @Shadow protected abstract List<String> getDebugInfoLeft();

    @Shadow protected abstract List<String> getDebugInfoRight();


    public List<ITextComponent> getNewLeftText() {

        List<ITextComponent> list = new ArrayList<>();

        for (BaseModule module : BaseModule.modules) {
            if (!module.enabled) {
                continue;
            }
            if (module instanceof MiscLeftModule) {
              ((MiscLeftModule) module).update(getDebugInfoLeft());
            } else {
                module.update(mc);
            }

            list.addAll(module.getLinesFormatted(mc.isReducedDebug()));
            if (GeneralOptions.spaceEveryModule) {
                list.add(new StringTextComponent(""));
            }
        }

        return list;

    }

    public List<ITextComponent> getNewRightText() {

        List<ITextComponent> list = new ArrayList<>();

        for (BaseModule module : BaseModule.modulesRight) {
            if (!module.enabled) {
                continue;
            }
            if (module instanceof MiscRightModule) {
                ((MiscRightModule) module).update(getDebugInfoRight());
            } else {
                module.update(mc);
            }

            list.addAll(module.getLinesFormatted(mc.isReducedDebug()));
            if (GeneralOptions.spaceEveryModule) {
                list.add(new StringTextComponent(""));
            }
        }

        return list;

    }

    @Inject(method = "renderDebugInfoRight", at = @At("HEAD"), cancellable = true)
    public void renderRightText(MatrixStack matrixStack, CallbackInfo ci) {




        if (GeneralOptions.disableMod) {
            return;
        }
        List<ITextComponent> list = getNewRightText();

        for (int i = 0; i < list.size(); i++) {

            if (!Strings.isNullOrEmpty(list.get(i).getString())) {
                int height = 9;
                int width = this.fontRenderer.getStringWidth(list.get(i).getString());
                int windowWidth = this.mc.getMainWindow().getScaledWidth() - 2 - width;
                if (GeneralOptions.enableAnimations) {
                    windowWidth += xPos;
                }
                int y = 2 + height * i;

                fill(matrixStack, windowWidth - 1, y - 1, windowWidth + width + 1, y + height - 1,
                        GeneralOptions.backgroundColor);

                if (GeneralOptions.shadowText) {
                    this.fontRenderer.func_243246_a(matrixStack, list.get(i), windowWidth, (float)y, 0xE0E0E0);
                } else {
                    this.fontRenderer.func_243248_b(matrixStack, list.get(i), windowWidth, (float)y, 0xE0E0E0);
                }


            }
        }
        ci.cancel();

    }


    @Inject(method = "renderDebugInfoLeft", at = @At("HEAD"), cancellable = true)
    public void renderLeftText(MatrixStack matrixStack, CallbackInfo ci) {



        if (GeneralOptions.disableMod) {
            return;
        }
        List<ITextComponent> list = getNewLeftText();

        for (int i = 0; i < list.size(); i++) {

            if (!Strings.isNullOrEmpty(list.get(i).getString())) {

                int height = 9;
                int width = this.fontRenderer.getStringWidth(list.get(i).getString());
                int y = 2 + height * i;
                int xPosLeft = 2;

                if (GeneralOptions.enableAnimations) {
                    xPosLeft -= xPos;
                }

                fill(matrixStack, 1 + xPosLeft, y - 1, width + 3 + xPosLeft, y + height - 1,
                        GeneralOptions.backgroundColor);

                if (GeneralOptions.shadowText) {
                    this.fontRenderer.func_243246_a(matrixStack, list.get(i), xPosLeft, (float)y, 0xE0E0E0);
                } else {
                    this.fontRenderer.func_243248_b(matrixStack, list.get(i), xPosLeft, (float) y, 0xE0E0E0);
                }


            }
        }

        ci.cancel();
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void renderAnimation(MatrixStack matrices, CallbackInfo ci) {



        if (!GeneralOptions.enableAnimations) {
            return;
        }
        long time = Util.milliTime();
        if (time - lastAnimationUpdate >= 10 && (xPos != 0 || closingAnimation)) {


            int i = ((START_X_POS/2 + xPos) / 10)-9;

            if (xPos != 0 && !closingAnimation) {
                xPos /= GeneralOptions.animationSpeed;
                xPos -= i;
            }

            if (i == 0) {
                i = 1;
            }

            if (closingAnimation) {

                xPos += i;
                xPos *= GeneralOptions.animationSpeed;

                if (xPos >= 300) {
                    this.mc.gameSettings.showDebugInfo = false;
                    closingAnimation = false;
                }

            }

            lastAnimationUpdate = time;
        }
    }

}
