package com.ice.graphics.geometry;

import com.ice.graphics.GlRes;
import com.ice.graphics.GlStateController;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static android.opengl.GLES20.*;
import static com.ice.model.Constants.BYTES_PER_BYTE;
import static com.ice.model.Constants.BYTES_PER_FLOAT;
import static com.ice.model.Constants.BYTES_PER_INT;

public class VBO implements GlStateController, GlRes {

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
    public void attach() {
        if (!prepared) {
            prepare();
        } else {
            glBindBuffer(GL_ARRAY_BUFFER, glVBO);
        }
    }

    @Override
    public void detach() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void prepare() {
        int[] temp = new int[1];

        glGenBuffers(1, temp, 0);

        glVBO = temp[0];

        glBindBuffer(GL_ARRAY_BUFFER, glVBO);

        int bytes = 0;

        if (verticesData instanceof ByteBuffer) {
            bytes = BYTES_PER_BYTE;
        } else if (verticesData instanceof IntBuffer) {
            bytes = BYTES_PER_INT;
        } else if (verticesData instanceof FloatBuffer) {
            bytes = BYTES_PER_FLOAT;
        }

        glBufferData(
                GL_ARRAY_BUFFER,
                verticesData.limit() * bytes,
                verticesData,
                usage
        );

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
