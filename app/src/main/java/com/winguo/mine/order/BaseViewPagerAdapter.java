package com.winguo.mine.order;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.List;

/**
 * @ explain:
 * @ author：xujun on 2016/7/30 18:48
 * @ email：gdutxiaoxu@163.com
 */
public class BaseViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> data;
    private String[] titles;

    public BaseViewPagerAdapter(FragmentManager fm, List<Fragment> data, String[] titles) {
        super(fm);
        this.data = data;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 0 : data.size();
    }

    boolean isEmpty() {
        return data == null || data.size() == 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return this.data.get(position);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
          Log.e("BaseViewPagerAdapter", "destroyItem");
        //super.destroyItem(container, position, object);
    }
}
