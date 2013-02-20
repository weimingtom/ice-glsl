package com.ice.graphics.geometry;

import com.ice.graphics.SafeGlStateController;

/**
 * User: jason
 * Date: 13-2-16
 */
public class IBO extends SafeGlStateController {

    private int type;

    @Override
    protected void onAttach() {
    }

    @Override
    protected void onDetach() {
    }

    public int getType() {
        return type;
    }
}
