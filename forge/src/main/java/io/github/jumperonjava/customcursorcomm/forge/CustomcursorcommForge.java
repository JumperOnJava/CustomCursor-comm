package io.github.jumperonjava.customcursorcomm.forge;

import io.github.jumperonjava.customcursorcomm.CursorRenderer;
import io.github.jumperonjava.customcursorcomm.CustomCursorInit;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod(CustomCursorInit.MOD_ID)
public class CustomcursorcommForge {
    public CustomcursorcommForge() {
		// Submit our event bus to let architectury register our content on the right time
        //EventBuses.registerModEventBus(CustomCursorInit.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
            CustomCursorInit.entrypoint((s)->ModList.get().isLoaded(s));

    }
    @SubscribeEvent
    public void onScreenInit(ScreenEvent.Init event){
         event.getScreen().drawables.add(CursorRenderer::render);
    }
}