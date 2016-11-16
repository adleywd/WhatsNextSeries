package br.com.adley.whatsnextseries.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

import br.com.adley.whatsnextseries.R;
import br.com.adley.whatsnextseries.adapters.viewpager.CustomViewPager;
import br.com.adley.whatsnextseries.adapters.viewpager.HomePageAdapter;
import br.com.adley.whatsnextseries.fragments.FavoritesFragment;
import br.com.adley.whatsnextseries.library.AppConsts;
import br.com.adley.whatsnextseries.library.Changelog;
import br.com.adley.whatsnextseries.library.Utils;

public class HomeActivity extends BaseActivity{

    private final String LOG_TAG = getClass().getSimpleName();
    private long mBackPressed;
    private AdView mAdView;
    private TabLayout mTabLayout;
    private CustomViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        activateToolbarWithNavigationView(HomeActivity.this);

        //Ad Config
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, getString(R.string.application_id_ad));

        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
        mAdView = (AdView) findViewById(R.id.ad_view_home);

        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(getString(R.string.device_id_test1))
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Utils.setLayoutVisible(mAdView);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Utils.setLayoutInvisible(mAdView);
            }
        });

        // Create custom tabs.
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        View favoritesView = getLayoutInflater().inflate(R.layout.tab_home, null);
        ImageView favorites_tab_icon = (ImageView) favoritesView.findViewById(R.id.icon_tab);
        favorites_tab_icon.setImageResource(R.drawable.ic_favorite_white_24dp);
        TextView favorites_tab_text = (TextView)favoritesView.findViewById(R.id.text_tab);
        favorites_tab_text.setText(getString(R.string.favorites_label_fragment));

        View airTodayView = getLayoutInflater().inflate(R.layout.tab_home, null);
        ImageView air_today_tab_icon = (ImageView) airTodayView.findViewById(R.id.icon_tab);
        air_today_tab_icon.setImageResource(R.drawable.ic_live_tv_white_24dp);
        TextView air_today_tab_text = (TextView)airTodayView.findViewById(R.id.text_tab);
        air_today_tab_text.setText(getString(R.string.air_today_label_fragment));

        mViewPager = (CustomViewPager) findViewById(R.id.home_pager);
        if (mTabLayout != null) {
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(favoritesView));
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(airTodayView));
            mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            final HomePageAdapter adapter = new HomePageAdapter
                    (getSupportFragmentManager(), mTabLayout.getTabCount());
            if (mViewPager != null) {
                mViewPager.setAdapter(adapter);
                mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
                mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        mViewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }
            // Execute changelog when app version name change.
            Changelog changelog = new Changelog(this, false);
            changelog.execute();
        }
    }

    public TabLayout getTabLayout() {
        return mTabLayout;
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
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            case R.id.action_mode_enable:
                return false;
            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + AppConsts.TIME_INTERVAL_CLOSE_APP > System.currentTimeMillis()) {
            finish();
            return;
        } else {
            Toast.makeText(getBaseContext(), this.getString(R.string.twice_tap_close_app), Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    public void refreshFavorites(){
        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        for (Fragment fragmento: allFragments) {
            if (fragmento instanceof FavoritesFragment){
                ((FavoritesFragment) fragmento).executeFavoriteList();
            }
        }
    }

    public void setTabPagingEnable(boolean enabled){
        mViewPager.setPagingEnabled(enabled);
    }

    @Override
    public void onResume() {
        // Resume the AdView.
        super.onResume();
        mAdView.resume();
    }

    @Override
    public void onPause() {
        // Pause the AdView.
        super.onPause();
        mAdView.pause();

    }

    @Override
    public void onDestroy() {
        // Destroy the AdView.
        super.onDestroy();
        mAdView.destroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Remove the ad keeping the attributes
        AdView ad = (AdView) findViewById(R.id.ad_view_home);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) (ad != null ? ad.getLayoutParams() : null);
        RelativeLayout parentLayout = (RelativeLayout) (ad != null ? ad.getParent() : null);
        if (parentLayout != null) {
            parentLayout.removeView(ad);
        }

        // Re-initialise the ad
        mAdView.destroy();
        mAdView = new AdView(this);
        mAdView.setAdSize(com.google.android.gms.ads.AdSize.SMART_BANNER);
        mAdView.setAdUnitId(getString(R.string.banner_ad_unit_id_home));
        mAdView.setId(R.id.ad_view_home);
        mAdView.setLayoutParams(lp);
        if (parentLayout != null) {
            parentLayout.addView(mAdView);
        }

        // Re-fetch add and check successful load
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(getString(R.string.device_id_test1))
                .build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Utils.setLayoutVisible(mAdView);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Utils.setLayoutInvisible(mAdView);
            }
        });
    }
}