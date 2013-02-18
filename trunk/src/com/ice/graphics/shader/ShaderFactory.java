package com.ice.graphics.shader;

import android.content.res.AssetManager;

import static com.ice.util.Asset.asser2Sting;

/**
 * User: jason
 * Date: 13-2-18
 */
public class ShaderFactory {

    public static VertexShader vertexShader(AssetManager assets, String assertFile) {
        String shaderSrc = asser2Sting(assets, assertFile);
        return new VertexShader(shaderSrc);
    }

    public static FragmentShader fragmentShader(AssetManager assets, String assertFile) {
        String shaderSrc = asser2Sting(assets, assertFile);
        return new FragmentShader(shaderSrc);
    }

}
