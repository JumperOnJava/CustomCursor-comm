package io.github.jumperonjava.customcursorcomm.mixin;

import io.github.jumperonjava.customcursorcomm.CursorConfigStorage;
import io.github.jumperonjava.customcursorcomm.cursor.CursorEditScreen;
import io.github.jumperonjava.customcursorcomm.CustomCursorInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ControlsOptionsScreen.class)
public class MouseOptionsScreenMixin extends Screen {

    protected MouseOptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init",at = @At("HEAD"),locals = LocalCapture.CAPTURE_FAILHARD)
    void inject(CallbackInfo ci){
        int k = this.height / 6 - 12 + 24 * 3;
        var cursorEditScreenButton = new ButtonWidget.Builder(Text.translatable("customcursor.openbutton"),(buttonWidget)->{
            MinecraftClient.getInstance().setScreen(new CursorEditScreen(this,CustomCursorInit.getConfig().pointer, c -> {
                var cfg = new CursorConfigStorage();
                cfg.pointer = c;
                CustomCursorInit.setConfig(cfg);
            }));
        }).dimensions(width / 2 - 155, k, 310, 20).build();
        addDrawableChild(cursorEditScreenButton);

    }
    @ModifyArg(method = "init",
            index = 1,
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/option/GameOptions;getOperatorItemsTab()Lnet/minecraft/client/option/SimpleOption;"
                    ),
                    to = @At("TAIL")
            ),
            at = @At(
                    value = "INVOKE",
                    slice = "fd",
                    target = "Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;dimensions(IIII)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;"))
    int injected(int k){
        return k+24;
    }
}
