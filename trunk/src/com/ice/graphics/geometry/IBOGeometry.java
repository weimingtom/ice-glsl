package com.ice.graphics.geometry;

import com.ice.graphics.shader.ShaderBinder;
import com.ice.graphics.shader.VertexShader;

import java.util.Map;

import static android.opengl.GLES20.glDrawElements;

/**
 * User: jason
 * Date: 13-2-17
 */
public class IBOGeometry extends VBOGeometry {

    private IBO ibo;

    public IBOGeometry(IndexedGeometryData geometryData, VertexShader vertexShader) {
        super(geometryData, vertexShader);
        buildIBO(geometryData);
    }

    public IBOGeometry(IndexedGeometryData geometryData, VertexShader vertexShader, Map<String, String> attributeNameMap) {
        super(geometryData, vertexShader, attributeNameMap);
        buildIBO(geometryData);
    }

    public IBOGeometry(IndexedGeometryData geometryData, VertexShader vertexShader, ShaderBinder<VertexShader> vertexShaderBinder) {
        super(geometryData, vertexShader, vertexShaderBinder);
        buildIBO(geometryData);
    }

    private void buildIBO(IndexedGeometryData indexedGeometryData) {
        ibo = new IBO(indexedGeometryData.getIndexData());
    }

    @Override
    protected void bindGeometryData(GeometryData data) {
        ibo.attach();
        super.bindGeometryData(data);
    }

    @Override
    protected void unbindGeometryData(GeometryData data) {
        ibo.detach();
        super.unbindGeometryData(data);
    }

    @Override
    public void draw() {
        GeometryData.Descriptor formatDescriptor = getGeometryData().getFormatDescriptor();
        glDrawElements(formatDescriptor.getMode(), formatDescriptor.getCount(), ibo.getType(), 0);
    }

}
