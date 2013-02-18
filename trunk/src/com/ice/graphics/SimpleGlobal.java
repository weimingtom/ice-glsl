package com.ice.graphics;

import static android.opengl.Matrix.perspectiveM;
import static android.opengl.Matrix.setLookAtM;

/**
 * User: jason
 * Date: 13-2-18
 */
public class SimpleGlobal implements CoordinateSystem.Global {
    private float[] viewMatrix, projectMatrix;

    public SimpleGlobal() {
        viewMatrix = new float[4 * 4];
        projectMatrix = new float[4 * 4];
    }

    public void eye(float eyeZ) {
        eye(
                0, 0, eyeZ,
                0, 0, eyeZ - 10,
                0, 1, 0
        );
    }

    public void eye(float eyeX, float eyeY, float eyeZ,
                    float centerX, float centerY, float centerZ,
                    float upX, float upY, float upZ) {
        setLookAtM(
                viewMatrix, 0,
                eyeX, eyeY, eyeZ,
                centerX, centerY, centerZ,
                upX, upY, upZ
        );

    }

    public void perspective(float fovy, float aspect, float zNear, float zFar) {
        perspectiveM(projectMatrix, 0, fovy, aspect, zNear, zFar);
    }

    @Override
    public float[] viewMatrix() {
        return viewMatrix;
    }

    @Override
    public float[] projectMatrix() {
        return projectMatrix;
    }

}
