package com.ice.graphics.texture;

import com.ice.graphics.GlRes;
import com.ice.graphics.state_controller.SafeGlStateController;

import java.util.HashMap;
import java.util.Map;

import static android.opengl.GLES20.*;

public abstract class Texture extends SafeGlStateController implements GlRes {

    private static int maxSize;
    private static boolean npotSupported;

    public static void setMaxSize(int maxSize) {
        Texture.maxSize = maxSize;
    }

    public static void setNpotSupported(boolean npotSupported) {
        Texture.npotSupported = npotSupported;
    }

    public static int getMaxSize() {
        return maxSize;
    }

    public static boolean isNpotSupported() {
        return npotSupported;
    }

    private boolean prepared;

    public Texture() {
        this(Params.LINEAR_CLAMP_TO_EDGE);
    }

    public Texture(Params params) {
        this.params = params;
    }

    @Override
    public int glRes() {
        return glTexture;
    }

    @Override
    protected void onAttach() {
        if (!prepared)
            prepare();

        glBindTexture(GL_TEXTURE_2D, glTexture);
    }

    @Override
    protected void onDetach() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    public void onEGLContextLost() {
        prepared = false;
    }

    @Override
    public void prepare() {
        if (prepared) return;

        int[] temp = new int[1];
        glGenTextures(1, temp, 0);

        glTexture = temp[0];

        glBindTexture(GL_TEXTURE_2D, glTexture);

        /*
         * Always set any texture parameters before loading texture data,
         *By setting the parameters first,
         *OpenGL ES can optimize the texture data it provides to the graphics hardware to match your settings.
         */
        bindTextureParams(params);

        onLoadTextureData();

        prepared = true;
    }

    @Override
    public void release() {
        if (prepared) {
            glDeleteTextures(1, new int[]{glTexture}, 0);
            prepared = false;
            glTexture = -1;
        }
    }

    protected abstract void onLoadTextureData();

    private void bindTextureParams(Params params) {
        for (Map.Entry<Integer, Integer> entry : params.getParamMap().entrySet()) {
            glTexParameterf(
                    GL_TEXTURE_2D,
                    entry.getKey(),
                    entry.getValue()
            );
        }
    }

    protected int glTexture;
    private Params params;

    public static class Params {
        public static final Params LINEAR_REPEAT;

        public static final Params LINEAR_CLAMP_TO_EDGE;

        static {
            LINEAR_REPEAT = new Params();
            LINEAR_REPEAT.add(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            LINEAR_REPEAT.add(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            LINEAR_REPEAT.add(GL_TEXTURE_WRAP_S, GL_REPEAT);
            LINEAR_REPEAT.add(GL_TEXTURE_WRAP_T, GL_REPEAT);

            LINEAR_CLAMP_TO_EDGE = new Params();
            LINEAR_CLAMP_TO_EDGE.add(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            LINEAR_CLAMP_TO_EDGE.add(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            LINEAR_CLAMP_TO_EDGE.add(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            LINEAR_CLAMP_TO_EDGE.add(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        }

        public Params() {
            paramMap = new HashMap<Integer, Integer>();
        }

        public void add(int pName, int value) {
            paramMap.put(pName, value);
        }

        public Map<Integer, Integer> getParamMap() {
            return paramMap;
        }

        private Map<Integer, Integer> paramMap;
    }

}
