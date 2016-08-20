package br.com.adley.myseriesproject.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.adapters.viewpager.HomePageAdapter;
import br.com.adley.myseriesproject.library.AppConsts;
import br.com.adley.myseriesproject.library.Utils;

public class HomeActivity extends BaseActivity {

    private long mBackPressed;
    private AdView mAdView;

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

        // Tabs Setup
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.home_pager);
        if (tabLayout != null) {
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.favorites_label_fragment)));
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.air_today_label_fragment)));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            final HomePageAdapter adapter = new HomePageAdapter
                    (getSupportFragmentManager(), tabLayout.getTabCount());
            if (viewPager != null) {
                viewPager.setAdapter(adapter);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
        if (id == R.id.action_refresh) {
            return false;
        }
        return true;
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

    public void setAdViewVisible (){
        Utils.setLayoutVisible(mAdView);
    }

    public void setAdViewInvisible (){
        Utils.setLayoutInvisible(mAdView);
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