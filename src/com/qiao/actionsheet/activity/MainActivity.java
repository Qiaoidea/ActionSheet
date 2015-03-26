package com.qiao.actionsheet.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.qiao.actionsheet.baoyz.ActionSheet.ItemClikListener;
import com.qiao.actionsheet.ios6.ActionSheet;
import com.qiao.actionsheet.ios6.ActionSheet.Action1;
import com.qiao.actionsheet.ActionSheetLayout;
import com.qiao.actionsheet.R;

public class MainActivity extends Activity implements OnClickListener{

	protected ActionSheetLayout actionSheetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		actionSheetLayout = (ActionSheetLayout) findViewById(R.id.action_sheet_layout);
		findViewById(R.id.fromtop).setOnClickListener(this);
		findViewById(R.id.frombottom).setOnClickListener(this);
		findViewById(R.id.fromios).setOnClickListener(this);
		findViewById(R.id.ios6).setOnClickListener(this);
		findViewById(R.id.ios7).setOnClickListener(this);
    }
    
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
    	menu();
    	return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK){
    		back();
    	}
    	return super.onKeyDown(keyCode, event);
    }
    
    public void menu(){
    	//直接调用hide()和()来控制显示
    	if(actionSheetLayout.isShow()){
    		actionSheetLayout.hide();
    	}else{
    		actionSheetLayout.show();
    	}
    }
    
    public void back(){
		if(actionSheetLayout.isShow()){
    		actionSheetLayout.hide();
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
			actionSheetLayout.hide();
		}else if(v.getId() == R.id.ios7){
			showActionSheet(R.style.ActionSheetStyleIOS7);
			actionSheetLayout.hide();
		}else if(v.getId() == R.id.fromtop){
			menu();
		}else if(v.getId() == R.id.frombottom){
			back();
		}else if( v.getId() == R.id.fromios){
			showActionSheet(R.style.ActionSheetStyleIOS7);
		}
	}
}
