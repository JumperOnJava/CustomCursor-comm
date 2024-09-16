package io.github.jumperonjava.customcursorcomm.mixin;

import io.github.jumperonjava.customcursorcomm.CursorConfigStorage;
import io.github.jumperonjava.customcursorcomm.CustomCursorInit;
import io.github.jumperonjava.customcursorcomm.cursor.CursorEditScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ControlsOptionsScreen.class)
public abstract class MouseOptionsScreenMixin extends GameOptionsScreen {

    public MouseOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Inject(method = "addOptions",at = @At(value = "TAIL"))
    void addOption(CallbackInfo ci){
        this.body.addWidgetEntry(new ButtonWidget.Builder(Text.translatable("customcursor.openbutton"),(buttonWidget)->{
            MinecraftClient.getInstance().setScreen(new CursorEditScreen(this,CustomCursorInit.getConfig().pointer, c -> {
                var cfg = new CursorConfigStorage();
                cfg.pointer = c;
                CustomCursorInit.setConfig(cfg);
            }));
        }).build(),null);
    }
}
