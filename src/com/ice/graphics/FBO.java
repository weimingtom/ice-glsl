package com.ice.graphics;

import android.opengl.GLES20;
import com.ice.graphics.state_controller.SafeGlStateController;

import static android.opengl.GLES20.GL_FRAMEBUFFER;
import static android.opengl.GLES20.glBindFramebuffer;
import static android.opengl.GLES20.glDeleteFramebuffers;

/**
 * User: jason
 * Date: 13-3-14
 */
public class FBO extends SafeGlStateController implements GlRes {

    private int glFBO;
    private boolean prepared;

    @Override
    public void prepare() {
        if (!prepared) {
            int[] temp = new int[1];
            GLES20.glGenFramebuffers(temp.length, temp, 0);
            glFBO = temp[0];
            prepared = true;
        }
    }

    @Override
    public int glRes() {
        return glFBO;
    }

    @Override
    public void release() {
        if (prepared) {
            int[] temp = new int[]{glFBO};
            glDeleteFramebuffers(temp.length, temp, 0);
            prepared = false;
        }
    }

    @Override
    public void onEGLContextLost() {
        prepared = false;
        glFBO = 0;
    }

    @Override
    protected void onAttach() {
        if (!prepared)
            prepare();

        glBindFramebuffer(GL_FRAMEBUFFER, glFBO);
    }

    @Override
    protected void onDetach() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

}
