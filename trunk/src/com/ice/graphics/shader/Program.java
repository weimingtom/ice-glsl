package com.ice.graphics.shader;

import com.ice.exception.FailException;
import com.ice.graphics.GlRes;
import com.ice.graphics.SafeGlStateController;

import static android.opengl.GLES20.*;

/**
 * User: jason
 * Date: 13-2-5
 */
public class Program extends SafeGlStateController implements GlRes {

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
            String info = glGetProgramInfoLog(glProgram);
            glDeleteProgram(glProgram);
            throw new FailException("Link failed ! " + info);
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
    protected void onAttach() {
        if (vertexShader == null || fragmentShader == null) {
            throw new IllegalStateException();
        }

        glUseProgram(glProgram);

        using = this;
    }

    @Override
    protected void onDetach() {
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
