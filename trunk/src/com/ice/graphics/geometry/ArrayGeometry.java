package com.ice.graphics.geometry;

import com.ice.graphics.shader.ShaderBinder;
import com.ice.graphics.shader.VertexShader;

/**
 * User: Jason
 * Date: 13-2-16
 */
public class ArrayGeometry extends Geometry {


    public ArrayGeometry(GeometryData geometryData, VertexShader vertexShader, ShaderBinder<VertexShader> vertexShaderBinder) {
        super(geometryData, vertexShader, vertexShaderBinder);
    }

    @Override
    public void draw() {
    }

    @Override
    protected void bindGeometryData(GeometryData data) {
    }

    @Override
    protected void unbindGeometryData(GeometryData data) {
    }

}
