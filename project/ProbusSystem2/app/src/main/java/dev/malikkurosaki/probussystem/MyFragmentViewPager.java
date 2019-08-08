package dev.malikkurosaki.probussystem;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

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
