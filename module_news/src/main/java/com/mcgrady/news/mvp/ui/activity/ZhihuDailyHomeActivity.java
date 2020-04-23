package com.mcgrady.news.mvp.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.hjq.toast.ToastUtils;
import com.mcgrady.common_core.RouterHub;
import com.mcgrady.common_res.utils.ViewUtils;
import com.mcgrady.news.R;
import com.mcgrady.news.R2;
import com.mcgrady.news.mvp.contract.ZhihuDailyHomeContract;
import com.mcgrady.news.mvp.model.ZhihuModel;
import com.mcgrady.news.mvp.model.entity.ZhihuDailyMultipleItem;
import com.mcgrady.news.mvp.model.entity.ZhihuDailyStoriesBean;
import com.mcgrady.news.mvp.presenter.ZhihuDailyHomePresenter;
import com.mcgrady.news.mvp.ui.adapter.ZhihuDailyHomeAdapter;
import com.mcgrady.xskeleton.base.AppComponent;
import com.mcgrady.xskeleton.base.BaseActivity;
import com.mcgrady.xtitlebar.TitleBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * 知乎日报列表
 */
@Route(path = RouterHub.ZHIHU_DAILY_HOME)
public class ZhihuDailyHomeActivity extends BaseActivity<ZhihuDailyHomePresenter> implements ZhihuDailyHomeContract.View,
        OnRefreshListener, OnLoadMoreListener, OnBannerListener {

    @BindView(R2.id.news_titlebar_zhihu_home)
    TitleBar titleBar;
    @BindView(R2.id.news_refresh_zhihu)
    SmartRefreshLayout refreshLayout;
    @BindView(R2.id.news_recycler_zhihu)
    RecyclerView recyclerView;

    private Banner banner;
    private ZhihuDailyHomeAdapter adapter;
    private LinearLayoutManager linearManager;
    private int lastTitlePostion = -1;

//    private com.mcgrady.xskeleton.imageloader.ImageLoader mImageLoader;

    @Override
    protected ZhihuDailyHomePresenter createPresenter() {

        ZhihuModel model = new ZhihuModel();
        return new ZhihuDailyHomePresenter(model, this, AppComponent.obtainClientModule(this).getRxErrorHandler());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.news_activity_zhihu_daily_home;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        linearManager = new LinearLayoutManager(this);
        ViewUtils.configRecyclerView(recyclerView, linearManager);

        banner = (Banner) LayoutInflater.from(this).inflate(R.layout.news_header_banner, null);
        banner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenHeight() / 3));
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
//        mImageLoader = Utils.obtainAppComponentFromContext(this).imageLoader();

        adapter = new ZhihuDailyHomeAdapter(this);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            ZhihuDailyMultipleItem itemEntity = (ZhihuDailyMultipleItem) adapter.getItem(position);
            switch (itemEntity.getItemType()) {
                case ZhihuDailyHomeAdapter.TYPE_ITEM:
                    ZhihuDailyStoriesBean.StoriesBean storiesBean = (ZhihuDailyStoriesBean.StoriesBean) itemEntity.getData();
                    ARouter.getInstance().build(RouterHub.ZHIHU_DAILY_DETAIL)
                            .withInt("daily_id", storiesBean.getId())
                            .withString("daily_title", storiesBean.getTitle())
                            .withString("daily_img_url", storiesBean.getImages() == null ?
                                    "" :storiesBean.getImages().get(0))
                            .navigation(ZhihuDailyHomeActivity.this);
                    break;
                default:
                    break;
            }
        });

        adapter.bindToRecyclerView(recyclerView);
        adapter.setHeaderView(banner);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                changeToolbarTitle(dy);
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.autoRefresh();
    }

    private void changeToolbarTitle(int dy) {
        int position = linearManager.findFirstVisibleItemPosition();
        if (lastTitlePostion == position) {
            return;
        }

        int itemType = adapter.getItemViewType(position);
        if (BaseQuickAdapter.HEADER_VIEW == itemType) {
            titleBar.setTitle("首页");
        } else if (dy > 0 && ZhihuDailyHomeAdapter.TYPE_DATE == itemType) {
            // postion - 1 是因为adapter有headerview
            ZhihuDailyMultipleItem<String> dateItem = (ZhihuDailyMultipleItem<String>) adapter.getItem(position - 1);
            titleBar.setTitle(adapter.getDateTitle(dateItem.getData()));
        } else if (dy < 0) {
            List<MultiItemEntity> subList = new ArrayList<>();
            subList.addAll(adapter.getData().subList(0, position));
            Collections.reverse(subList);
            for (MultiItemEntity itemEntity : subList) {
                if (ZhihuDailyHomeAdapter.TYPE_DATE == itemEntity.getItemType()) {
                    titleBar.setTitle(adapter.getDateTitle((String) ((ZhihuDailyMultipleItem) itemEntity).getData()));
                    break;
                }
            }
        }
        lastTitlePostion = position;
    }



    @Override
    protected void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        presenter.requestDailyList();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        presenter.requestBeforeDailyList();
    }

    @Override
    protected void onDestroy() {
        ViewUtils.releaseAllHolder(recyclerView);
        super.onDestroy();
    }

//    @Override
//    public void finishLoadMore(boolean success) {
//        refreshLayout.finishLoadMore(success);
//    }

    @Override
    public void notifyDataSetChanged(ZhihuDailyStoriesBean data) {
        banner.setImages(data.getTop_stories())
            .setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object bean, ImageView imageView) {
//                    mImageLoader.loadImage(ZhihuDailyHomeActivity.this,
//                            ImageConfigImpl.builder()
//                                    .url(((ZhihuDailyStoriesBean.TopStoriesBean) bean).getImage())
//                                    .imageView(imageView)
//                                    .build());
                }
            })
            .setOnBannerListener(ZhihuDailyHomeActivity.this)
            .start();
        adapter.setData(data);
    }

    @Override
    public void loadMoreData(ZhihuDailyStoriesBean data) {
        adapter.addData(data);
    }

    @Override
    public void finishRefresh() {
        refreshLayout.finishRefresh();
    }

    @Override
    public void finishLoadMore(boolean success) {
        refreshLayout.finishLoadMore(success);
    }

    @Override
    public void OnBannerClick(int position) {
        ToastUtils.show("你点击了: " + position);
    }
}
