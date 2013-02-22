package com.ice.graphics.geometry;

import android.util.Log;
import com.ice.graphics.VBO;
import com.ice.graphics.shader.FragmentShader;
import com.ice.graphics.shader.VertexShader;

import java.util.List;

import static android.opengl.GLES20.*;
import static com.ice.graphics.geometry.GeometryData.Component;
import static com.ice.graphics.geometry.GeometryData.Descriptor;

/**
 * User: Jason
 * Date: 13-2-16
 */
public class VBOGeometry extends Geometry {

    private static final String TAG = "VBOGeometry";

    private VBO vbo;

    public VBOGeometry(GeometryData data) {
        this(data, null, null);
    }

    public VBOGeometry(GeometryData data, VertexShader vsh) {
        this(data, vsh, null);
    }

    public VBOGeometry(GeometryData data, VertexShader vsh, FragmentShader fsh) {
        super(data, vsh, fsh);

        vbo = new VBO(data.getVertexData());

        setBinder(
                new EasyBinder(getGeometryData().getFormatDescriptor())
        );
    }

    @Override
    protected void bindShaderData(GeometryData data, VertexShader vsh, FragmentShader fsh) {
        vbo.attach();

        super.bindShaderData(data, vsh, fsh);
    }

    @Override
    protected void unbindShaderData(GeometryData data, VertexShader vsh, FragmentShader fsh) {
        vbo.detach();

        super.unbindShaderData(data, vsh, fsh);
    }

    @Override
    public void draw() {
        Descriptor formatDescriptor = getGeometryData().getFormatDescriptor();
        glDrawArrays(formatDescriptor.getMode(), 0, formatDescriptor.getCount());
    }

    public static class EasyBinder implements Geometry.Binder {

        private boolean errorPrinted;
        private Descriptor descriptor;

        public EasyBinder(Descriptor descriptor) {
            this.descriptor = descriptor;
        }

        @Override
        public void bind(GeometryData data, VertexShader vsh, FragmentShader fsh) {

            List<GeometryData.Component> components = descriptor.getComponents();

            boolean error = false;

            for (Component component : components) {

                int attribute = vsh.findAttribute(component.name);

                if (attribute < 0) {
                    error = true;

                    if (!errorPrinted) {
                        Log.e(TAG, "attribute " + component.name + " not found in vertex shader ");
                    }

                } else {
                    glVertexAttribPointer(
                            attribute,
                            component.dimension,
                            component.type,
                            component.normalized,
                            descriptor.getStride(),
                            component.offset
                    );

                    glEnableVertexAttribArray(attribute);
                }
            }

            errorPrinted = error;

        }

        @Override
        public void unbind(GeometryData data, VertexShader vsh, FragmentShader fsh) {

        }

    }

}
