package io.github.jumperonjava.customcursorcomm.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class CobblemonFix {
    @Shadow @Final private MinecraftClient client;
    boolean locked=false;
    @Inject(method = "render",at = @At("HEAD"))
    void lock(DrawContext context, float tickDelta, CallbackInfo ci){

        if(client.currentScreen == null)
        {
            if(locked)
                return;
            client.mouse.lockCursor();
            locked = true;
            var x = (double)(this.client.getWindow().getWidth() / 2);
            var y = (double)(this.client.getWindow().getHeight() / 2);
            InputUtil.setCursorParameters(this.client.getWindow().getHandle(), 212995, x, y);
        }
        else{
            locked=false;
        }
    }
}
