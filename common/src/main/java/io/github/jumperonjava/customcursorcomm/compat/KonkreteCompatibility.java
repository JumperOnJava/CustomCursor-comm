package io.github.jumperonjava.customcursorcomm.compat;

import de.keksuccino.konkrete.events.SubscribeEvent;
import de.keksuccino.konkrete.events.client.GuiScreenEvent;
import io.github.jumperonjava.customcursorcomm.CursorRenderer;

public class KonkreteCompatibility {
    @SubscribeEvent()
    public static void onEndRender(GuiScreenEvent.DrawScreenEvent.Post event){
        CursorRenderer.render(event.getGuiGraphics(), event.getMouseX(), event.getMouseY(),0f);
    }
}
