package com.mcgrady.main.mvp.presenter;

import android.app.Application;

import com.mcgrady.common_core.di.scope.ActivityScope;
import com.mcgrady.common_core.http.imageloader.ImageLoader;
import com.mcgrady.common_core.intergration.handler.RxErrorHandler;
import com.mcgrady.common_core.intergration.manager.AppManager;
import com.mcgrady.common_core.mvp.BasePresenter;
import com.mcgrady.main.mvp.contract.EyepetizerLoginContract;

import javax.inject.Inject;



/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 01/03/2019 16:35
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class EyepetizerLoginPresenter extends BasePresenter<EyepetizerLoginContract.Model, EyepetizerLoginContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public EyepetizerLoginPresenter(EyepetizerLoginContract.Model model, EyepetizerLoginContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }
}