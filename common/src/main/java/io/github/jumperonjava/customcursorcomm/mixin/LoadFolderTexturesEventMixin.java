package io.github.jumperonjava.customcursorcomm.mixin;

import io.github.jumperonjava.customcursorcomm.CustomCursorInit;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class LoadFolderTexturesEventMixin {
    @Inject(method = "init",at=@At("HEAD"))
    void init(CallbackInfo ci){
        CustomCursorInit.TEXTURE_FOLDER.redefineTextures();
    }

}
