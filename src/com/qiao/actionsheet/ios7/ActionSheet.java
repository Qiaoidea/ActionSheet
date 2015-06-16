package com.qiao.actionsheet.ios7;

import android.content.Context;
import android.content.res.Resources;
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

import com.qiao.actionsheet.R;
import com.qiao.actionsheet.linearlayout.MaskView;
import com.qiao.actionsheet.linearlayout.MaskView.MaskListener;

import java.util.ArrayList;
import java.util.List;

public class ActionSheet extends RelativeLayout {
	protected final static long durationMillis = 200;
	protected WindowManager windowManager;
	protected GestureDetector gestureDetector;
	protected MaskView maskView;
	protected LinearLayout actionSheetView;
	protected Button cancelButton;
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
		actionSheetView.setVisibility(View.GONE);
		LayoutParams rlp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		rlp.leftMargin = rlp.rightMargin = (int)applyDimension(getContext(), TypedValue.COMPLEX_UNIT_DIP, 8);
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

    public void show(String title,String[] displayStrings, Action1<Integer> callback) {
        show(title,displayStrings, null, callback, null, true);
    }

	public void show(String[] displayStrings, Action1<Integer> callback) {
		show(null,displayStrings, null, callback, null, true);
	}

	public void show(String title,String[] displayStrings, final Action1<Integer> callback, boolean hasCancelButton) {
		show(title,displayStrings, null, callback, null, hasCancelButton);
	}

	public void show(String[] displayStrings, final Action1<Integer> callback, boolean hasCancelButton) {
		show(null,displayStrings, null, callback, null, hasCancelButton);
	}

	public void show(String title,String[] displayStrings, boolean[] hide, final Action1<Integer> callback, final Func<Boolean> onCancelAction, boolean hasCancelButton) {
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

		actionSheetView.setVisibility(View.VISIBLE);
		actionSheetView.removeAllViews();


        if(null!=title&&!title.trim().equals("")){
            titleTextView = new TextView(getContext());
            titleTextView.setBackgroundColor(Color.TRANSPARENT);
            titleTextView.setGravity(Gravity.CENTER);
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            titleTextView.setText(title);
            titleTextView.setTextColor(Color.WHITE);

            int mrg = (int) applyDimension(getContext(), TypedValue.COMPLEX_UNIT_DIP, 5);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            lp.setMargins(0, mrg, 0, mrg);
            actionSheetView.addView(titleTextView,lp);
        }

		List<Button> buttons = new ArrayList<Button>();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.topMargin = 1;
		for (int i = 0, len = displayStrings.length; i < len; i++) {
			final int index = i;
			Button button = new Button(getContext());
			button.setBackgroundResource(R.drawable.actionsheet_button1);
			button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
			button.setText(displayStrings[index]);
			button.setTextColor(0xff007aff);
			button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					callback.invoke(index);
				}
			});
			if (hide != null && i < hide.length && hide[i]) {
				button.setVisibility(View.GONE);
			}
			actionSheetView.addView(button, lp);
			buttons.add(button);
		}

		Util.setItemStyle(buttons.toArray(new View[] {}), new int[] {
				R.drawable.actionsheet_button_single, R.drawable.actionsheet_button_top, R.drawable.actionsheet_button_center, R.drawable.actionsheet_button_bottom
		});

		int marginTop = (int)applyDimension(getContext(), TypedValue.COMPLEX_UNIT_DIP, 13);
		lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, marginTop, 0, marginTop);

		if (hasCancelButton) {
			Button button = new Button(getContext());
			button.setBackgroundResource(R.drawable.actionsheet_button_single);
			button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
			button.setTextColor(0xff007aff);
			button.setText("取消");
			button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (onCancelAction != null && onCancelAction.invoke())
						return;
					hide();
				}
			});
			cancelButton = button;
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

	public Button getCancelButton() {
		return cancelButton;
	}
    
    public static float applyDimension(Context context, int unit, float size) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
    }
    
    public interface Action1<T1> {
        void invoke(T1 p);
    }
    
    public interface Func<Tresult> {
        Tresult invoke();
    }
}
