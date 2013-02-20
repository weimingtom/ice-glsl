package com.ice.graphics.texture;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: jason
 * Date: 12-3-30
 * Time: 下午3:46
 */
public class BitmapTexture extends Texture {
    private static final String TAG = "BitmapTexture";

    public BitmapTexture(Bitmap bitmap) {
        this(bitmap, Params.LINEAR_CLAMP_TO_EDGE);
    }

    public BitmapTexture(Bitmap bitmap, Params params) {
        super(params);

        this.bitmap = bitmap;
    }

    @Override
    protected void onAttach() {
        super.onAttach();

        if (reload) {
            reload = false;
            onLoadTextureData();
        }

        if (subProvider != null) {
            synchronized (this) {
                GLUtils.texSubImage2D(GL10.GL_TEXTURE_2D, 0, xOffset, yOffset, subProvider);
            }
            subProvider = null;
        }
    }

    @Override
    protected void onLoadTextureData() {
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void postSubData(int xoffset, int yoffset, Bitmap subPixel) {
        //TODO Warning !   subProvider 的这种处理可能导致按钮的状态不正常
        if (this.subProvider != null) {

            Log.w(TAG, "postSubData ignored ! ");
            return;
        }

        this.subProvider = subPixel;
        this.xOffset = xoffset;
        this.yOffset = yoffset;
    }

    private int xOffset, yOffset;
    private boolean reload;

    private Bitmap bitmap;
    private volatile Bitmap subProvider;
}
