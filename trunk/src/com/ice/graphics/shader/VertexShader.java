package com.ice.graphics.shader;

import java.util.Map;

import static android.opengl.GLES20.*;

/**
 * User: jason
 * Date: 13-2-17
 */
public class VertexShader extends Shader {

    public VertexShader(String shaderSrc) {
        super(shaderSrc);
    }

    @Override
    protected int createGlShader() {
        return glCreateShader(GL_VERTEX_SHADER);
    }

    public void preBindAttribute(Map<String, Integer> preBindAttributes) {
        validateProgram();

        if (preBindAttributes.size() > attributeCapacity) {
            throw new IllegalStateException("Too many attribute bound ! while attribute capacity is " + attributeCapacity);
        }

        int glProgram = attachedProgram.getGlProgram();

        for (Map.Entry<String, Integer> entry : preBindAttributes.entrySet()) {
            String attributeName = entry.getKey();
            Integer location = entry.getValue();
            glBindAttribLocation(glProgram, location, attributeName);
        }

    }

    public int findAttribute(String name) {
        if (name == null) throw new IllegalArgumentException();

        validateProgram();

        return glGetAttribLocation(attachedProgram.getGlProgram(), name);
    }

}
