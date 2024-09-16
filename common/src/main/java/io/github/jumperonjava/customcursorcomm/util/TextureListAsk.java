package io.github.jumperonjava.customcursorcomm.util;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public abstract class TextureListAsk extends AskScreen<Identifier> {
    ScrollListWidget list;
    //private static Map<Identifier, AbstractTexture> textures;
    /*static{
        var paths = new LinkedList<String>();
        for(var path : paths)
        {
            var manager = MinecraftClient.getInstance().getResourceManager();
            var resources = manager.findResources(path,i-> i.toString().endsWith(".png"));
            textures.addAll(resources.keySet().stream().toList());
        }
    }*/

    protected Identifier EMPTY_TEXTURE = Identifier.of("minecraft","empty");
    private Identifier selectedTexture = EMPTY_TEXTURE;
    public static final int gap = 2;
    private List<Identifier> textures = new ArrayList<>();

    protected TextureListAsk(Consumer<Identifier> onSuccess, Runnable onFail){
        super(onSuccess, onFail);
    }
    public void setTexturesCallback(Consumer<List<Identifier>> textureGetter){};
    private TextureWidget selectedTextureWidget;
    @Override
    protected void init() {
        list = new ScrollListWidget(client,width,height-22*2,0,22,40);
        refreshListByFilter("");
        addDrawableChild(list);

        var search = new TextFieldWidget(client.textRenderer,0,0,width,20,Text.empty());
        search.setChangedListener(this::refreshListByFilter);
        addDrawableChild(search);

        var accept = new ButtonWidget.Builder(Text.translatable("customcursor.folder.accept"),b->{
            if(selectedTexture!=EMPTY_TEXTURE)
                success(selectedTexture);
            else
                fail();
        })
                .dimensions((int) (40+gap),height-20-gap,100,20).build();
        var cancel = new ButtonWidget.Builder(Text.translatable("customcursor.folder.cancel"),b->fail())
                .dimensions((int) (140+gap*1.5),height-20-gap,100,20).build();

        addDrawableChild(accept);
        addDrawableChild(cancel);
        selectedTextureWidget = new TextureWidget(Identifier.of("minecraft","textures/item/barrier.png"),gap/2,height-40-gap/2,40,40);
        addDrawableChild(selectedTextureWidget);
        setTexturesCallback(this::setTextures);
    }

    public void setTextures(List<Identifier> identifiers) {
        this.textures = identifiers;
        refreshListByFilter(lastFilter);
    }
    private String lastFilter = "";
    private void refreshListByFilter(String s) {
        lastFilter = s;
        list.children().clear();
        list.setScrollAmount(0);
        for(var key : textures){
            var id = key.toString();
            if(!id.toLowerCase().contains(s.toLowerCase()))
                continue;
            var button = new ButtonWidget.Builder(Text.literal(id),b->{
                this.selectedTexture = Identifier.of(b.getMessage().getString());
                selectedTextureWidget.setTexture(selectedTexture);
            })
                    .position(40,10)
                    .size(width-40-6-gap,20)
                    .build();
            var entry = new ScrollListWidget.ScrollListEntry();
            entry.addDrawableChild(button,true);
            entry.addDrawableChild(new TextureWidget(key,0,0,40,40),false);
            list.addEntry(entry);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context,mouseX,mouseY,delta);
        super.render(context, mouseX, mouseY, delta);
        context.drawText(
                textRenderer,
                Text.translatable("customcursor.folder.selected").append(": ").append(selectedTexture.toString()),
                45,
                height-30-6-gap/2,
                0xFFFFFFFF,
                true);
    }
}
