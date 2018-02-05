package com.mcgrady.xproject.app;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.blankj.utilcode.util.Utils;
import com.mcgrady.xproject.component.InitializeService;
import com.mcgrady.xproject.di.component.AppComponent;
import com.mcgrady.xproject.di.component.DaggerAppComponent;
import com.mcgrady.xproject.di.module.AppModule;
import com.mcgrady.xproject.di.module.HttpModule;

/**
 * Created by mcgrady on 2017/8/9.
 */

public class App extends Application {
    private static App instance;
    public static AppComponent appComponent;

    public static int SCREEN_WIDTH = -1;
    public static int SCREEN_HEIGHT = -1;
    public static float DIMEN_RATE = -1.0F;
    public static int DIMEN_DPI = -1;

    public static synchronized App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // 初始化屏幕宽高
        getScreenSize();

        // 初始化Util工具类
        Utils.init(this);

        // 在子线程中完成其他初始化
        InitializeService.start(this);
    }

    protected void attchBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public void exitApp() {
        ActivityStack.getInstance().clearAllActivity();

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public void getScreenSize() {
        WindowManager manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = manager.getDefaultDisplay();
        display.getMetrics(dm);
        DIMEN_RATE = dm.density / 1.0F;
        DIMEN_DPI = dm.densityDpi;
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        if(SCREEN_WIDTH > SCREEN_HEIGHT) {
            int t = SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = t;
        }
    }

    public static AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(instance))
                    .httpModule(new HttpModule())
                    .build();
        }

        return appComponent;
    }
}