package com.qiao.actionsheet.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.qiao.actionsheet.R;
import com.qiao.actionsheet.baoyz.ActionSheet.ItemClikListener;
import com.qiao.actionsheet.ios6.ActionSheet;
import com.qiao.actionsheet.ios6.ActionSheet.Action1;
import com.qiao.actionsheet.linearlayout.ActionSheetLayout;
import com.qiao.actionsheet.linearlayout.ActionSheetLayout.OnCancelListener;

public class MainActivity extends Activity implements OnClickListener{

	protected ActionSheetLayout actionSheetLayout;
	private View moreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAnimation();
        setContentView(R.layout.activity_main);
        moreView = findViewById(R.id.btn_more);
		actionSheetLayout = (ActionSheetLayout) findViewById(R.id.action_sheet_layout);
		moreView.setOnClickListener(this);
		findViewById(R.id.fromtop).setOnClickListener(this);
		findViewById(R.id.frombottom).setOnClickListener(this);
		findViewById(R.id.fromios).setOnClickListener(this);
		findViewById(R.id.ios6).setOnClickListener(this);
		findViewById(R.id.ios7).setOnClickListener(this);
		actionSheetLayout.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel() {
	    		moreView.startAnimation(hideAnim);
			}
		});
    }
    
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
		showActionSheetLayoutAnim();
    	return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK){
    		back();
    	}
    	return super.onKeyDown(keyCode, event);
    }
    
    public void back(){
		if(actionSheetLayout.isShow()){
			showActionSheetLayoutAnim();
    		return ;
    	}
		final ActionSheet actionSheet = new ActionSheet(MainActivity.this);
		actionSheet.show("确定要退出么？",new String[]{"退出" },new Action1<Integer>(){
			@Override
			public void invoke(Integer index) {
				actionSheet.hide();
				if(index==0){
					MainActivity.this.finish();
				}
			}
		});
    }

    /**
     * com.qiao.actionsheet.baoyz.ActionSheet
     * 下午8:06:17
     * @param resid
     */
	public void showActionSheet(int resid) {
		com.qiao.actionsheet.baoyz.ActionSheet.init(this)
				.setTitle("This is test title ,do you want do something？")
				.setTheme(resid)
				.setItemTexts("Item1", "Item2", "Item3", "Item4")
				.setItemClickListener(new ItemClikListener() {
					@Override
					public void onitemClick(
							com.qiao.actionsheet.baoyz.ActionSheet actionSheet,
							int index) {
						Toast.makeText(getApplicationContext(), "click item index = " + index,0).show();
					}
				})
				.setCancelText("Cancel")
//				.setCancelText("Cancel",new CancelListener() {
//					@Override
//					public void onCancelClick(ActionSheet actionSheet) {
//						Toast.makeText(getApplicationContext(), "dismissed ", 0).show();
//					}
//				})
				.show();
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.ios6){
			showActionSheet(R.style.ActionSheetStyleIOS6);
			showActionSheetLayoutAnim();
		}else if(v.getId() == R.id.ios7){
			showActionSheet(R.style.ActionSheetStyleIOS7);
			showActionSheetLayoutAnim();
		}else if(v.getId() == R.id.fromtop){
			showActionSheetLayoutAnim();
		}else if(v.getId() == R.id.frombottom){
			back();
		}else if( v.getId() == R.id.fromios){
			showActionSheet(R.style.ActionSheetStyleIOS7);
		}else if( v.getId() == R.id.btn_more){
			showActionSheetLayoutAnim();
		}
	}
	
	public void showActionSheetLayoutAnim(){
		if(actionSheetLayout.isShow()){
    		actionSheetLayout.hide();
    		moreView.startAnimation(hideAnim);
    	}else{
    		actionSheetLayout.show();
    		moreView.startAnimation(showAnim);
    	}
	}
	

	private Animation showAnim;
	private Animation hideAnim;
	private void initAnimation() {  
		showAnim = new RotateAnimation(0f, 45f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		hideAnim = new RotateAnimation(45f, 0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		showAnim.setDuration(500);
		showAnim.setFillAfter(true);
		hideAnim.setDuration(500);
		hideAnim.setFillAfter(true);
	}
}
