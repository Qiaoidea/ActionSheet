
package com.qiao.actionsheet.util;

import com.qiao.actionsheet.R;
import com.qiao.actionsheet.util.MaskView.MaskListener;
import com.qiao.actionsheet.util.Method.Action1;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ActionSheet extends RelativeLayout {
    protected final static long durationMillis = 200;
    protected WindowManager windowManager;
    protected GestureDetector gestureDetector;
    protected MaskView maskView;
    protected LinearLayout actionSheetView;
    protected TextView titleTextView;

    public ActionSheet(Context context) {
        super(context);
        initialize();
    }

    public ActionSheet(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ActionSheet(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    protected void initialize() {
        maskView = new MaskView(getContext(), this);
        maskView.setCanCancel(true);
        maskView.setDurationMillis(durationMillis);
        maskView.setOnMaskListener(new MaskListener() {
            @Override
            public void onShow() {
            }

            @Override
            public void onHide() {
                hide();
            }
        });

        actionSheetView = new LinearLayout(getContext());
        actionSheetView.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) UnitConverter.applyDimension(getContext(), TypedValue.COMPLEX_UNIT_DIP, 20);
        actionSheetView.setPadding(pad, pad, pad, pad);
//        actionSheetView.setBackgroundResource(R.drawable.actionsheet_background);
        actionSheetView.setBackgroundColor(0x80000000);
        actionSheetView.setVisibility(View.GONE);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        addView(actionSheetView, rlp);

        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        gestureDetector = new GestureDetector(getContext(), new SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                hide();
                return super.onSingleTapUp(e);
            }
        });
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_BACK == keyCode && KeyEvent.ACTION_DOWN == event.getAction()) {
                    hide();
                    return true;
                }
                return false;
            }
        });
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    public void show(String[] displayStrings, Action1<Integer> callback) {
        show(null,displayStrings, callback, true);
    }
    
    public void show(String[] displayStrings,boolean[] isShow,Action1<Integer> callback){
    	 show(null,displayStrings, isShow,callback, true);
    }
    
    public void show(String title,String[] displayStrings, Action1<Integer> callback) {
        show(title,displayStrings, callback, true);
    }
    
    public void show(String title,String[] displayStrings,boolean[] isShow,Action1<Integer> callback){
    	 show(title,displayStrings, isShow,callback, true);
    }
    
    public void show(String title,String[] displayStrings, boolean[] isShow,final Action1<Integer> callback, boolean hasCancelButton) {
        if (getParent() == null) {
            WindowManager.LayoutParams wlp = new WindowManager.LayoutParams();
            wlp.type = WindowManager.LayoutParams.TYPE_APPLICATION;
            wlp.format = PixelFormat.TRANSPARENT;
            wlp.gravity = Gravity.LEFT | Gravity.TOP;
            wlp.width = LayoutParams.MATCH_PARENT;
            wlp.height = LayoutParams.MATCH_PARENT;
            windowManager.addView(this, wlp);
        }

        maskView.show();

        int mrg = (int) UnitConverter.applyDimension(getContext(), TypedValue.COMPLEX_UNIT_DIP, 10);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.setMargins(0, mrg, 0, mrg);

        actionSheetView.setVisibility(View.VISIBLE);
        actionSheetView.removeAllViews();
        
        if(null!=title&&!title.trim().equals("")){
        	titleTextView = new TextView(getContext());
        	titleTextView.setBackgroundColor(Color.TRANSPARENT);
        	titleTextView.setGravity(Gravity.CENTER);
        	titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        	titleTextView.setText(title);
        	titleTextView.setTextColor(Color.WHITE);
        	actionSheetView.addView(titleTextView,lp);
        }
        
        for (int i = 0, len = displayStrings.length; i < len; i++) {
            final int index = i;
            Button button = new Button(getContext());
            button.setBackgroundResource(R.drawable.actionsheet_red_btn);
            button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            button.setText(displayStrings[index]);
            button.setTextColor(Color.WHITE);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.invoke(index);
                }
            });
            if(isShow[i]) {
            	button.setVisibility(View.GONE);
            }
            actionSheetView.addView(button, lp);
        }

        if (hasCancelButton) {
            Button button = new Button(getContext());
            button.setBackgroundResource(R.drawable.actionsheet_black_btn);
            button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            button.setTextColor(Color.WHITE);
            button.setText("取消");
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    hide();
                }
            });
            actionSheetView.addView(button, lp);
        }

        actionSheetView.clearAnimation();
        TranslateAnimation an = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        an.setDuration(durationMillis);
        actionSheetView.startAnimation(an);
    }

    public void show(String title,String[] displayStrings, final Action1<Integer> callback, boolean hasCancelButton) {
        if (getParent() == null) {
            WindowManager.LayoutParams wlp = new WindowManager.LayoutParams();
            wlp.type = WindowManager.LayoutParams.TYPE_APPLICATION;
            wlp.format = PixelFormat.TRANSPARENT;
            wlp.gravity = Gravity.LEFT | Gravity.TOP;
            wlp.width = LayoutParams.MATCH_PARENT;
            wlp.height = LayoutParams.MATCH_PARENT;
            windowManager.addView(this, wlp);
        }

        maskView.show();

        int mrg = (int) UnitConverter.applyDimension(getContext(), TypedValue.COMPLEX_UNIT_DIP, 5);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.setMargins(0, mrg, 0, mrg);

        actionSheetView.setVisibility(View.VISIBLE);
        actionSheetView.removeAllViews();
        
        if(!title.trim().equals("")&&null!=title){
        	titleTextView = new TextView(getContext());
        	titleTextView.setBackgroundColor(Color.TRANSPARENT);
        	titleTextView.setGravity(Gravity.CENTER);
        	titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        	titleTextView.setText(title);
        	titleTextView.setTextColor(Color.WHITE);
        	actionSheetView.addView(titleTextView,lp);
        }
        
        for (int i = 0, len = displayStrings.length; i < len; i++) {
            final int index = i;
            Button button = new Button(getContext());
            button.setBackgroundResource(R.drawable.actionsheet_red_btn);
            button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            button.setText(displayStrings[index]);
            button.setTextColor(Color.WHITE);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.invoke(index);
                }
            });
            actionSheetView.addView(button, lp);
        }

        if (hasCancelButton) {
            Button button = new Button(getContext());
            button.setBackgroundResource(R.drawable.actionsheet_black_btn);
            button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            button.setTextColor(Color.WHITE);
            button.setText("取消");
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    hide();
                }
            });
            actionSheetView.addView(button, lp);
        }

        actionSheetView.clearAnimation();
        TranslateAnimation an = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        an.setDuration(durationMillis);
        actionSheetView.startAnimation(an);
    }
    
    public void hide() {
        maskView.hide();
        actionSheetView.clearAnimation();
        TranslateAnimation an = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
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
                actionSheetView.setVisibility(View.GONE);
                if (getParent() != null)
                    windowManager.removeView(ActionSheet.this);
            }
        });
        actionSheetView.startAnimation(an);
    }
}
