package com.mcgrady.main.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.blankj.utilcode.util.ConvertUtils;
import com.mcgrady.main.mvp.ui.fragment.xhs.XhsWelcomeFragment;

/**
 * 顶层ViewPager,包含2个fragment childview
 * 处于第一个fragment,所有事件将被拦截在本层View,手动分发到指定子View
 * 处于第二个fragment,释放拦截
 * @author Zhongdaxia
 */
public class XhsParentViewPager extends ViewPager {

    public static boolean mLoginPageLock = false;

    /**
     * 跳过按钮相关
     */
    private Boolean mSkipFlag = true;
    private float mCx,mCy;
    private int[] mTvSkipLocation;
    private int margin, left, top, right, bottom;

    private XhsWelcomeFragment welcomeFragment;

    public XhsParentViewPager(Context context) {
        this(context, null);
    }

    public XhsParentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setWelcomAnimFragment(XhsWelcomeFragment welcomAnimFragment) {
        this.welcomeFragment = welcomAnimFragment;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mLoginPageLock) {
            requestDisallowInterceptTouchEvent(true);
            return super.onInterceptTouchEvent(ev);
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.getCurrentItem() == 1 || mLoginPageLock) {
            return true;
        }
        if (welcomeFragment != null) {
            mCx = ev.getX();
            mCy = ev.getY();

            /**
             * 由于子View被拦截,这里通过计算跳过按钮的坐标手动处理跳过click事件
             */
            if (mTvSkipLocation == null) {
                mTvSkipLocation = new int[2];
                welcomeFragment.getTvSkip().getLocationOnScreen(mTvSkipLocation);
            }
            if (left == 0) {
                margin = ConvertUtils.dp2px(10);
                left = mTvSkipLocation[0] - margin;
                top = mTvSkipLocation[1] - margin;
                right = mTvSkipLocation[0] + welcomeFragment.getTvSkip().getWidth() + margin;
                bottom = mTvSkipLocation[1] + welcomeFragment.getTvSkip().getHeight() + margin;
            }
            if (mCx - left > 0 && right - mCx > 0 && mCy - top > 0 && bottom - mCy > 0 && !mLoginPageLock) {
            } else {
                mSkipFlag = false;
            }
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                if (mCx - left > 0 && right - mCx > 0 && mCy - top > 0 && bottom - mCy > 0 && !mLoginPageLock && mSkipFlag) {
                    //todo:
//                    if (welcomeFragment.getImageFragmentStatePagerAdapter() != null) {
//                        welcomeFragment.getImageFragmentStatePagerAdapter().onSkip();
//                    }
                }
                mSkipFlag = true;
                mCx = 0;
                mCy = 0;
            }

            /**
             * touch事件由顶层viewpager捕捉,手动分发到两个子viewpager
             */
            if (welcomeFragment.getVpImage() != null && !welcomeFragment.getVpImage().mIsLockScoll) {
                welcomeFragment.getVpImage().onTouchEvent(ev);
            }
            if (welcomeFragment.getVpText() != null) {
                welcomeFragment.getVpText().onTouchEvent(ev);
            }
            if (welcomeFragment.isMoveParent()) {
                return super.onTouchEvent(ev);
            }
        }
        return true;
    }
}
