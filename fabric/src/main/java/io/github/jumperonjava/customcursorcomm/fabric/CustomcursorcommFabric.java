package io.github.jumperonjava.customcursorcomm.fabric;

import io.github.jumperonjava.customcursorcomm.CustomCursorInit;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class CustomcursorcommFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CustomCursorInit.entrypoint((s)->FabricLoader.getInstance().isModLoaded(s));
    }
}