package br.com.adley.myseriesproject.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.adley.myseriesproject.fragments.FavoritesFragment;
import br.com.adley.myseriesproject.fragments.AirTodayFragment;

/**
 * Created by Adley.Damaceno on 21/07/2016.
 * Page adapter for tabs
 */
public class HomePageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public HomePageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FavoritesFragment favoritesFragment = new FavoritesFragment();
                return favoritesFragment;
            case 1:
                AirTodayFragment airTodayFragment = new AirTodayFragment();
                return airTodayFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
