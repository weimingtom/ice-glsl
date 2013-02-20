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
import com.ice.graphics.state_controller.SafeGlStateController;

import java.util.HashMap;
import java.util.Map;

import static android.opengl.GLES20.*;

public abstract class Shader extends SafeGlStateController {

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
    protected void onAttach() {
        validateProgram();

        if (!attachedProgram.isActive()) {
            attachedProgram.attach();
        }
    }

    @Override
    protected void onDetach() {
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

    public void uploadUniform(String name, float... values) {
        int location = findUniform(name);

        if (location == -1)
            throw new IllegalStateException(name + " not found !");

        switch (values.length) {
            case 1:
                glUniform1f(location, values[0]);
                break;
            case 2:
                glUniform2f(location, values[0], values[1]);
                break;
            case 3:
                glUniform3f(location, values[0], values[1], values[2]);
                break;
            case 4:
                glUniform4f(location, values[0], values[1], values[2], values[3]);
                break;
            case 16:
                glUniformMatrix4fv(location, 1, false, values, 0);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void uploadUniform(String name, int... values) {
        int location = findUniform(name);

        if (location == -1)
            throw new IllegalStateException(name + " not found !");

        switch (values.length) {
            case 1:
                glUniform1i(location, values[0]);
                break;
            case 2:
                glUniform2i(location, values[0], values[1]);
                break;
            case 3:
                glUniform3i(location, values[0], values[1], values[2]);
                break;
            case 4:
                glUniform4i(location, values[0], values[1], values[2], values[3]);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public int findUniform(String name) {
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
