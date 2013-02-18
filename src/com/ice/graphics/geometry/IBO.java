package com.ice.graphics.geometry;

import com.ice.graphics.GlStateController;

/**
 * User: jason
 * Date: 13-2-16
 */
public class IBO implements GlStateController {

    private int type;

    @Override
    public void attach() {
    }

    @Override
    public void detach() {
    }

    public int getType() {
        return type;
    }
}
