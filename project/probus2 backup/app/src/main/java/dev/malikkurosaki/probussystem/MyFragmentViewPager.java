package dev.malikkurosaki.probussystem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentViewPager extends FragmentStatePagerAdapter {

    List<Fragment> list = new ArrayList<>();
    MyFragmentViewPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    void addFragment(Fragment fragment){
        list.add(fragment);
    }
}
