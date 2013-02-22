package com.ice.graphics;

import android.util.Log;
import com.ice.graphics.GlRes;
import com.ice.graphics.state_controller.SafeGlStateController;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import static android.opengl.GLES20.*;
import static com.ice.model.Constants.BYTES_PER_SHORT;
import static javax.microedition.khronos.opengles.GL11.GL_ELEMENT_ARRAY_BUFFER;
import static javax.microedition.khronos.opengles.GL11.GL_STATIC_DRAW;

/**
 * User: jason
 * Date: 13-2-16
 */
public class IBO extends SafeGlStateController implements GlRes {

    private static final String TAG = "IBO";

    private int glIBO;
    private int usage;
    private boolean prepared;
    private Buffer indicesData;

    private final int size;
    private final int type;

    public IBO(Buffer data) {
        this(data, GL_STATIC_DRAW);
    }

    public IBO(Buffer data, int usage) {
        if (data instanceof ByteBuffer) {
            type = GL_UNSIGNED_BYTE;
            size = data.limit();
            Log.i(TAG, "GL_UNSIGNED_BYTE");
        }
        else if (data instanceof ShortBuffer) {
            type = GL_UNSIGNED_SHORT;
            size = data.limit() * BYTES_PER_SHORT;
            Log.i(TAG, "GL_UNSIGNED_SHORT");
        }
        else {
            throw new IllegalArgumentException();
        }

        indicesData = data;
        this.usage = usage;
    }

    public int getType() {
        return type;
    }

    @Override
    protected void onAttach() {
        if (!prepared) {
            prepare();
        }
        else {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, glIBO);
        }
    }

    @Override
    protected void onDetach() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    @Override
    public void prepare() {
        int[] temp = new int[1];

        glGenBuffers(1, temp, 0);

        glIBO = temp[0];

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, glIBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, size, indicesData, usage);

        prepared = true;
    }

    @Override
    public void release() {
        if (glIsBuffer(glIBO)) {
            glDeleteBuffers(1, new int[]{glIBO}, 0);
        }

        prepared = false;
    }

    @Override
    public void onEGLContextLost() {
        prepared = false;
    }

}
