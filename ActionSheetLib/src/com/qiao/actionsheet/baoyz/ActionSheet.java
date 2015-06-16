package com.qiao.actionsheet.baoyz;

import com.qiao.actionsheet.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActionSheet extends FrameLayout implements OnClickListener{
    protected WindowManager windowManager;
    protected View mBackgroundView;
	protected LinearLayout container;
	protected Attributes mAttrs;
	protected Builder bundle;
	
	protected boolean isDismissed = true;
	protected boolean isCancel = true;
	
	public static final int BG_VIEW_ID = 100;
	public static final int CANCEL_BUTTON_ID = 101;
	public static final int ITEM_ID = 102;
	
	public static final int ActionSheetThemeIOS6 = R.style.ActionSheetStyleIOS6;
	public static final int ActionSheetThemeIOS7 = R.style.ActionSheetStyleIOS7;
	
    protected final static long durationMillis = 200;

	public ActionSheet(Context context, Builder bundle) {
		super(context);
		this.bundle = bundle;
		initalize();
	}

	protected void initalize() {
		tryHideSoftInput();
		mAttrs = initAttrs();
		initViews();
	}

	/**
	 * 如果输入法键盘没有隐藏，则隐藏软键盘
	 */
	protected void tryHideSoftInput(){
		InputMethodManager imm = (InputMethodManager) getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
		}
	}
	
	
	/**
	 * 初始化背景view 和 底部items
	 */
	protected void initViews(){
		windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		
		mBackgroundView = new View(getContext());
		mBackgroundView.setBackgroundColor(0x88000000);
		mBackgroundView.setId(BG_VIEW_ID);
		mBackgroundView.setOnClickListener(this);
		FrameLayout.LayoutParams bgLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		this.addView(mBackgroundView);
		
		container = new LinearLayout(getContext());
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 
				FrameLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.BOTTOM;
		container.setLayoutParams(params);
		container.setOrientation(LinearLayout.VERTICAL);
		createItems();
		this.addView(container);

        setFocusable(true);
        setFocusableInTouchMode(true);
	}
	
	/**
	 * 初始化底部items
	 */
	protected void createItems(){ 
		//创建title
		if(bundle.mTitle !=null){
			TextView title = new TextView(getContext());
			title.getPaint().setFakeBoldText(true);
			title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttrs.textSize);
			title.setText(bundle.mTitle);
			title.setTextColor(mAttrs.titleColor);
			title.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams params = createItemLayoutParams();
			int margin = mAttrs.itemSpacing > 0 ? mAttrs.itemSpacing:mAttrs.margin;
			params.setMargins(margin, 0, margin, margin);			
			container.addView(title, params);
		}
		
		//创建items
		String[] titles = bundle.mItemsText;
		if (titles != null) {
			for (int i = 0; i < titles.length; i++) {
				Button bt = new Button(getContext());
				bt.setId(ITEM_ID + i);
				bt.setOnClickListener(this);
				bt.setBackgroundDrawable(getItemBg(titles, i));
				bt.setText(titles[i]);
				bt.setTextColor(mAttrs.itemTextColor);
				bt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttrs.textSize);
				if (i > 0) {
					LinearLayout.LayoutParams params = createItemLayoutParams();
					params.topMargin = mAttrs.itemSpacing;
					container.addView(bt, params);
				} else {
					container.addView(bt);
				}
			}
		}
		//创建cancelbutton
		if(bundle.mCancelButtonText != null){
			Button bt = new Button(getContext());
			bt.getPaint().setFakeBoldText(true);
			bt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttrs.textSize);
			bt.setId(CANCEL_BUTTON_ID);
			bt.setBackgroundDrawable(mAttrs.cancelButtonBackground);
			bt.setText(bundle.mCancelButtonText);
			bt.setTextColor(mAttrs.cancelButtonTextColor);
			bt.setOnClickListener(this);
			LinearLayout.LayoutParams params = createItemLayoutParams();
			params.topMargin = mAttrs.cancelButtonMarginTop;
			container.addView(bt, params);
		}

		container.setBackgroundDrawable(mAttrs.background);
		container.setPadding(mAttrs.padding, mAttrs.padding, mAttrs.padding,
				mAttrs.padding);
	}

	private Drawable getItemBg(String[] titles, int i) {
		if (titles.length == 1) {
			return mAttrs.singleItemBackground;
		}else if(titles.length>1){
			if (i == 0) {
				return mAttrs.topItemBackground;
			}else if (i == (titles.length - 1)) {
				return mAttrs.bottomItemBackground;
			}
			return mAttrs.getMiddleItemBackground();
		}
		return null;
	}

	private LinearLayout.LayoutParams createItemLayoutParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		return params;
	}

	public void show(){
		if(isDismissed){
			isDismissed = false;
			
	        if (getParent() == null) {
	            WindowManager.LayoutParams wlp = new WindowManager.LayoutParams();
	            wlp.type = WindowManager.LayoutParams.TYPE_APPLICATION;
	            wlp.format = PixelFormat.TRANSPARENT;
	            wlp.gravity = Gravity.LEFT | Gravity.TOP;
	            wlp.width = LayoutParams.MATCH_PARENT;
	            wlp.height = LayoutParams.MATCH_PARENT;
	            windowManager.addView(this, wlp);
	        }
	        startSlideInAnim();
		}
	}

	public void dismiss(){
		if(!isDismissed){
			startSlideOutAnim();
			if (isCancel && bundle.cancelListener != null) {
				bundle.cancelListener.onCancelClick(this);
			}
		}
	}

	private void startSlideInAnim() {
		container.clearAnimation();
        mBackgroundView.clearAnimation();
        
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        translateAnimation.setDuration(durationMillis);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(durationMillis);
        

        container.startAnimation(translateAnimation);
        mBackgroundView.startAnimation(alphaAnimation);
	}
	
	private void startSlideOutAnim() {
		container.clearAnimation();
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
        translateAnimation.setDuration(durationMillis);

        mBackgroundView.clearAnimation();
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(durationMillis);
        alphaAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            	ActionSheet.this.setVisibility(View.GONE);
                if (getParent() != null)
                    windowManager.removeView(ActionSheet.this);
            }
        });
        
        container.startAnimation(translateAnimation);
        mBackgroundView.startAnimation(alphaAnimation);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode && KeyEvent.ACTION_DOWN == event.getAction()) {
			dismiss();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == BG_VIEW_ID && !bundle.mCancelableOnTouchOutside) {
			return;
		}
		if (v.getId() != CANCEL_BUTTON_ID && v.getId() != BG_VIEW_ID) {
			if (bundle.itemClikListener != null) {
				bundle.itemClikListener.onitemClick(this, v.getId() - CANCEL_BUTTON_ID
						- 1);
			}
			isCancel = false;
		}
		dismiss();
	}

	protected Attributes initAttrs(){
		Attributes attrs = new Attributes(getContext());
		TypedArray a = getContext().getTheme().obtainStyledAttributes(null,
				R.styleable.ActionSheet, R.attr.actionSheetStyle, 0);
		Drawable background = a
				.getDrawable(R.styleable.ActionSheet_actionSheetBackground);
		if (background != null) {
			attrs.background = background;
		}
		Drawable cancelButtonBackground = a
				.getDrawable(R.styleable.ActionSheet_cancelButtonBackground);
		if (cancelButtonBackground != null) {
			attrs.cancelButtonBackground = cancelButtonBackground;
		}
		Drawable itemTopBackground = a
				.getDrawable(R.styleable.ActionSheet_topItemBackground);
		if (itemTopBackground != null) {
			attrs.topItemBackground = itemTopBackground;
		}
		Drawable itemMiddleBackground = a
				.getDrawable(R.styleable.ActionSheet_middleItemBackground);
		if (itemMiddleBackground != null) {
			attrs.middleItemBackground = itemMiddleBackground;
		}
		Drawable itemBottomBackground = a
				.getDrawable(R.styleable.ActionSheet_bottomItemBackground);
		if (itemBottomBackground != null) {
			attrs.bottomItemBackground = itemBottomBackground;
		}
		Drawable itemSingleBackground = a
				.getDrawable(R.styleable.ActionSheet_singleItemBackground);
		if (itemSingleBackground != null) {
			attrs.singleItemBackground = itemSingleBackground;
		}
		attrs.titleColor = a.getColor(
				R.styleable.ActionSheet_titleColor,
				attrs.titleColor);
		attrs.cancelButtonTextColor = a.getColor(
				R.styleable.ActionSheet_cancelButtonTextColor,
				attrs.cancelButtonTextColor);
		attrs.itemTextColor = a.getColor(
				R.styleable.ActionSheet_itemTextColor,
				attrs.itemTextColor);
		attrs.padding = (int) a.getDimension(
				R.styleable.ActionSheet_actionSheetPadding, attrs.padding);
		attrs.itemSpacing = (int) a.getDimension(
				R.styleable.ActionSheet_itemSpacing,
				attrs.itemSpacing);
		attrs.cancelButtonMarginTop = (int) a.getDimension(
				R.styleable.ActionSheet_cancelButtonMarginTop,
				attrs.cancelButtonMarginTop);
		attrs.textSize = a.getDimensionPixelSize(R.styleable.ActionSheet_actionSheetTextSize, (int) attrs.textSize);

		a.recycle();
		return attrs;
	}

	/**
	 * actionsheet各种属性样式
	 */
	private static class Attributes {
		Context mContext;
		Drawable background;
		Drawable cancelButtonBackground;
		Drawable topItemBackground;
		Drawable middleItemBackground;
		Drawable bottomItemBackground;
		Drawable singleItemBackground;
		int cancelButtonTextColor;
		int itemTextColor;
		int titleColor;
		int padding;
		int itemSpacing;
		int margin;
		int cancelButtonMarginTop;
		float textSize;

		public Attributes(Context context) {
			mContext = context;
			this.background = new ColorDrawable(Color.TRANSPARENT);
			this.cancelButtonBackground = new ColorDrawable(Color.BLACK);
			ColorDrawable gray = new ColorDrawable(Color.GRAY);
			this.topItemBackground = gray;
			this.middleItemBackground = gray;
			this.bottomItemBackground = gray;
			this.singleItemBackground = gray;
			this.cancelButtonTextColor = Color.WHITE;
			this.titleColor = Color.WHITE;
			this.itemTextColor = Color.BLACK;
			this.padding = dp2px(20);
			this.itemSpacing = dp2px(2);
			this.margin = dp2px(5);
			this.cancelButtonMarginTop = dp2px(10);
			this.textSize = dp2px(16);
		}

		/**
		 * dp转像素类
		 * @param dp
		 * @return
		 */
		private int dp2px(int dp){
			return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
					dp, mContext.getResources().getDisplayMetrics());
		}

		/**
		 * 获取items按键状态对应背景
		 * @return
		 */
		public Drawable getMiddleItemBackground() {
			if (middleItemBackground instanceof StateListDrawable) {
				TypedArray a = mContext.getTheme().obtainStyledAttributes(null,
						R.styleable.ActionSheet, R.attr.actionSheetStyle, 0);
				middleItemBackground = a
						.getDrawable(R.styleable.ActionSheet_middleItemBackground);
				a.recycle();
			}
			return middleItemBackground;
		}
	}
	
	public static Builder init(Context context) {
		return new Builder(context);
	}

	public static Builder createBuilder(Context context) {
		return new Builder(context);
	}
	
	public static class Builder {

		private Context mContext;
		private String mTitle;
		private String mCancelButtonText;
		private String[] mItemsText;
		private String mTag = "ActionSheet";
		private boolean mCancelableOnTouchOutside = true;
		private ItemClikListener itemClikListener;
		private CancelListener cancelListener;
		private boolean isThemed = false;

		public Builder(Context context) {
			mContext = context;
		}
		
		public Builder setTitle(String title) {
			mTitle = title;
			return this;
		}

		public Builder setCancelText(String title) {
			mCancelButtonText = title;
			return this;
		}
		
		public Builder setCancelText(String title,CancelListener cancelListener) {
			mCancelButtonText = title;
			this.cancelListener = cancelListener;
			return this;
		}

		public Builder setItemTexts(String... titles) {
			mItemsText = titles;
			return this;
		}
		
		public Builder setTheme(int resid){
			isThemed = true;
			mContext.setTheme(resid);
			return this;
		}

		public Builder setTag(String tag) {
			mTag = tag;
			return this;
		}
		
		public Builder setItemClickListener(ItemClikListener listener) {
			this.itemClikListener = listener;
			return this;
		}

		public Builder setCancelableOnTouchOutside(boolean cancelable) {
			mCancelableOnTouchOutside = cancelable;
			return this;
		}

		public ActionSheet show() {
			if(!isThemed)
				setTheme(R.style.ActionSheetStyleIOS7);
			ActionSheet actionSheet = new ActionSheet(mContext, Builder.this);
			actionSheet.show();
			return actionSheet;
		}
	}
	
	public static interface ItemClikListener{
		void onitemClick(ActionSheet actionSheet, int index);
	}
	
	public static interface CancelListener{
		void onCancelClick(ActionSheet actionSheet);
	}
}
