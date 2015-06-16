# ActionSheet
参照ios中的 actionsheet样式做的Android ui界面。详解参见博客[仿IOS控件之ActionSheet](http://blog.csdn.net/qiaoidea/article/details/46417747)

<img src="http://img.blog.csdn.net/20150522203925438"  width="400" alt="效果图"/>

### 包含两种界面，四种样式：

1. 顶部下拉layout菜单 和 底部弹出式ActionSheet菜单
<p>
   <img src="http://upload-images.jianshu.io/upload_images/125949-db227cd950489727.jpg"  width="350" alt="Screenshot"/>
   &nbsp;&nbsp;
   <img src="http://upload-images.jianshu.io/upload_images/125949-8310bcf4d6e63e25.jpg" width="350" alt="Screenshot"/>
</p>


2. 后来,在此基础上，整合了[baoyz](https://github.com/baoyongzhang/ActionSheetForAndroid)的AcitonSheet，添加了带title通用性更强但是耦合性稍差的新样式菜单

有两种样式，分别为IOS6和IOS7风格
<p>
   <img src="http://upload-images.jianshu.io/upload_images/125949-e9e32abc216b6821.png" width="350" alt="Screenshot"/>
   &nbsp;&nbsp;
   <img src="http://upload-images.jianshu.io/upload_images/125949-d67f1ec0ad8527dd.png" width="350" alt="Screenshot"/>
</p>

# 如何使用
项目本身体积不大，可以引用库或者直接拷贝至自己的项目中去，使用方法很简单

#### 1. 第一种顶部弹出菜单ActionSheetLayout。 
    ActionSheetLayout就当做普通的linearlayout来使用，然后调用
    布局中xml

    <com.qiao.actionsheet.view.ActionSheetLayout
                android:id="@+id/action_sheet_layout"
                android:layout_width="match_parent"
                android:layout_height="match_parent" >
                <!-- 当做linearlayout使用-->
    </com.qiao.actionsheet.view.ActionSheetLayout>

  java代码
         
    	actionSheetLayout = (ActionSheetLayout) findViewById(R.id.action_sheet_layout);

    	//直接调用hide()和()来控制显示隐藏
    	if(actionSheetLayout.isShow()){
    		actionSheetLayout.hide();
    	}else{
    		actionSheetLayout.show();
    	}

#### 2. 底部弹出菜单ActionSheet  
使用很简单：

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

#### 3. 使用baoyz兄的Actionsheet整改后的  
具体使用见代码demo
```
		baoyz.qiao.actionsheet.ActionSheet.init(this)
				.setTitle("This is test title ,do you want do something？")
				.setTheme(resid)
				.setItemTexts("Item1", "Item2", "Item3", "Item4")
				.setItemClickListener(new ItemClikListener() {
					@Override
					public void onitemClick(
							baoyz.qiao.actionsheet.ActionSheet actionSheet,
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
```

其中,
+ **setTheme()** 参数可选 R.style.ActionSheetStyleIOS6 或 R.style.ActionSheetStyleIOS6 。不设置则默认为 R.style.ActionSheetStyleIOS7。注：
+ 不设置 **setTitle** 则为无title样式
+  **setCanTouchOutside(false)** 点击选项外无响应，默认为true，点击隐藏
+ 不设置 **setCancelText()** 则不显示取消按钮。其参数有两个，第一个string为显示内容,第二个listener为对应事件
+ 最后，别忘记 **show()** 让其显示。
