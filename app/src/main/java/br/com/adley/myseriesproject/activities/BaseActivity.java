package br.com.adley.myseriesproject.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.library.AppConsts;

/**
 * Created by Adley.Damaceno on 11/04/2016.
 * Base activity.
 * If need to set a general code, which can be
 * used in all application, write here.
 */
public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mToolbar;
    private Context mContext;
    private boolean mIsLanguageUsePtBr = true;
    private String mPosterSize;
    private String mBackDropSize;

    private String LOG_TAG = BaseActivity.class.getSimpleName();

    public Toolbar getToolbar() {
        return mToolbar;
    }

    protected Toolbar activateToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.app_bar);
            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
            }
        }
        return mToolbar;
    }

    protected Toolbar activateToolbarWithHomeEnabled() {
        activateToolbar();
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        return mToolbar;

    }

    protected void activateToolbarWithNavigationView(Context context) {
        activateToolbar();
        this.mContext = context;
        // --- Navigation Drawer ---//
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_open);
        if (drawerLayout != null) {
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
        }
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search_show) {
            startActivity(new Intent(mContext, SearchTVShowActivity.class));
        }
        if (id == R.id.nav_about_app) {
            startActivity(new Intent(mContext, AboutAppActivity.class));
        }
        if (id == R.id.nav_prefs_app) {
            startActivity(new Intent(mContext, AppPreferences.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, AppPreferences.class));
        } else if (id == R.id.action_search_show) {
            startActivity(new Intent(this, SearchTVShowActivity.class));
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void loadConfigPreferences(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mIsLanguageUsePtBr = sharedPreferences.getBoolean(AppConsts.LANGUAGE_USE_PTBR, false);
        mPosterSize = sharedPreferences.getString(context.getString(R.string.preferences_poster_size_key), AppConsts.POSTER_DEFAULT_SIZE);
        mBackDropSize = sharedPreferences.getString(context.getString(R.string.preferences_backdrop_size_key), AppConsts.BACKDROP_DEFAULT_SIZE);
    }

    public boolean isLanguageUsePtBr() {
        return mIsLanguageUsePtBr;
    }

    public void setLanguageUsePtBr(boolean languageUsePtBr) {
        mIsLanguageUsePtBr = languageUsePtBr;
    }

    public String getPosterSize() {
        return mPosterSize;
    }

    public void setPosterSize(String posterSize) {
        mPosterSize = posterSize;
    }

    public String getBackDropSize() {
        return mBackDropSize;
    }

    public void setBackDropSize(String backDropSize) {
        mBackDropSize = backDropSize;
    }
}
