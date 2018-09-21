package br.com.adley.whatsnextseries.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import br.com.adley.whatsnextseries.R;
import br.com.adley.whatsnextseries.library.AppConsts;

/**
 * Created by Adley.Damaceno on 11/04/2016.
 * Base activity.
 * If need to set a general code, which can be
 * used in all application, write here.
 */
public class BaseActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private Context mContext;
    private boolean mIsLanguageUsePtBr = true;
    private String mPosterSize;
    private String mBackDropSize;
    private boolean mAutoLoadAirToday = false;
    private boolean mTipsOn;
    private boolean mAcceptPrivacyPolicy;

    private boolean mAnimateMenu;

    public Toolbar getToolbar() {
        return mToolbar;
    }

    protected void activateToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.app_bar);
            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
            }
        }
    }

    protected void activateToolbarWithHomeEnabled() {
        activateToolbar();
        if (mToolbar != null && getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            default:
                finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void loadConfigPreferences(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mIsLanguageUsePtBr = sharedPreferences.getBoolean(AppConsts.LANGUAGE_USE_PTBR, false);
        mPosterSize = sharedPreferences.getString(context.getString(R.string.preferences_poster_size_key), AppConsts.POSTER_DEFAULT_SIZE);
        mBackDropSize = sharedPreferences.getString(context.getString(R.string.preferences_backdrop_size_key), AppConsts.BACKDROP_DEFAULT_SIZE);
        mAutoLoadAirToday = sharedPreferences.getBoolean(AppConsts.AUTO_LOAD_AIR_TODAY, true);
        mTipsOn = sharedPreferences.getBoolean(getString(R.string.preferences_tips_enable), true);
        mAnimateMenu = sharedPreferences.getBoolean(getString(R.string.preferences_animated_bottom_menu), true);
        mAcceptPrivacyPolicy = sharedPreferences.getBoolean(getString(R.string.preference_accept_privacy_policy), false);
    }

    public boolean isLanguageUsePtBr() {
        return mIsLanguageUsePtBr;
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

    public boolean autoLoadAirToday() {
        return mAutoLoadAirToday;
    }

    public void setAutoLoadAirToday(boolean autoLoadAirToday) {
        mAutoLoadAirToday = autoLoadAirToday;
    }

    public boolean isTipsOn() {
        return mTipsOn;
    }

    public void setTipsOn(boolean tipsOn) {
        mTipsOn = tipsOn;
    }

    public boolean isAnimateMenu() {
        return mAnimateMenu;
    }

    public void setAnimateMenu(boolean animateMenu) {
        mAnimateMenu = animateMenu;
    }

    public boolean isAcceptPrivacyPolicy() {
        return mAcceptPrivacyPolicy;
    }

    public void setAcceptPrivacyPolicy(boolean mAcceptPrivacyPolicy) {
        this.mAcceptPrivacyPolicy = mAcceptPrivacyPolicy;
    }
}
