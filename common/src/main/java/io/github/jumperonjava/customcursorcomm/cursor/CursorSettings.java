package io.github.jumperonjava.customcursorcomm.cursor;


import com.google.gson.Gson;
import net.minecraft.util.Identifier;

public class CursorSettings implements Cloneable{
    public float x=1f;
    public float y;
    public int size=32;
    public boolean enabled=false;
    public Identifier identifier = PLACEHOLDER_CURSOR;
    public final static Identifier PLACEHOLDER_CURSOR = new Identifier("textures/item/diamond_sword.png");

    public CursorSettings clone() {
        return new Gson().fromJson(new Gson().toJson(this), CursorSettings.class);
    }
}
