package com.qiao.actionsheet.activity;

import com.qiao.actionsheet.R;
import com.qiao.actionsheet.util.ActionSheet;
import com.qiao.actionsheet.util.Method.Action1;
import com.qiao.actionsheet.view.ActionSheetLayout;

import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;

public class MainActivity extends Activity {

	protected ActionSheetLayout actionSheetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		actionSheetLayout = (ActionSheetLayout) findViewById(R.id.action_sheet_layout);
    }
    
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
    	if(actionSheetLayout.isShow()){
    		actionSheetLayout.hide();
    	}else{
    		actionSheetLayout.show();
    	}
    	return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK){
    		if(actionSheetLayout.isShow()){
        		actionSheetLayout.hide();
        		return true;
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
    	return super.onKeyDown(keyCode, event);
    }
    
}
