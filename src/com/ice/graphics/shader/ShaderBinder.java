package com.ice.graphics.shader;

/**
 * User: jason
 * Date: 13-2-18
 */
public interface ShaderBinder<T extends Shader> {

    String POSITION = "a_Positions";
    String COLOR = "a_Color";
    String NORMAL = "a_Normal";
    String TEXTURE_COORD = "a_Texture";

    String M_MATRIX = "u_mMatrix";
    String M_V_MATRIX = "u_mvMatrix";
    String M_V_P_MATRIX = "u_mvpMatrix";

    void bind(T shader);

}
