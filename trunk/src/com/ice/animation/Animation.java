package com.ice.animation;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import com.ice.graphics.state_controller.SafeGlStateController;

public abstract class Animation extends SafeGlStateController {

    public static final int FOREVER = Integer.MIN_VALUE;
    private static final long NOT_STARTED = 0;

    public interface Listener {
        void onAnimationEnd();
    }

    public Animation() {
        interpolator = new AccelerateDecelerateInterpolator();
        startTime = NOT_STARTED;
    }

    public Animation(long duration) {
        this();

        setDuration(duration);
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    protected void start() {
        finished = false;
        startTime = AnimationUtils.currentAnimationTimeMillis();
    }

    public void cancel() {
        cancel = true;
    }

    @Override
    protected void onAttach() {
        if (startTime == NOT_STARTED)
            start();

        long currentTime = AnimationUtils.currentAnimationTimeMillis();

        if (currentTime - startTime < offset)
            return;

        boolean over = currentTime - (startTime + offset) > duration;

        if (over) {
            if (loopTimes > 0) {
                start();
                loopTimes--;
            } else if (loopTimes == FOREVER) {
                start();
            } else {
                finished = true;
            }
        }

        float normalizedTime = 0;

        if (over) {
            normalizedTime = 1.0f;
        } else {
            if (duration != 0 && currentTime >= startTime + offset) {
                normalizedTime = ((float) (currentTime - startTime - offset)) / (float) duration;
            }
        }

        //根据归一化时间调整时间插值
        float interpolatedTime = interpolator.getInterpolation(normalizedTime);

        onAttach(interpolatedTime);

        attached = true;
    }

    @Override
    protected void onDetach() {
        if (attached) {
            //todo
            attached = false;
        }

        if (isCompleted()) {
            onComplete();
        }
    }

    public void onComplete() {

        if (fillAfter)
            applyFillAfter();

        if (listener != null)
            listener.onAnimationEnd();
    }

    protected void applyFillAfter() {

    }

    protected abstract void onAttach(float interpolatedTime);

    public long getDuration() {
        return duration;
    }

    public boolean isCompleted() {
        return finished;
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public void setLoopTimes(int loopTimes) {
        this.loopTimes = loopTimes;
    }

    public boolean isCanceled() {
        return cancel;
    }

    public void setFillAfter(boolean fillAfter) {
        this.fillAfter = fillAfter;
    }

    public boolean isFillAfter() {
        return fillAfter;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    private boolean attached;

    private long offset;

    private boolean finished;

    private boolean fillAfter = true;

    protected long startTime;
    protected long duration;

    protected int loopTimes;

    private boolean cancel;

    private Interpolator interpolator;
    private Listener listener;
}
