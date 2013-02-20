package com.ice.graphics.geometry;

import android.util.Log;
import com.ice.graphics.shader.ShaderBinder;
import com.ice.graphics.shader.VertexShader;

import java.util.List;
import java.util.Map;

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

    public VBOGeometry(GeometryData geometryData, VertexShader vertexShader) {
        this(
                geometryData,
                vertexShader,
                new Binder(geometryData.getFormatDescriptor(), null)
        );
    }

    public VBOGeometry(GeometryData geometryData, VertexShader vertexShader, Map<String, String> attributeNameMap) {
        this(
                geometryData,
                vertexShader,
                new Binder(geometryData.getFormatDescriptor(), attributeNameMap)
        );
    }

    public VBOGeometry(GeometryData geometryData, VertexShader vertexShader, ShaderBinder<VertexShader> vertexShaderBinder) {
        super(geometryData, vertexShader, vertexShaderBinder);

        vbo = new VBO(geometryData.getVertexData());
    }

    @Override
    protected void bindGeometryData(GeometryData data) {
        vbo.attach();


    }

    @Override
    protected void unbindGeometryData(GeometryData data) {
        vbo.detach();
    }

    @Override
    public void draw() {
        Descriptor formatDescriptor = getGeometryData().getFormatDescriptor();
        glDrawArrays(formatDescriptor.getMode(), 0, formatDescriptor.getCount());
    }

    public static class Binder implements ShaderBinder<VertexShader> {

        private boolean errorPrinted;
        private Descriptor formatDescriptor;

        public Binder(Descriptor formatDescriptor, Map<String, String> attributeNameMap) {
            if (attributeNameMap != null) {
                Descriptor transformedDescriptor = formatDescriptor.deepClone();

                List<Component> components = transformedDescriptor.getComponents();

                for (Component component : components) {
                    if (!attributeNameMap.containsKey(component.name)) {
                        Log.w(TAG, "transformNames " + component.name + " not in map !");
                    } else {
                        component.name = attributeNameMap.get(component.name);
                    }
                }

                this.formatDescriptor = transformedDescriptor;
            } else {
                this.formatDescriptor = formatDescriptor;
            }

        }

        @Override
        public void bind(VertexShader shader) {

            List<GeometryData.Component> components = formatDescriptor.getComponents();

            boolean error = false;

            for (Component component : components) {

                int attribute = shader.findAttribute(component.name);

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
                            formatDescriptor.getStride(),
                            component.offset
                    );

                    glEnableVertexAttribArray(attribute);
                }
            }

            errorPrinted = error;
        }

    }

}
