package br.com.adley.myseriesproject.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.adapters.viewpager.HomePageAdapter;
import br.com.adley.myseriesproject.library.AppConsts;

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
        mAdView.setVisibility(View.VISIBLE);
        mAdView.loadAd(adRequest);

        // Tabs Setup
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.favorites_label_fragment)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.air_today_label_fragment)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.home_pager);
        final HomePageAdapter adapter = new HomePageAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + AppConsts.TIME_INTERVAL_CLOSE_APP > System.currentTimeMillis())
        {
            finish();
            return;
        }
        else { Toast.makeText(getBaseContext(), this.getString(R.string.twice_tap_close_app), Toast.LENGTH_SHORT).show(); }
        mBackPressed = System.currentTimeMillis();

    }
}