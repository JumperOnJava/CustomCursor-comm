package io.github.jumperonjava.customcursorcomm.cursor;

import io.github.jumperonjava.customcursorcomm.util.SliderWidget;
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

import java.util.function.Consumer;
import java.util.function.Function;

public class CursorEditScreen extends Screen {
    private final Consumer<CursorSettings> onSuccess;
    private final CursorSettings targetConfig;
    private static int heightOffset = (int) (24*2.5);
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

        var field = new TextFieldWidget(client.textRenderer,width/2-128,height/2+heightOffset-64-24,256,20,Text.empty());
        field.setMaxLength(512);
        field.setText(this.targetConfig.identifier.toString());
        field.setChangedListener((s)->{
            try{
                this.targetConfig.identifier = new Identifier(s);
            }
            catch (Exception e){e.printStackTrace();}
        });
        addDrawableChild(new ButtonWidget.Builder(Text.translatable("customcursor.edit.confirm"),this::confirm)
                .dimensions(width/2-128,height/2+heightOffset-64-24-22,126,20).build());
        addDrawableChild(new ButtonWidget.Builder(Text.translatable("customcursor.edit.cancel"),(f)->close())
                .dimensions(width/2+2,height/2+heightOffset-64-24-22,126,20).build());
        var xSlider = new SliderWidget(width/2-128,height/2+heightOffset-64-24-22*2,126,20,Text.translatable("customcursor.edit.x"),0.,1.,this.targetConfig.x,1/64f);
        var ySlider = new SliderWidget(width/2+2,height/2+heightOffset-64-24-22*2,126,20,Text.translatable("customcursor.edit.y"),0.,1.,this.targetConfig.y,1/64f);
        Function<Boolean,Text> textfunc = (Boolean b) -> {
            return Text.translatable("customcursor.edit.enabled."+b);
        };
        var enabledButtonWidget = new ButtonWidget.Builder(textfunc.apply(this.targetConfig.enabled),(buttonWidget)->{
            this.targetConfig.enabled=!targetConfig.enabled;
            buttonWidget.setMessage(textfunc.apply(this.targetConfig.enabled));
        })
                .dimensions(width/2-128,height/2+heightOffset-64-24-22*4,256,20).build();
        var maxsize = 256.;
        var size = new SliderWidget(width/2-128,height/2+heightOffset-64-24-22*3,256,20,Text.translatable("customcursor.edit.size"),0,maxsize,this.targetConfig.y,1);
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

    private void confirm(ButtonWidget buttonWidget) {
        try{
            onSuccess.accept(targetConfig);
        }
        catch (Exception e){e.printStackTrace();}
        super.close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        //context.fill(width/2-64,height/2-64,width/2+64,height/2+64,0xFF00FF00);
        context.fill(width/2-64,height/2+heightOffset-64,width/2+64,height/2+heightOffset+64,0xFF000000);
        context.drawTexture(this.targetConfig.identifier,width/2-64,height/2+heightOffset-64,0,0,128,128, 128,128);
        context.drawTexture(new Identifier("customcursor","textures/gui/pointer.png"), (int) (width/2-64+this.targetConfig.x*128)-4, (int) (height/2-64+this.targetConfig.y*128)+heightOffset-4,0,0,8,8, 8,8);
    }
}
