package com.ice.graphics;

import android.opengl.Matrix;

import static com.ice.util.Math.IDENTITY_MATRIX_4F;

/**
 * User: jason
 * Date: 13-2-18
 */
public class CoordinateSystem {
    private static final float[] TEMP_MATRIX = new float[4 * 4];

    public static final float[] M_V_MATRIX = new float[4 * 4];
    public static final float[] M_V_P_MATRIX = new float[4 * 4];

    public interface Global {
        float[] viewMatrix();

        float[] projectMatrix();
    }

    public static Global global;

    public static void buildGlobal(Global global) {
        CoordinateSystem.global = global;
    }

    public static Global global() {
        return global;
    }

    private float[] modelMatrix;

    public CoordinateSystem() {
        modelMatrix = new float[4 * 4];

        System.arraycopy(IDENTITY_MATRIX_4F, 0, modelMatrix, 0, modelMatrix.length);
    }

    public float[] modelMatrix() {
        return modelMatrix;
    }

    public void modelViewMatrix(float[] output) {
        if (output.length != 4 * 4) {
            throw new IllegalArgumentException();
        }

        Matrix.multiplyMM(
                output, 0,
                global.viewMatrix(), 0,
                modelMatrix, 0
        );
    }

    public void modelViewProjectMatrix(float[] output) {
        if (output.length != 4 * 4) {
            throw new IllegalArgumentException();
        }

        Matrix.multiplyMM(
                TEMP_MATRIX, 0,
                global.viewMatrix(), 0,
                modelMatrix, 0
        );

        Matrix.multiplyMM(
                output, 0,
                global.projectMatrix(), 0,
                TEMP_MATRIX, 0
        );

    }

}
