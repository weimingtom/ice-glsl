package com.ice.graphics.geometry;

import android.graphics.Matrix;
import com.ice.graphics.shader.ShaderBinder;
import com.ice.model.Point3F;
import com.ice.util.BufferUtil;

import java.nio.FloatBuffer;

import static android.graphics.Color.*;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static com.ice.graphics.geometry.GeometryData.Descriptor;
import static com.ice.graphics.shader.ShaderBinder.*;

/**
 * User: jason
 * Date: 13-2-17
 */
public class GeometryDataFactory {

    public static GeometryData createTriangleData(float radius) {
        Descriptor descriptor = new Descriptor(GL_TRIANGLES, 3);

        descriptor.addComponent(POSITION, 3);
        descriptor.addComponent(COLOR, 4);
        descriptor.addComponent(ShaderBinder.TEXTURE_COORD, 2);
        descriptor.addComponent(ShaderBinder.NORMAL, 3, true);

        FloatBuffer data = BufferUtil.wrap(new Triangle(radius).vertexes);

        return new GeometryData(data, descriptor);
    }

    public static GeometryData createCubeData(float length) {
        if (length == 0) throw new IllegalArgumentException("length " + length);

        Cube cube = new Cube(length);

        int[] indices = Cube.indices;

        Descriptor descriptor = new Descriptor(GL_TRIANGLES, 6 * 2 * 3);

        descriptor.addComponent(POSITION, 3);
        descriptor.addComponent(COLOR, 4);
        descriptor.addComponent(ShaderBinder.NORMAL, 3);
        descriptor.addComponent(ShaderBinder.TEXTURE_COORD, 2);

        int componentCount = 3 + 4 + 3 + 2;

        float[] data = new float[descriptor.getStride() * descriptor.getCount()];

        for (int i = 0; i < indices.length; i++) {
            int vertexIndex = indices[i];

            int index = 0;

            for (int j = 0; j < 3; j++) {
                data[i * componentCount + index++] = cube.positions[vertexIndex * 3 + j];
            }

            for (int j = 0; j < 4; j++) {
                data[i * componentCount + index++] = Cube.colors[vertexIndex * 4 + j];
            }

            for (int j = 0; j < 3; j++) {
                data[i * componentCount + index++] = Cube.normals[vertexIndex * 3 + j];
            }

            for (int j = 0; j < 2; j++) {
                data[i * componentCount + index++] = Cube.textureCoord[vertexIndex * 2 + j];
            }

        }

        FloatBuffer buffer = BufferUtil.wrap(data);

        return new GeometryData(buffer, descriptor);
    }

    public static GeometryData createPointData(float[] point, int argb, float size) {
        Point3F point3f = new Point3F(point[0], point[1], point[2]);

        return createPointData(point3f, argb, size);
    }

    public static GeometryData createPointData(Point3F point, int argb, float size) {
        Descriptor descriptor = new Descriptor(GL_POINTS, 1);
        descriptor.addComponent(POSITION, 3);
        descriptor.addComponent(COLOR, 4);
        descriptor.addComponent(POINT_SIZE, 1);

        float red = red(argb) / 255f;
        float green = green(argb) / 255f;
        float blue = blue(argb) / 255f;
        float alpha = alpha(argb) / 255f;

        FloatBuffer floatBuffer = BufferUtil.wrap(
                point.x, point.y, point.z, red, green, blue, alpha, size
        );

        return new GeometryData(floatBuffer, descriptor);
    }

    public static class Triangle {
        private static final float Z = 0;
        public final float[] vertexes;

        public Triangle(float radius) {
            float[] top = new float[]{0, radius};
            float[] left = new float[2];
            float[] right = new float[2];

            Matrix matrix = new Matrix();
            matrix.postRotate(120, 0, 0);
            matrix.mapPoints(left, top);

            matrix.postRotate(120, 0, 0);
            matrix.mapPoints(right, top);

            vertexes = new float[]{
                    top[0], top[1], Z, //pos
                    1.0f, 0.0f, 0.0f, 1.0f,//color
                    0.5f, 1.0f,//texture
                    0f, 0f, 1.0f, //normal

                    left[0], left[1], Z, //pos
                    0.0f, 1.0f, 0.0f, 1.0f,//color
                    0.0f, 0.0f,//texture
                    0f, 0f, 1.0f, //normal

                    right[0], right[1], Z,//pos
                    0.0f, 0.0f, 1.0f, 1.0f,//color
                    1.0f, 0.0f,//texture
                    0f, 0f, 1.0f, //normal
            };
        }


    }

    /**
     * v6----- v5
     * /|      /|
     * v1------v0|
     * | |     | |
     * | |v7---|-|v4
     * |/      |/
     * v2------v3
     */
    public static class Cube {
        float length;
        private final float[] positions;

