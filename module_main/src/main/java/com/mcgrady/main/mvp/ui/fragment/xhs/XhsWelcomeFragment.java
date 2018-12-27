package com.mcgrady.main.mvp.ui.fragment.xhs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.mcgrady.main.R;
import com.mcgrady.main.widget.ChildViewPager;
import com.mcgrady.main.widget.WelcomeIndicator;

import butterknife.BindView;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/21/2018 14:43
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class XhsWelcomeFragment extends BaseFragment {

    @BindView(R.id.main_vp_imageanim)
    ChildViewPager vpImageAnim;
    @BindView(R.id.main_vp_textanim)
    ChildViewPager vpTextAnim;
    @BindView(R.id.main_view_indicator)
    WelcomeIndicator viewIndicator;
    @BindView(R.id.main_tv_skip)
    TextView tvSkip;
    @BindView(R.id.main_rl_indicator)
    RelativeLayout rlIndicator;
    @BindView(R.id.main_rl_parent)
    RelativeLayout rlParent;

    private int oldPosition = -1;
    private boolean isMoveParent = false;
    private boolean fristPageSuperLock = false;
//    XhsImageFragmentStatePagerAdapter imageFragmentStatePagerAdapter;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment_xhs_welcome, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }

    public TextView getTvSkip() {
        return tvSkip;
    }

    public ChildViewPager getVpImage() {
        return vpImageAnim;
    }

    public ChildViewPager getVpText() {
        return vpTextAnim;
    }

    public int getOldPosition() {
        return oldPosition;
    }

    public boolean isMoveParent() {
        return isMoveParent;
    }

    public boolean isFristPageSuperLock() {
        return fristPageSuperLock;
    }

//    public XhsImageFragmentStatePagerAdapter getImageFragmentStatePagerAdapter() {
//        return imageFragmentStatePagerAdapter;
//    }
}
