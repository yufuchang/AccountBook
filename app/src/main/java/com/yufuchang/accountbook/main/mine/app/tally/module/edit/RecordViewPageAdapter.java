package com.yufuchang.accountbook.main.mine.app.tally.module.edit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;



class RecordViewPageAdapter extends FragmentPagerAdapter {

    private List<String> mTitleList = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();

    RecordViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    void setData(List<Fragment> fragmentList, List<String> titleList) {
        mFragmentList.clear();
        mFragmentList.addAll(fragmentList);
        mTitleList.clear();
        mTitleList.addAll(titleList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position >= 0 && position < mTitleList.size()) {
            return mTitleList.get(position);
        }
        return "";
    }
}
