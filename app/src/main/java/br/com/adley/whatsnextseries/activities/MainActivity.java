package br.com.adley.whatsnextseries.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activateToolbar();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment selectedFragment = null;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_favorites:
                        selectedFragment = FavoritesFragment.newInstance();
                        break;
                    case R.id.navigation_airing_today:
                        selectedFragment = AirTodayFragment.newInstance();
                        break;
                    case R.id.navigation_popular:
                        selectedFragment = FavoritesFragment.newInstance(); // TODO : Criar fragmento da aba popular
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_main, selectedFragment);
                transaction.commit();
                return true;
            }
        });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().
                beginTransaction();
        transaction.replace(R.id.content_main, FavoritesFragment.newInstance());
        transaction.commit();
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
