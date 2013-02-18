package com.ice.graphics;


/**
 * User: ice
 * Date: 11-11-15
 * Time: 下午3:26
 */
public interface GlRes {

    void prepare();

    //void recycle(GL10 gl);

    void release();

    void onEGLContextLost();

}
