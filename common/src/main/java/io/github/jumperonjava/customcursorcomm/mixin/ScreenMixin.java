package io.github.jumperonjava.customcursorcomm.mixin;

import io.github.jumperonjava.customcursorcomm.CursorRenderer;
import io.github.jumperonjava.customcursorcomm.CustomCursorInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.InputUtil;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {
    @Shadow @Nullable protected MinecraftClient client;

    @Inject(method = "renderWithTooltip",at = @At("TAIL"))//at = @At(value = "INVOKE",shift = At.Shift.AFTER ,target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V"))
    void renderCursor(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        CursorRenderer.render(context,mouseX,mouseY,delta);
    }
}
