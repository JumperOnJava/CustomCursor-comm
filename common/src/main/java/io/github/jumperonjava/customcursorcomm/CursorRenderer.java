package io.github.jumperonjava.customcursorcomm;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import static io.github.jumperonjava.customcursorcomm.CustomCursorInit.client;

public class CursorRenderer {
    public static void render(DrawContext context, int mouseX, int mouseY, float delta){
        var config = CustomCursorInit.getConfig().pointer;
        if(config.enabled){
            var scale = client.getWindow().getScaleFactor();
            context.drawTexture(config.identifier, (int) Math.round(mouseX-config.size*config.x/scale), (int) Math.round(mouseY-config.size*config.y/scale), (float) 0, (float) 0, (int) (config.size/scale), (int) (config.size/scale), (int) (config.size/scale), (int) (config.size/scale));

            //for debugging
            //context.drawTexture(new Identifier("customcursor","textures/gui/pointer.png"), (int) (mouseX-4), (int) (mouseY-4),0,0,8,8,8,8);

            GLFW.glfwSetInputMode(client.getWindow().getHandle(),GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
        }
        else {
            GLFW.glfwSetInputMode(client.getWindow().getHandle(),GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }
    }
}
