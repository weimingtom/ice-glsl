package com.ice.graphics;

import android.util.Log;

import static android.opengl.GLES20.*;

/**
 * User: jason
 * Date: 13-3-14
 */
public class GlUtil {
    private static boolean debug = true;
    private static final String TAG = "GlUtil";

    public static int checkFramebufferStatus() {
        int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);

        if (debug) {
            printFboStatus(status);
        }

        return status;
    }

    private static void printFboStatus(int status) {

        switch (status) {
            case GL_FRAMEBUFFER_COMPLETE:
                Log.w(TAG, "GL_FRAMEBUFFER_COMPLETE");
                break;
            case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
                Log.w(TAG, "GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
                break;
            case GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
                Log.w(TAG, "GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
                break;
            case GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS:
                Log.w(TAG, "GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS");
                break;
            case GL_FRAMEBUFFER_UNSUPPORTED:
                Log.w(TAG, "GL_FRAMEBUFFER_UNSUPPORTED");
                break;
        }

    }

}
