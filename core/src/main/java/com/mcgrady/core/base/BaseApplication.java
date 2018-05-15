package com.mcgrady.core.base;

import android.app.Application;

import com.mcgrady.core.ActivitySwitchCallbacks;
import com.mcgrady.core.di.component.AppComponent;
import com.mcgrady.core.di.component.DaggerAppComponent;
import com.mcgrady.core.di.module.AppModule;
import com.mcgrady.core.di.module.DataModule;
import com.mcgrady.core.http.IHttpHelper;
import com.mcgrady.core.server.AppInitService;
import com.mcgrady.core.utils.ActivityStack;
import com.mcgrady.core.utils.AppContext;

/**
 * <p>app基类</p>
 *
 * @author: mcgrady
 * @date: 2018/5/9
 */
public class BaseApplication extends Application {

    protected static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AppContext.init(this);

        // 在子线程中完成初始化操作
        AppInitService.start(this);

        registerActivityLifecycleCallbacks(new ActivitySwitchCallbacks());
    }

    public void exitApp() {
        ActivityStack.getInstance().clearAllActivity();
    }

    public AppComponent getAppComponent() {
        return DaggerAppComponent.builder()
                .dataModule(new DataModule(getNetConfig()))
                .appModule(new AppModule(instance))
                .build();
    }

    public IHttpHelper.NetConfig getNetConfig() {
        return null;
    }
}
