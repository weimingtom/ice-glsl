package com.ice.graphics.texture;

import static android.opengl.GLES20.*;

/**
 * User: jason
 * Date: 13-3-14
 */
public class FboTexture extends Texture {

    private int width, height;
    private int attachment = GL_COLOR_ATTACHMENT0;

    public FboTexture(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public FboTexture(Params params, int width, int height) {
        super(params);
        this.width = width;
        this.height = height;
    }

    @Override
    protected void onLoadTextureData() {

        glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_DEPTH_COMPONENT,
                width, height, 0,
                GL_DEPTH_COMPONENT,
                GL_UNSIGNED_INT,
                null
        );

    }

}
