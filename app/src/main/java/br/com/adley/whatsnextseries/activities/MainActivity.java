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
import br.com.adley.whatsnextseries.library.AppConsts;

public class MainActivity extends BaseActivity {

    private final String LOG_TAG = getClass().getSimpleName();
    private long mBackPressed;
    private FavoritesFragment mFavoritesFragment;
    private AirTodayFragment mAirTodayFragment;
    private String TAG_FAVORITES = "tag_favorites";
    private String TAG_AIR_TODAY = "tag_air_today";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activateToolbar();
        mFavoritesFragment = FavoritesFragment.newInstance();
        mAirTodayFragment = AirTodayFragment.newInstance();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment selectedFragment = null;
            String selectedTAG = "";

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
                        pushFragments(TAG_FAVORITES, mFavoritesFragment); // TODO : Criar fragmento da aba popular
                        break;
                }
                return true;
            }
        });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content_main, mFavoritesFragment, TAG_FAVORITES);
        transaction.commit();
    }

    private void pushFragments(String tag, Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (manager.findFragmentByTag(tag) == null) {
            ft.add(R.id.content_main, fragment, tag);
        }

        Fragment fragmentFavorites = manager.findFragmentByTag(TAG_FAVORITES);
        Fragment fragmentAirToday = manager.findFragmentByTag(TAG_AIR_TODAY);

        // Hide all Fragment
        if (fragmentFavorites != null) {
            ft.hide(fragmentFavorites);
        }
        if (fragmentAirToday != null) {
            ft.hide(fragmentAirToday);
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
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_mode_delete:
                return false;
            case R.id.action_search_home:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            case R.id.action_mode_enable:
                return false;
            case R.id.action_mode_forward:
                return false;
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
