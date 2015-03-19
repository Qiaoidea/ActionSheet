
package com.qiao.actionsheet;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;

public class MaskView extends RelativeLayout {

    protected ViewGroup targetView;
    protected boolean isShowing;
    protected long durationMillis;
    protected boolean canCancel;
    protected MaskListener maskListener;

    public MaskView(Context context, ViewGroup targetView) {
        super(context);
        this.targetView = targetView;
        initialize();
    }

    protected void initialize() {
        setBackgroundColor(0x88000000);
        setVisibility(View.GONE);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        targetView.addView(this, lp);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canCancel) {
                    hide();
                }
            }
        });
    }

    public void setDurationMillis(long durationMillis) {
        this.durationMillis = durationMillis;
    }

    public void setCanCancel(boolean can) {
        this.canCancel = can;
    }

    public void show() {
        if (isShowing)
            return;
        isShowing = true;
        clearAnimation();
        setVisibility(View.VISIBLE);
        AlphaAnimation an = new AlphaAnimation(0, 1);
        an.setDuration(durationMillis);
        startAnimation(an);
        if (maskListener != null)
            maskListener.onShow();
    }

    public void hide() {
        if (!isShowing)
            return;
        isShowing = false;
        clearAnimation();
        AlphaAnimation an = new AlphaAnimation(1, 0);
        an.setDuration(durationMillis);
        an.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.GONE);
            }
        });
        startAnimation(an);
        if (maskListener != null)
            maskListener.onHide();
    }

    public void setOnMaskListener(MaskListener listener) {
        this.maskListener = listener;
    }

    public interface MaskListener {
        void onShow();

        void onHide();
    }
}
