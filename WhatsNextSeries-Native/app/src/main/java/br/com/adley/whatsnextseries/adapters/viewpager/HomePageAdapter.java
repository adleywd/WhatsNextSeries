package br.com.adley.whatsnextseries.adapters.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.adley.whatsnextseries.fragments.FavoritesFragment;
import br.com.adley.whatsnextseries.fragments.AirTodayFragment;

/**
 * Created by Adley Damaceno on 21/07/2016.
 * Page adapter for tabs in home activity
 */
public class HomePageAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public HomePageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new FavoritesFragment();
            case 1:
                return new AirTodayFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
