package com.ice.graphics.geometry;

import com.ice.graphics.shader.VertexShader;

import static android.opengl.GLES20.glDrawElements;

/**
 * User: jason
 * Date: 13-2-17
 */
public class IBOGeometry extends VBOGeometry {

    private IBO ibo;

    public IBOGeometry(IndexedGeometryData indexedGeometryData, VertexShader vertexShader) {
        super(indexedGeometryData, vertexShader);
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
