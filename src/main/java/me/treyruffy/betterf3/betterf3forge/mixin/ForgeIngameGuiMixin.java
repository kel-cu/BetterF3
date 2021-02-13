package me.treyruffy.betterf3.betterf3forge.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.gui.overlay.DebugOverlayGui;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ForgeIngameGui.class)
public abstract class ForgeIngameGuiMixin {

	@Inject(remap = false, method = "renderHUDText", at = @At(value = "INVOKE", opcode = Opcodes.PUTFIELD, target =
			"net/minecraftforge/client/gui/ForgeIngameGui$GuiOverlayDebugForge.update()V"), cancellable = true)
	public void customDebugMenu(int width, int height, MatrixStack mStack, CallbackInfo ci) {
		new DebugOverlayGui(Minecraft.getInstance()).render(mStack);
		ci.cancel();
	}

}
