package br.com.adley.whatsnextseries.adapters.viewpager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import br.com.adley.whatsnextseries.fragments.AirTodayFragment;
import br.com.adley.whatsnextseries.fragments.FavoritesFragment;

/**
 * Created by Adley.Damaceno on 23/11/2016.
 * Page adapter for tabs in detail activity
 */
public class DetailsPageAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public DetailsPageAdapter(FragmentManager fm, int NumOfTabs) {
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