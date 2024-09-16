package io.github.jumperonjava.customcursorcomm;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import static io.github.jumperonjava.customcursorcomm.CustomCursorInit.client;

public class CursorRenderer {
    public static void render(DrawContext context, int mouseX, int mouseY, float delta){
        var config = CustomCursorInit.getConfig().pointer;
        if(config.enabled){
            client.getProfiler().push("cursor");
            var scale = client.getWindow().getScaleFactor();
            RenderSystem.depthFunc(GL11.GL_ALWAYS);
            context.drawTexture(config.identifier, (int) Math.round(mouseX-config.size*config.x/scale), (int) Math.round(mouseY-config.size*config.y/scale), (float) 0, (float) 0, (int) (config.size/scale), (int) (config.size/scale), (int) (config.size/scale), (int) (config.size/scale));
            RenderSystem.depthFunc(GL11.GL_LEQUAL);

            //for debugging
            //context.drawTexture(new Identifier("customcursor","textures/gui/pointer.png"), (int) (mouseX-4), (int) (mouseY-4),0,0,8,8,8,8);

            GLFW.glfwSetInputMode(client.getWindow().getHandle(),GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
            client.getProfiler().pop();
        }
        else {
            GLFW.glfwSetInputMode(client.getWindow().getHandle(),GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }
    }
}
