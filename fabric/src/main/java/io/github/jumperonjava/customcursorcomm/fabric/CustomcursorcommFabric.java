package io.github.jumperonjava.customcursorcomm.fabric;

import io.github.jumperonjava.customcursorcomm.CustomCursorInit;
import net.fabricmc.api.ModInitializer;

public class CustomcursorcommFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CustomCursorInit.init();
    }
}