package io.github.jumperonjava.customcursorcomm.cursor;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.jumperonjava.customcursorcomm.CustomCursorInit;
import io.github.jumperonjava.customcursorcomm.util.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.MouseOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.datafixer.fix.OminousBannerBlockEntityRenameFix;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;

import java.awt.*;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

public class CursorEditScreen extends Screen {
    private final Consumer<CursorSettings> onSuccess;
    private final CursorSettings targetConfig;
    private final Screen parent;

    public CursorEditScreen(Screen parent,CursorSettings cursorConfig, Consumer<CursorSettings> onSuccess) {
        super(Text.empty());
        this.parent = parent;
        this.onSuccess = onSuccess;
        this.targetConfig = cursorConfig.clone();
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }

    @Override
    protected void init() {
        super.init();
        int heightOffset = (int) (24*2.75);
        var field = new TextFieldWidget(client.textRenderer,width/2-128,height/2+heightOffset-64-24-22,256,20,Text.empty());
        field.setMaxLength(512);
        field.setText(this.targetConfig.identifier.toString());
        field.setChangedListener((s)->{
            try{
                setIdentifier(new Identifier(s));
            }
            catch (Exception e){e.printStackTrace();}
        });
        addDrawableChild(new ButtonWidget.Builder(Text.translatable("customcursor.edit.confirm"),this::confirm)
                .dimensions(width/2-128,height/2+heightOffset-64-24-22*0,126,20).build());
        addDrawableChild(new ButtonWidget.Builder(Text.translatable("customcursor.edit.cancel"),(f)->close())
                .dimensions(width/2+2,height/2+heightOffset-64-24-22*0,126,20).build());
        addDrawableChild(new ButtonWidget.Builder(Text.translatable("customcursor.edit.folder"),(b)->{
            AskScreen.ask(
                    new FolderTextureAskList(
                            CustomCursorInit.TEXTURE_FOLDER,
                            this::setIdentifier,
                            ()->{}
                    )
            );
        })
                .dimensions(width/2-128,height/2+heightOffset-64-24-22*2,256,20).build());
        var xSlider = new SliderWidget(width/2-128,height/2+heightOffset-64-24-22*3,126,20,Text.translatable("customcursor.edit.x"),0.,1.,this.targetConfig.x,1/64f);
        var ySlider = new SliderWidget(width/2+2,height/2+heightOffset-64-24-22*3,126,20,Text.translatable("customcursor.edit.y"),0.,1.,this.targetConfig.y,1/64f);
        Function<Boolean,Text> textfunc = (Boolean b) -> {
            return Text.translatable("customcursor.edit.enabled."+b);
        };
        var enabledButtonWidget = new ButtonWidget.Builder(textfunc.apply(this.targetConfig.enabled),(buttonWidget)->{
            this.targetConfig.enabled=!targetConfig.enabled;
            buttonWidget.setMessage(textfunc.apply(this.targetConfig.enabled));
        })
                .dimensions(width/2-128,height/2+heightOffset-64-24-22*5,256,20).build();
        var maxsize = 256.;
        var size = new SliderWidget(width/2-128,height/2+heightOffset-64-24-22*4,256,20,Text.translatable("customcursor.edit.size"),0,maxsize,this.targetConfig.y,1);
        size.setValueOwn(this.targetConfig.size/maxsize);
        xSlider.setChangedListener(d->this.targetConfig.x=(float)(double)d);
        ySlider.setChangedListener(d->this.targetConfig.y=(float)(double)d);
        size.setChangedListener(d->this.targetConfig.size=(int)(double)d);

        addDrawableChild(xSlider);
        addDrawableChild(ySlider);
        addDrawableChild(enabledButtonWidget);
        addDrawableChild(size);
        addDrawableChild(field);
    }

    private void setIdentifier(Identifier identifier) {
        this.targetConfig.identifier = identifier;
    }

    private void confirm(ButtonWidget buttonWidget) {
        try{
            onSuccess.accept(targetConfig);
        }
        catch (Exception e){e.printStackTrace();}
        this.close();
    }
    private float bgx=0,bgy=0,color,rot;
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int heightOffset = (int) (24*2.75   );
        super.render(context, mouseX, mouseY, delta);
        //renderBackground(context,mouseX,mouseY,delta);
        var vec = new Vec2f(mouseX-width/2,mouseY-height/2).normalize().multiply(delta);
        bgx += vec.x;
        bgy += vec.y;
        color+=delta*0.05;
        var n = 2;
        renderCheckerboard(
                context,
                delta,
                ColorHelper.Argb.getArgb(255,
                        (int) (128+64*Math.pow(Math.sin(color+0*Math.PI/3),n)),
                        (int) (128+64*Math.pow(Math.sin(color+2*Math.PI/3),n)),
                        (int) (128+64*Math.pow(Math.sin(color+4*Math.PI/3),n))
                ),
                ColorHelper.Argb.getArgb(255,
                        (int) ((192+63*Math.pow(Math.cos(color+0*Math.PI/3),n))),
                        (int) ((192+63*Math.pow(Math.cos(color+2*Math.PI/3),n))),
                        (int) ((192+63*Math.pow(Math.cos(color+4*Math.PI/3),n)))
                )
        );

        context.drawTexture(this.targetConfig.identifier,width/2-64,height/2+heightOffset-64,0,0,128,128, 128,128);

        context.drawTexture(new Identifier("customcursor","textures/gui/pointer.png"), (int) (width/2-64+this.targetConfig.x*128)-4, (int) (height/2-64+this.targetConfig.y*128)+heightOffset-4,0,0,8,8, 8,8);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
    }

    private void renderCheckerboard(DrawContext context, float delta, int color1, int color2) {
        int heightOffset = (int) (24*2.75);
        context.fill(width/2-64,height/2+heightOffset-64,width/2+64,height/2+heightOffset+64,color2);
        context.enableScissor(width/2-64,height/2+heightOffset-64,width/2+64,height/2+heightOffset+64);
        rot+=new Random().nextFloat(-0.5f,0.5f)*delta/4;
        //bgx+=Math.sin(rot)/9;
        //bgy+=Math.cos(rot)/9;
        //bgx+=delta/9;
        //bgy+=delta/9;
        context.getMatrices().push();
        context.getMatrices().translate((width/2-64+MathHelper.floorMod(bgx,16)-16),(height/2+heightOffset-64+MathHelper.floorMod(bgy,16)-16),0);
        float r = ColorHelper.Argb.getRed(color1)/255f;
        float g = ColorHelper.Argb.getGreen(color1)/255f;
        float b = ColorHelper.Argb.getBlue(color1)/255f;
        RenderSystem.setShaderColor(r,g,b,255);
        context.drawTexture(new Identifier("customcursor","textures/gui/backgroundcheckerboard.png"),
                0,
                0,
                0,
                0,144,144,144,144);
        RenderSystem.setShaderColor(1,1,1,1);
        context.getMatrices().pop();
        context.disableScissor();
    }
}
