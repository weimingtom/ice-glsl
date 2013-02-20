package com.ice.graphics.geometry;

import com.ice.graphics.CoordinateSystem;
import com.ice.graphics.state_controller.SafeGlStateController;
import com.ice.graphics.shader.FragmentShader;
import com.ice.graphics.shader.ShaderBinder;
import com.ice.graphics.shader.VertexShader;
import com.ice.graphics.texture.Texture;

/**
 * User: jason
 * Date: 13-2-16
 */
public abstract class Geometry extends SafeGlStateController {

    private CoordinateSystem coordinateSystem;

    private GeometryData geometryData;
    private VertexShader vertexShader;
    private ShaderBinder<VertexShader> vertexShaderBinder;

    private Texture texture;
    private FragmentShader fragmentShader;
    private ShaderBinder<FragmentShader> fragmentShaderBinder;

    public Geometry(GeometryData geometryData, VertexShader vertexShader, ShaderBinder<VertexShader> vertexShaderBinder) {
        this.geometryData = geometryData;
        this.vertexShader = vertexShader;
        this.vertexShaderBinder = vertexShaderBinder;

        coordinateSystem = new CoordinateSystem();
    }

    @Override
    protected void onAttach() {
        validateState();

        bindGeometryData(geometryData);
        vertexShader.attach();
        vertexShaderBinder.bind(vertexShader);

        if (texture != null) {
            texture.attach();
        }

        if (fragmentShaderBinder != null) {
            fragmentShaderBinder.bind(fragmentShader);
        }
    }

    @Override
    protected void onDetach() {
        unbindGeometryData(geometryData);
    }

    private void validateState() {
        if (vertexShader == null) {
            throw new IllegalStateException("Vertex shader NULL !");
        }

        if (vertexShaderBinder == null) {
            throw new IllegalStateException("Vertex shader binder NULL !");
        }
    }

    public abstract void draw();

    protected abstract void bindGeometryData(GeometryData data);

    protected abstract void unbindGeometryData(GeometryData data);

    public float[] selfCoordinateSystem() {
        return coordinateSystem.modelMatrix();
    }

    public CoordinateSystem getCoordinateSystem() {
        return coordinateSystem;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public FragmentShader getFragmentShader() {
        return fragmentShader;
    }

    public void setFragmentShader(FragmentShader fragmentShader) {
        this.fragmentShader = fragmentShader;
    }

    public ShaderBinder<FragmentShader> getFragmentShaderBinder() {
        return fragmentShaderBinder;
    }

    public void setFragmentShaderBinder(ShaderBinder<FragmentShader> fragmentShaderBinder) {
        this.fragmentShaderBinder = fragmentShaderBinder;
    }

    public GeometryData getGeometryData() {
        return geometryData;
    }

    public void setGeometryData(GeometryData geometryData) {
        this.geometryData = geometryData;
    }

    public VertexShader getVertexShader() {
        return vertexShader;
    }

    public void setVertexShader(VertexShader vertexShader) {
        this.vertexShader = vertexShader;
    }

    public ShaderBinder<VertexShader> getVertexShaderBinder() {
        return vertexShaderBinder;
    }

    public void setVertexShaderBinder(ShaderBinder<VertexShader> vertexShaderBinder) {
        this.vertexShaderBinder = vertexShaderBinder;
    }

}
