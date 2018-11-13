# SlideBackHelper
这是一个android的开源库，它可以帮助您实现类似微信左滑返回上一个activity的效果，您仅仅只需短短一行代码，不需改动其他代码，非常方便

![示范](https://github.com/OUYANGV5/SlideBackHelper/blob/master/GIF.gif)

## 使用
 * 依赖      ![链接](https://www.jitpack.io/v/OUYANGV5/SlideBackHelper.svg)
    ```
      dependencies {
	        implementation 'com.github.OUYANGV5:SlideBackHelper:0.1'
	    }
    ```
 * 在application中添加ActivityLifeManager.registerListener(this)注册监听，否则没有联动效果
 
  ```Java
  public class MyApplicaiotn extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActivityLifeManager.registerListener(this);
    }
  }
  ```
  
 * 在activity中添加SlideBackHelper.init(this)，让这个activity变成可滑动的
  ```Java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SlideBackHelper.init(this);
        
    }
  ```
  * 如果您在使用中发现了什么，请告诉我，谢谢
