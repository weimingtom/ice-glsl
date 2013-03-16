package com.ice.graphics;

import com.ice.graphics.state_controller.SafeGlStateController;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static android.opengl.GLES20.*;
import static com.ice.model.Constants.*;

public class VBO extends SafeGlStateController implements GlRes {

    private int glVBO;
    private int usage;
    private boolean prepared;
    private Buffer verticesData;

    public VBO(Buffer data) {
        this(data, GL_STATIC_DRAW);
    }

    public VBO(Buffer data, int usage) {
        verticesData = data;
        this.usage = usage;
    }

    @Override
    protected void onAttach() {
        if (!prepared) {
            prepare();
        }
        else {
            glBindBuffer(GL_ARRAY_BUFFER, glVBO);
        }
    }

    @Override
    protected void onDetach() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void prepare() {
        if (prepared) return;

        int[] temp = new int[1];

        glGenBuffers(1, temp, 0);

        glVBO = temp[0];

        glBindBuffer(GL_ARRAY_BUFFER, glVBO);

        int bytes = 0;

        if (verticesData instanceof ByteBuffer) {
            bytes = BYTES_PER_BYTE;
        }
        else if (verticesData instanceof IntBuffer) {
            bytes = BYTES_PER_INT;
        }
        else if (verticesData instanceof FloatBuffer) {
            bytes = BYTES_PER_FLOAT;
        }

        glBufferData(
                GL_ARRAY_BUFFER,
                verticesData.limit() * bytes,
                verticesData,
                usage
        );

        prepared = true;
    }

    @Override
    public int glRes() {
        return glVBO;
    }

    @Override
    public void release() {
        if (glIsBuffer(glVBO)) {
            glDeleteBuffers(1, new int[]{glVBO}, 0);
        }

        prepared = false;
    }

    @Override
    public void onEGLContextLost() {
        prepared = false;
    }

}
