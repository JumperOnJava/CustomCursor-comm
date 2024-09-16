package io.github.jumperonjava.customcursorcomm.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Pattern;

public class TextureFolder {
    public final Path path;
    public final String namespace;
    private List<Identifier> textures = new ArrayList<>();

    /**
     * Initializes texture folder
     * DOES NOT SCAN THIS FOLDER AUTOMATICALLY but creates it to prevent some crashes
     * Use redefineTextures() to scan and update textures from folder
     */
    public TextureFolder(Path path,String namespace){
        this.path = path;
        this.namespace = namespace;
        if(Files.isRegularFile(path)){
            throw new RuntimeException("Path %s is not a directory".formatted(path));
        }
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * redefines textures in given folder
     */
    public void redefineTextures(){
        redefineTextures(()->{});
    }
    public void redefineTextures(Runnable onFinishedCallback){
        List<Path> l = new ArrayList<>();
        reregisterTextures(onFinishedCallback,getAllTexturesRecursive(path));
    }
    private List<Path> getAllTexturesRecursive(Path path){
        try{
            var l = new ArrayList<Path>();
            var objects = Files.list(path);
            for(var object : objects.toList()){
                if(Files.isDirectory(object)){
                    l.addAll(getAllTexturesRecursive(object));
                }
                if(Files.isRegularFile(object) && object.toString().toLowerCase().endsWith(".png")){
                    l.add(object);
                }
            }
            return l;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void reregisterTextures(Runnable onFinishedCallback, List<Path> toRegister) {
        // Move the RenderSystem.recordRenderCall out of the forEach loop
        RenderSystem.recordRenderCall(() -> {
            var tman = MinecraftClient.getInstance().getTextureManager();
            textures.forEach(tman::destroyTexture);
            textures.clear();
            Map<Identifier, NativeImage> textureMap = new HashMap<>();

            toRegister.forEach((p) -> {
                try {
                    var stream = Files.newInputStream(p, StandardOpenOption.READ);
                    var nativeImage = NativeImage.read(stream);
                    stream.close();

                    textureMap.put(getIdentifierFor(p), nativeImage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            textureMap.forEach((identifier, nativeImage) -> {
                var texture = new NativeImageBackedTexture(nativeImage);
                tman.registerTexture(identifier, texture);
                textures.add(identifier);
            });

            onFinishedCallback.run();
        });
    }

    private Identifier getIdentifierFor(Path p) {
        var s = p.toAbsolutePath().toString().replaceAll(Pattern.quote(path.toAbsolutePath().toString()),"").replace("\\","/").toLowerCase().substring(1);
        try{
            return Identifier.of(namespace,s);
        }
        catch (InvalidIdentifierException e){
            return Identifier.of(namespace,"wrongfilename" + s.hashCode());
        }
    }

    /**
     * return COPY of textures list, empty if no scan was done
     */
    public List<Identifier> getTextures(){
        return new ArrayList(this.textures);
    }
}
