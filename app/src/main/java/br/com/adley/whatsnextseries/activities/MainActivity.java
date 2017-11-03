package br.com.adley.whatsnextseries.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import br.com.adley.whatsnextseries.R;
import br.com.adley.whatsnextseries.fragments.AirTodayFragment;
import br.com.adley.whatsnextseries.fragments.FavoritesFragment;
import br.com.adley.whatsnextseries.fragments.PopularFragment;
import br.com.adley.whatsnextseries.library.AppConsts;
import br.com.adley.whatsnextseries.library.Changelog;

public class MainActivity extends BaseActivity {

    private final String LOG_TAG = getClass().getSimpleName();
    private long mBackPressed;
    private FavoritesFragment mFavoritesFragment;
    private AirTodayFragment mAirTodayFragment;
    private PopularFragment mPopularFragment;
    private String TAG_FAVORITES = "tag_favorites";
    private String TAG_AIR_TODAY = "tag_air_today";
    private String TAG_POPULAR = "tag_popular";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activateToolbar();
        mFavoritesFragment = FavoritesFragment.newInstance();
        mAirTodayFragment = AirTodayFragment.newInstance();
        mPopularFragment = PopularFragment.newInstance();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_favorites:
                        pushFragments(TAG_FAVORITES, mFavoritesFragment);
                        break;
                    case R.id.navigation_airing_today:
                        pushFragments(TAG_AIR_TODAY, mAirTodayFragment);
                        break;
                    case R.id.navigation_popular:
                        pushFragments(TAG_POPULAR, mPopularFragment);
                        break;
                }
                return true;
            }
        });

        //Manually displaying the first fragment - one time only
        pushFragments(TAG_FAVORITES, mFavoritesFragment);
    }

    private void pushFragments(String tag, Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (manager.findFragmentByTag(tag) == null) {
            ft.add(R.id.content_main, fragment, tag);
        }

        Fragment fragmentFavorites = manager.findFragmentByTag(TAG_FAVORITES);
        Fragment fragmentAirToday = manager.findFragmentByTag(TAG_AIR_TODAY);
        Fragment fragmentPopular = manager.findFragmentByTag(TAG_POPULAR);

        // Hide all Fragment
        if (fragmentFavorites != null) {
            ft.hide(fragmentFavorites);
        }
        if (fragmentAirToday != null) {
            ft.hide(fragmentAirToday);
        }
        if (fragmentPopular != null) {
            ft.hide(fragmentPopular);
        }

        // Show  current Fragment
        if (tag.equals(TAG_FAVORITES)) {
            if (fragmentFavorites != null) {
                ft.show(fragmentFavorites);
            }
        }
        if (tag.equals(TAG_AIR_TODAY)) {
            if (fragmentAirToday != null) {
                ft.show(fragmentAirToday);
            }
        }

        if (tag.equals(TAG_POPULAR)) {
            if (fragmentPopular != null) {
                ft.show(fragmentPopular);
            }
        }
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_mode_delete:
                return false;
            case R.id.action_search_home:
                startActivity(new Intent(this, SearchActivity.class));
            case R.id.action_mode_enable:
                return false;
            case R.id.action_mode_forward:
                return false;
            case R.id.action_notifications:
                startActivity(new Intent(this, NotificationActivity.class));
                return true;
            case R.id.action_about:
                startActivity(new Intent(this, AboutAppActivity.class));
                return true;
            case R.id.action_news:
                Changelog changelog = new Changelog(this, true);
                changelog.execute();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, AppPreferences.class));
                return true;
            default:
                return true;
        }
    }


    @Override
    public void onBackPressed() {
        if(getIsInActionMode()) {
            List<Fragment> allFragments = getSupportFragmentManager().getFragments();
            for (Fragment fragment: allFragments) {
                if (fragment instanceof FavoritesFragment){
                    ((FavoritesFragment) fragment).clearActionModeWithNotify();
                }
            }
        }else{
            if (mBackPressed + AppConsts.TIME_INTERVAL_CLOSE_APP > System.currentTimeMillis()) {
                finish();
                return;
            } else {
                Toast.makeText(getBaseContext(), this.getString(R.string.twice_tap_close_app), Toast.LENGTH_SHORT).show();
            }
            mBackPressed = System.currentTimeMillis();
        }
    }

    public void refreshFavorites(){
        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment: allFragments) {
            if (fragment instanceof FavoritesFragment){
                ((FavoritesFragment) fragment).executeFavoriteList();
            }
        }
    }

    private boolean getIsInActionMode(){
        boolean isInActionMode = false;
        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment: allFragments) {
            if (fragment instanceof FavoritesFragment){
                isInActionMode = ((FavoritesFragment) fragment).isInActionMode();
            }
        }
        return isInActionMode;
    }
}
