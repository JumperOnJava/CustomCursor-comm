package io.github.jumperonjava.customcursorcomm.util;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FolderTextureAskList extends TextureListAsk{
    private final TextureFolder folder;
    private List<Identifier> textures = new ArrayList<>();

    public FolderTextureAskList(TextureFolder folder, Consumer<Identifier> onSuccess, Runnable onFail) {
        super(onSuccess, onFail);
        this.folder = folder;
    }

    @Override
    protected void init() {
        super.init();
        var cancel = new ButtonWidget.Builder(Text.translatable("customcursor.folder.openfolder"), b->openCursorFolder())
                .dimensions((int) (240+gap*2),height-20-gap,100,20).build();
        addDrawableChild(cancel);
    }

    private void openCursorFolder() {
        Util.getOperatingSystem().open(folder.path.toFile());
    }

    public void setTexturesCallback(Consumer<List<Identifier>> consumer) {
        if (!this.textures.isEmpty())
            consumer.accept(textures);
        folder.redefineTextures(()->{
            this.textures = folder.getTextures();
            consumer.accept(this.textures);
        });

    }
}