        public Cube(float length) {
            this.length = length;

            float halfLength = length / 2;

            Point3F v0 = new Point3F(halfLength, halfLength, halfLength);
            Point3F v1 = new Point3F(-halfLength, halfLength, halfLength);
            Point3F v2 = new Point3F(-halfLength, -halfLength, halfLength);
            Point3F v3 = new Point3F(halfLength, -halfLength, halfLength);

            Point3F v4 = new Point3F(halfLength, -halfLength, -halfLength);
            Point3F v5 = new Point3F(halfLength, halfLength, -halfLength);
            Point3F v6 = new Point3F(-halfLength, halfLength, -halfLength);
            Point3F v7 = new Point3F(-halfLength, -halfLength, -halfLength);

            // X, Y, Z
            positions = new float[]{
                    v0.x, v0.y, v0.z,
                    v1.x, v1.y, v1.z,
                    v2.x, v2.y, v2.z,
                    v3.x, v3.y, v3.z,        // v0-v1-v2-v3

                    v0.x, v0.y, v0.z,
                    v3.x, v3.y, v3.z,
                    v4.x, v4.y, v4.z,
                    v5.x, v5.y, v5.z,        // v0-v3-v4-v5

                    v0.x, v0.y, v0.z,
                    v5.x, v5.y, v5.z,
                    v6.x, v6.y, v6.z,
                    v1.x, v1.y, v1.z,        // v0-v5-v6-v1

                    v1.x, v1.y, v1.z,
                    v6.x, v6.y, v6.z,
                    v7.x, v7.y, v7.z,
                    v2.x, v2.y, v2.z,    // v1-v6-v7-v2

                    v7.x, v7.y, v7.z,
                    v4.x, v4.y, v4.z,
                    v3.x, v3.y, v3.z,
                    v2.x, v2.y, v2.z,    // v7-v4-v3-v2

                    v4.x, v4.y, v4.z,
                    v7.x, v7.y, v7.z,
                    v6.x, v6.y, v6.z,
                    v5.x, v5.y, v5.z,   // v4-v7-v6-v5
            };
        }

        // R, G, B, A
        public static final float[] colors = {
                1, 1, 1, 1,
                1, 1, 0, 1,
                1, 0, 0, 1,
                1, 0, 1, 1,              // v0-v1-v2-v3
                1, 1, 1, 1,
                1, 0, 1, 1,
                0, 0, 1, 1,
                0, 1, 1, 1,              // v0-v3-v4-v5
                1, 1, 1, 1,
                0, 1, 1, 1,
                0, 1, 0, 1,
                1, 1, 0, 1,              // v0-v5-v6-v1
                1, 1, 0, 1,
                0, 1, 0, 1,
                0, 0, 0, 1,
                1, 0, 0, 1,              // v1-v6-v7-v2
                0, 0, 0, 1,
                0, 0, 1, 1,
                1, 0, 1, 1,
                1, 0, 0, 1,              // v7-v4-v3-v2
                0, 0, 1, 1,
                0, 0, 0, 1,
                0, 1, 0, 1,
                0, 1, 1, 1             // v4-v7-v6-v5
        };

        // X, Y, Z
        // The normal is used in light calculations and is a vector which points
        // orthogonal to the plane of the surface. For a cube model, the normals
        // should be orthogonal to the points of each face.
        public static final float[] normals = {
                0, 0, 1,
                0, 0, 1,
                0, 0, 1,
                0, 0, 1,             // v0-v1-v2-v3
                1, 0, 0,
                1, 0, 0,
                1, 0, 0,
                1, 0, 0,              // v0-v3-v4-v5
                0, 1, 0,
                0, 1, 0,
                0, 1, 0,
                0, 1, 0,              // v0-v5-v6-v1
                -1, 0, 0,
                -1, 0, 0,
                -1, 0, 0,
                -1, 0, 0,          // v1-v6-v7-v2
                0, -1, 0,
                0, -1, 0,
                0, -1, 0,
                0, -1, 0,         // v7-v4-v3-v2
                0, 0, -1,
                0, 0, -1,
                0, 0, -1,
                0, 0, -1        // v4-v7-v6-v5
        };

        public static final float[] textureCoord = new float[]{
                1, 1,
                0, 1,
                0, 0,
                1, 0,                    // v0-v1-v2-v3
                0, 1,
                0, 0,
                1, 0,
                1, 1,              // v0-v3-v4-v5
                1, 0,
                1, 1,
                0, 1,
                0, 0,              // v0-v5-v6-v1 (top)
                1, 1,
                0, 1,
                0, 0,
                1, 0,              // v1-v6-v7-v2
                1, 1,
                0, 1,
                0, 0,
                1, 0,              // v7-v4-v3-v2 (bottom)
                0, 0,
                1, 0,
                1, 1,
                0, 1             // v4-v7-v6-v5
        };

        public static final int[] indices = new int[]{
                0, 1, 2,
                0, 2, 3,
                4, 5, 6,
                4, 6, 7,
                8, 9, 10,
                8, 10, 11,
                12, 13, 14,
                12, 14, 15,
                16, 17, 18,
                16, 18, 19,
                20, 21, 22,
                20, 22, 23
        };
    }

}
