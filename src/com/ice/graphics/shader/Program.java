package com.ice.graphics.shader;

import com.ice.exception.FailException;
import com.ice.graphics.GlRes;
import com.ice.graphics.GlStateController;

import static android.opengl.GLES20.*;

/**
 * User: jason
 * Date: 13-2-5
 */
public class Program implements GlStateController, GlRes {

    public static Program using;

    private int glProgram;
    private boolean linked;
    private VertexShader vertexShader;
    private FragmentShader fragmentShader;

    public Program() {
        // Create the attachedProgram object
        glProgram = glCreateProgram();

        if (glProgram == 0) {
            throw new FailException("Create attachedProgram failed !");
        }
    }

    int getGlProgram() {
        return glProgram;
    }

    public void attachShader(VertexShader vertexShader, FragmentShader fragmentShader) {
        glAttachShader(glProgram, vertexShader.getGlShader());
        glAttachShader(glProgram, fragmentShader.getGlShader());

        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;

        vertexShader.onAttachToProgram(this);
        fragmentShader.onAttachToProgram(this);
    }

    public VertexShader getVertexShader() {
        return vertexShader;
    }

    public FragmentShader getFragmentShader() {
        return fragmentShader;
    }

    public void link() {
        // Link the attachedProgram
        glLinkProgram(glProgram);

        // Check the link status
        int[] link = new int[1];
        glGetProgramiv(glProgram, GL_LINK_STATUS, link, 0);

        if (link[0] == GL_FALSE) {
            StringBuilder sb = new StringBuilder();

            String info = glGetProgramInfoLog(glProgram);

            while (info != null) {
                sb.append(info);
                info = glGetProgramInfoLog(glProgram);
            }

            glDeleteProgram(glProgram);
            throw new FailException("Link failed ! " + sb.toString());
        }

        // Free up no longer needed shader resources
        glDeleteShader(vertexShader.getGlShader());
        glDeleteShader(fragmentShader.getGlShader());

        linked = true;
    }

    public boolean isLinked() {
        return linked;
    }

    @Override
    public void attach() {
        if (vertexShader == null || fragmentShader == null) {
            throw new IllegalStateException();
        }

        glUseProgram(glProgram);

        using = this;
    }

    @Override
    public void detach() {
        glUseProgram(0);
    }

    @Override
    public void prepare() {
    }

    @Override
    public void release() {
    }

    @Override
    public void onEGLContextLost() {
        glProgram = 0;
    }

    public boolean isActive() {
        return this == using;
    }

}
