package com.qiao.actionsheet.view;


import com.qiao.actionsheet.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

public class ActionSheetLayout extends FrameLayout {

	protected long durationMillis = 200;
	protected MaskView maskView;
	protected View actionSheetContainer;
	protected boolean isShow = false;

	public ActionSheetLayout(Context context) {
		super(context);
		initialize();
	}

	public ActionSheetLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public ActionSheetLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	protected void initialize() {
		maskView = new MaskView(getContext(), this);
		maskView.setDurationMillis(durationMillis);
		maskView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isShow){
					hide();
				}
			}
		});
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		actionSheetContainer = findViewById(R.id.action_sheet_container);
		actionSheetContainer.setVisibility(View.INVISIBLE);
	}
	
	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	public void show() {
		isShow = !isShow;
		setVisibility(View.VISIBLE);
		actionSheetContainer.clearAnimation();
		
		TranslateAnimation an = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0);
		an.setFillAfter(true);
		an.setDuration(durationMillis);
		actionSheetContainer.startAnimation(an);
		maskView.show();
	}

	public void hide() {
		isShow = !isShow;
		setVisibility(View.VISIBLE);
		actionSheetContainer.clearAnimation();
		
		TranslateAnimation an = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1);
		an.setFillAfter(true);
		an.setDuration(durationMillis);
		an.setAnimationListener(new EmptyAnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				super.onAnimationEnd(animation);
				setVisibility(View.INVISIBLE);
			}
		});
		actionSheetContainer.startAnimation(an);
		maskView.hide();
	}
}