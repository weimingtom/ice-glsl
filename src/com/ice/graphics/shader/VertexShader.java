package com.ice.graphics.shader;

import java.util.HashMap;
import java.util.Map;

import static android.opengl.GLES20.*;

/**
 * User: jason
 * Date: 13-2-17
 */
public class VertexShader extends Shader {

    private Map<String, Integer> attributes;

    public VertexShader(String shaderSrc) {
        super(shaderSrc);

        attributes = new HashMap<String, Integer>();
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

        if (attributes.containsKey(name)) return attributes.get(name);

        int attribute = glGetAttribLocation(attachedProgram.getGlProgram(), name);

        attributes.put(name, attribute);

        return attribute;
    }

    public void uploadAttribute(String name, float... values) {
        int attribute = findAttribute(name);

        if (attribute == -1) {
            throw new IllegalStateException(name + " not found !");
        } else {
            uploadAttribute(attribute, values);
        }

    }

    public void uploadAttribute(int attribute, float... values) {
        switch (values.length) {
            case 1:
                glVertexAttrib1f(attribute, values[0]);
                break;
            case 2:
                glVertexAttrib2fv(attribute, values, 0);
                break;
            case 3:
                glVertexAttrib3fv(attribute, values, 0);
                break;
            case 4:
                glVertexAttrib4fv(attribute, values, 0);
                break;

            default:
                throw new IllegalArgumentException();
        }
    }

}
