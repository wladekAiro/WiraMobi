package com.example.wladek.wira.pager_adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by wladek on 8/9/16.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> aFragmentList = new ArrayList<>();
    private ArrayList<String> aFragmentTitles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return aFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return aFragmentList.size();
    }

    public void addFragment(Fragment fragment , String title){
        aFragmentList.add(fragment);
        aFragmentTitles.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
//        return aFragmentTitles.get(position);
        return null;
    }
}
