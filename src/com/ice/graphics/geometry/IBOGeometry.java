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
    public void attach() {
        ibo.attach();
        super.attach();
    }

    @Override
    public void detach() {
        ibo.detach();
        super.detach();
    }

    @Override
    public void draw() {
        GeometryData.Descriptor formatDescriptor = getGeometryData().getFormatDescriptor();
        glDrawElements(formatDescriptor.getMode(), formatDescriptor.getCount(), ibo.getType(), 0);
    }

}
