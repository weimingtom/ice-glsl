package com.ice.util;

import android.opengl.Matrix;

/**
 * User: jason
 * Date: 13-2-18
 */
public class Math {
    public static final float[] IDENTITY_MATRIX_4F;

    static {
        IDENTITY_MATRIX_4F = new float[4 * 4];
        Matrix.setIdentityM(IDENTITY_MATRIX_4F, 0);
    }

}
