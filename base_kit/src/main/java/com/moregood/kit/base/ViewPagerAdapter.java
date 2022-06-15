package com.moregood.kit.base;


import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Devin.Ding
 * Date: 2019/10/16
 * Descripe:
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragments = new ArrayList<>();//添加的Fragment的集合
    private final List<String> mFragmentsTitles = new ArrayList<>();//每个Fragment对应的title的集合

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    /**
     * @param fragment      添加Fragment
     * @param fragmentTitle Fragment的标题，即TabLayout中对应Tab的标题
     */
    public void addFragment(Fragment fragment, String fragmentTitle) {
        mFragments.add(fragment);
        mFragmentsTitles.add(fragmentTitle);
    }




    public void removeFragment() {
        mFragmentsTitles.remove(1);
        mFragmentsTitles.remove(2);

        mFragments.remove(1);
        mFragments.remove(2);
    }
    public void removeAllFragment() {
        mFragments.clear();
        mFragmentsTitles.clear();
    }

    @Override
    public Fragment getItem(int position) {
        //得到对应position的Fragment
        return mFragments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mFragments.get(position).hashCode();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        //得到对应position的Fragment的title
        return mFragmentsTitles.get(position);
    }
}
