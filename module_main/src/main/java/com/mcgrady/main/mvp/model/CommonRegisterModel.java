package com.mcgrady.main.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.mcgrady.main.mvp.contract.CommonRegisterContract;
import com.mcgrady.xskeleton.di.scope.ActivityScope;
import com.mcgrady.xskeleton.integration.IRepositoryManager;
import com.mcgrady.xskeleton.mvp.BaseModel;

import javax.inject.Inject;

@ActivityScope
public class CommonRegisterModel extends BaseModel implements CommonRegisterContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public CommonRegisterModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }
}