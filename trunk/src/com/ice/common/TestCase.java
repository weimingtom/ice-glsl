package com.ice.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

/**
 * Activity class for example attachedProgram that detects OpenGL ES 2.0.
 */
public abstract class TestCase extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!supportOpenGLES20()) {
            Log.e("HelloTriangle", "OpenGL ES 2.0 not supported on device.  Exiting...");
            finish();
            return;
        }

        setContentView(mGLSurfaceView = new GLSurfaceView(this));

        // Tell the surface view we want to create an OpenGL ES 2.0-compatible
        // context, and set an OpenGL ES 2.0-compatible renderer.
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(buildRenderer());
    }

    protected abstract GLSurfaceView.Renderer buildRenderer();

    private boolean supportOpenGLES20() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        return (info.reqGlEsVersion >= 0x20000);
    }

    @Override
    protected void onResume() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        mGLSurfaceView.onPause();
    }

    private GLSurfaceView mGLSurfaceView;
}
