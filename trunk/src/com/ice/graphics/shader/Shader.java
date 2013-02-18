//
// Book:      OpenGL(R) ES 2.0 Programming Guide
// Authors:   Aaftab Munshi, Dan Ginsburg, Dave Shreiner
// ISBN-10:   0321502795
// ISBN-13:   9780321502797
// Publisher: Addison-Wesley Professional
// URLs:      http://safari.informit.com/9780321563835
//            http://www.opengles-book.com
//

// Shader
//
//    Utility functions for loading shaders and creating attachedProgram objects.
//

package com.ice.graphics.shader;

import android.util.Log;
import com.ice.exception.FailException;
import com.ice.graphics.GlStateController;

import java.util.HashMap;
import java.util.Map;

import static android.opengl.GLES20.*;

public abstract class Shader implements GlStateController {

    protected static int attributeCapacity = 8;
    private static final String TAG = "Shader";

    public static void setAttributeCapacity(int attributeCapacity) {
        Shader.attributeCapacity = attributeCapacity;
        Log.i(TAG, "Attribute capacity = " + attributeCapacity);
    }

    public static int getAttributeCapacity() {
        return attributeCapacity;
    }

    private String shaderSrc;

    private int glShader;
    protected Program attachedProgram;

    private Map<String, Integer> uniforms;

    protected Shader(String shaderSrc) {
        if (shaderSrc == null || shaderSrc.length() == 0) {
            throw new IllegalArgumentException(" src " + shaderSrc);
        }

        this.shaderSrc = shaderSrc;

        uniforms = new HashMap<String, Integer>();

        // Create the shader object
        glShader = createGlShader();

        if (glShader == 0) {
            throw new FailException("Create shader failed !");
        }

        // Load the shader source
        glShaderSource(glShader, shaderSrc);

        // Compile the shader
        glCompileShader(glShader);

        // Check the compile status
        int[] compiled = new int[1];
        glGetShaderiv(glShader, GL_COMPILE_STATUS, compiled, 0);

        if (compiled[0] == GL_FALSE) {
            String log = glGetShaderInfoLog(glShader);
            glDeleteShader(glShader);
            throw new FailException("Compile failed ! " + log);
        }
    }

    protected abstract int createGlShader();

    @Override
    public void attach() {
        validateProgram();

        if (!attachedProgram.isActive()) {
            attachedProgram.attach();
        }

    }

    @Override
    public void detach() {
        validateProgram();

        attachedProgram.detach();
    }

    public void onAttachToProgram(Program attachedProgram) {
        this.attachedProgram = attachedProgram;
    }

    protected void validateProgram() {
        if (attachedProgram == null) {
            throw new IllegalStateException("Not attached to a program yet !");
        }
    }

    public boolean isActive() {
        return attachedProgram != null && attachedProgram.isActive();
    }

    public void bindUniform(String name, float[] matrix) {
        int location = getUniformLocation(name);

        if (location == -1) {
            throw new IllegalStateException(name + " not found !");
        } else {
            glUniformMatrix4fv(location, 1, false, matrix, 0);
        }
    }

    public void bindUniform(String name, int x, int y, int z) {
        int location = getUniformLocation(name);

        if (location == -1) {
            throw new IllegalStateException(name + " not found !");
        } else {
            glUniform3i(location, x, y, z);
        }
    }

    public void bindUniform(String name, float x, float y, float z) {
        int location = getUniformLocation(name);

        if (location == -1) {
            throw new IllegalStateException(name + " not found !");
        } else {
            glUniform3f(location, x, y, z);
        }
    }

    public int getUniformLocation(String name) {
        validateProgram();

        if (uniforms.containsKey(name)) return uniforms.get(name);

        int location = glGetUniformLocation(attachedProgram.getGlProgram(), name);

        uniforms.put(name, location);

        return location;
    }


    int getGlShader() {
        return glShader;
    }

}
