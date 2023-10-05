package io.github.jumperonjava.customcursorcomm.forge;

import dev.architectury.platform.forge.EventBuses;
import io.github.jumperonjava.customcursorcomm.CustomCursorInit;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CustomCursorInit.MOD_ID)
public class CustomcursorcommForge {
    public CustomcursorcommForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(CustomCursorInit.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
            CustomCursorInit.init();
    }
}