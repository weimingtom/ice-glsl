package com.ice.entity;

import com.ice.graphics.GlStateController;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Jason
 * Date: 13-2-8
 */
public class Entity {

    private List<GlStateController> glStateControllers;

    public Entity() {
        glStateControllers = new ArrayList<GlStateController>();
    }

    public void addGlStateControllers(GlStateController controller) {
        glStateControllers.add(controller);
    }

    public void removeGlStateControllers(GlStateController controller) {
        glStateControllers.remove(controller);
    }

}
