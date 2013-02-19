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

    public void uploadAttribute(String name, float... values) {
        int location = findAttribute(name);

        if (location == -1) {
            throw new IllegalStateException(name + " not found !");
        }
        else {
            switch (values.length) {
                case 1:
                    glVertexAttrib1f(location, values[0]);
                    break;
                case 2:
                    glVertexAttrib2fv(location, values, 0);
                    break;
                case 3:
                    glVertexAttrib3fv(location, values, 0);
                    break;
                case 4:
                    glVertexAttrib4fv(location, values, 0);
                    break;

                default:
                    throw new IllegalArgumentException();
            }

        }
    }

}
