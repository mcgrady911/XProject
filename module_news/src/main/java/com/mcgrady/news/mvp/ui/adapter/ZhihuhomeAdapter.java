package com.mcgrady.news.mvp.ui.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mcgrady.common_core.di.component.AppComponent;
import com.mcgrady.common_core.http.imageloader.ImageConfigImpl;
import com.mcgrady.common_core.http.imageloader.ImageLoader;
import com.mcgrady.common_core.utils.Utils;
import com.mcgrady.common_res.interf.IViewHolderRelease;
import com.mcgrady.news.R;
import com.mcgrady.news.mvp.model.entity.DailyMultipleItem;
import com.mcgrady.news.mvp.model.entity.DailyStoriesBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * <p>类说明</p>
 *
 * @author: mcgrady
 * @date: 2019/1/15
 */

public class ZhihuhomeAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>
        implements IViewHolderRelease<BaseViewHolder> {

    public static final int TYPE_HEADER = 1;
    public static final int TYPE_DATE = 2;
    public static final int TYPE_ITEM = 3;

    private AppComponent appComponent;
    private ImageLoader imageLoader;

    public ZhihuhomeAdapter(Context context) {
        this(context, new ArrayList());
    }
    public ZhihuhomeAdapter(Context context, List data) {
        super(data);
        addItemType(TYPE_HEADER, R.layout.news_header_banner);
        addItemType(TYPE_DATE, R.layout.news_item_zhihu_date);
        addItemType(TYPE_ITEM, R.layout.news_item_zhihu_home_list);

        appComponent = Utils.obtainAppComponentFromContext(context);
        imageLoader = appComponent.imageLoader();
    }

    public void setData(DailyStoriesBean data) {
        List<MultiItemEntity> entities = new ArrayList<>();

        entities.add(new DailyMultipleItem(TYPE_DATE, data.getDate()));

        List<DailyStoriesBean.StoriesBean> stories = data.getStories();
        for (DailyStoriesBean.StoriesBean storiesBean : stories) {
            entities.add(new DailyMultipleItem(TYPE_ITEM, storiesBean));
        }

        setNewData(entities);
    }

    public void addData(DailyStoriesBean data) {
        List<MultiItemEntity> entities = new ArrayList<>();

        entities.add(new DailyMultipleItem(TYPE_DATE, data.getDate()));

        List<DailyStoriesBean.StoriesBean> stories = data.getStories();
        for (DailyStoriesBean.StoriesBean storiesBean : stories) {
            entities.add(new DailyMultipleItem(TYPE_ITEM, storiesBean));
        }

        addData(entities);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        int itemType = item.getItemType();
        switch (itemType) {
            case TYPE_DATE:
                helper.setText(R.id.news_tv_date, getDateTitle((String) ((DailyMultipleItem) item).getData()));
                break;
            case TYPE_ITEM:
                DailyStoriesBean.StoriesBean storiesBean = (DailyStoriesBean.StoriesBean) ((DailyMultipleItem) item).getData();

                helper.setText(R.id.list_item_title, storiesBean.getTitle());
                List<String> imageList = storiesBean.getImages();
                imageLoader.loadImage(mContext, ImageConfigImpl.builder()
                        .url(imageList == null || imageList.isEmpty() ? "" : imageList.get(0))
                        .imageView(helper.getView(R.id.list_item_image))
                        .build());
                helper.setGone(R.id.list_item_multipic, imageList == null || imageList.isEmpty() ?
                        false : storiesBean.getImages().size() > 1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRelease(BaseViewHolder helper) {
        imageLoader.clear(appComponent.application(), ImageConfigImpl.builder()
                .imageView(helper.getView(R.id.list_item_image))
                .build());
    }

    public String getDateTitle(String serviceTime) {
        if (TextUtils.isEmpty(serviceTime)) {
            return "";
        }

        Date date = TimeUtils.string2Date(serviceTime, new SimpleDateFormat("yyyyMMdd"));
        if (TimeUtils.isToday(date)) {
            return "今日热文";
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append(TimeUtils.date2String(date, new SimpleDateFormat("MM月dd日", Locale.getDefault())));
            builder.append(" ");
            builder.append(TimeUtils.getChineseWeek(date));
            return  builder.toString();
        }
    }
}
