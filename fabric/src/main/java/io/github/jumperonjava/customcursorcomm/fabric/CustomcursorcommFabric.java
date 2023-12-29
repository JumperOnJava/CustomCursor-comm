package io.github.jumperonjava.customcursorcomm.fabric;

import io.github.jumperonjava.customcursorcomm.CursorRenderer;
import io.github.jumperonjava.customcursorcomm.CustomCursorInit;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;

public class CustomcursorcommFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CustomCursorInit.entrypoint((s)->FabricLoader.getInstance().isModLoaded(s));
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> screen.drawables.add(CursorRenderer::render));
    }
}