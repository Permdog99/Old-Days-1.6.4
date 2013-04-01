package net.minecraft.src;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;

public abstract class TextureFX extends TextureStitched{
    protected byte[] imageData;
    protected boolean anaglyphEnabled;
    private Texture tmp;
    private int width = 16;
    private int height = 16;

    public TextureFX(String str){
        super(str);
        anaglyphEnabled = false;
        imageData = new byte[1024];
    }

    public abstract void onTick();

    @Override
    public void updateAnimation(){
        if (textureSheet == null){
            return;
        }
        width = (Integer)(mod_OldDays.getField(TextureStitched.class, this, 7));
        height = width;
        if (tmp == null){
            tmp = new Texture(getIconName(), 2, 16, 16, 10496, 6408, 9728, 9728, 0, null);
        }
        anaglyphEnabled = Minecraft.getMinecraft().gameSettings.anaglyph;
        onTick();
        tmp.getTextureData().put(imageData);
        for (int i = 0; i <  width / 16; i++){
            for (int j = 0; j < height / 16; j++){
                tmp.getTextureData().clear();
                tmp.getTextureData().position(0).limit(imageData.length);
                textureSheet.copyFrom(originX + 16 * i, originY + 16 * j, tmp, rotated);
            }
        }
    }
}