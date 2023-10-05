package io.github.jumperonjava.customcursorcomm.mixin;

import io.github.jumperonjava.customcursorcomm.CustomCursorInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(HoveredTooltipPositioner.class)
public class TooltipPositionMixin
{
    @ModifyArgs(method = "getPosition", at = @At(value = "INVOKE",target = "Lorg/joml/Vector2i;add(II)Lorg/joml/Vector2i;"))
    void tooltipPosChange(Args args){
        var scale = MinecraftClient.getInstance().getWindow().getScaleFactor();
        var cursor = CustomCursorInit.getConfig().pointer.clone();
        cursor.size/=scale;
        args.set(0,(int)args.get(0) - 6 + (int)(cursor.size * (1-cursor.x)));
        args.set(1,(int)args.get(1) - (int)(cursor.size * (cursor.y)));
    }
}
