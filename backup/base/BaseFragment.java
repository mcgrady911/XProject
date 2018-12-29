package com.mcgrady.core.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.mcgrady.core.di.module.FragmentModule;
import com.trello.rxlifecycle2.LifecycleTransformer;

import javax.inject.Inject;

/**
 * <p>MVP frgament基类</p>
 * @author: mcgrady
 * @date: 2018/5/9
 */
public abstract class BaseFragment<T extends IBasePresenter> extends BaseLazyFragment implements IBaseView, ISupportDagger {

    /**
     * 如果当前页面逻辑简单, Presenter可以为null
     */
    @Inject
    @Nullable
    protected T mPresenter;

    protected FragmentModule getFragmentModule() {
        return new FragmentModule(this);
    }

    @Override
    public <E> LifecycleTransformer<E> bindLifecycle() {
        return this.<E>bindToLifecycle();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initInject(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public Context getAContext() {
        return _mActivity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
