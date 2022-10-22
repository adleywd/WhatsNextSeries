package br.com.adley.whatsnextseries.activities;



import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import br.com.adley.whatsnextseries.R;
import br.com.adley.whatsnextseries.fragments.AirTodayFragment;
import br.com.adley.whatsnextseries.fragments.FavoritesFragment;
import br.com.adley.whatsnextseries.fragments.NotificationsFragment;
import br.com.adley.whatsnextseries.fragments.PopularFragment;
import br.com.adley.whatsnextseries.library.AppConsts;
import br.com.adley.whatsnextseries.library.Changelog;
import br.com.adley.whatsnextseries.library.Utils;

public class MainActivity extends BaseActivity {

    private final String LOG_TAG = getClass().getSimpleName();
    private long mBackPressed;
    private FavoritesFragment mFavoritesFragment;
    private AirTodayFragment mAirTodayFragment;
    private PopularFragment mPopularFragment;
    private NotificationsFragment mNotificationsFragment;
    private BottomNavigationView mBottomNavigationView;
    private boolean mFirstDarkMode;
    private boolean mFirstAnimatedMenu;
    private boolean mFirstLanguagePtBr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activateToolbar();
        loadConfigPreferences(this);
        mFirstDarkMode = isDarkMode();
        mFirstAnimatedMenu = isAnimateMenu();
        mFirstLanguagePtBr = isLanguageUsePtBr();

        Changelog changelog = new Changelog(this,false);
        changelog.execute(); // Execute changelog for users update

        Utils.createNotificationChannels(this);

        mFavoritesFragment = FavoritesFragment.newInstance();
        mAirTodayFragment = AirTodayFragment.newInstance();
        mPopularFragment = PopularFragment.newInstance();
        mNotificationsFragment = NotificationsFragment.newInstance();

        mBottomNavigationView = findViewById(R.id.navigation);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_favorites:
                        pushFragments(AppConsts.TAG_FAVORITES, mFavoritesFragment);
                        break;
                    case R.id.navigation_airing_today:
                        pushFragments(AppConsts.TAG_AIR_TODAY, mAirTodayFragment);
                        break;
                    case R.id.navigation_popular:
                        pushFragments(AppConsts.TAG_POPULAR, mPopularFragment);
                        break;
                    case R.id.navigation_notifications:
                        pushFragments(AppConsts.TAG_NOTIFICATIONS, mNotificationsFragment);
                        break;
                }
                return true;
            }
        });

        // Check for dark mode.
        if(isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Check for animated menu.
        if (!isAnimateMenu()) {
            Utils.disableShiftMode(mBottomNavigationView);
        }

        //Manually displaying the first fragment - one time only
        pushFragments(AppConsts.TAG_FAVORITES, mFavoritesFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadConfigPreferences(this);

        // Search in PtBR
        if(isLanguageUsePtBr() != mFirstLanguagePtBr) {
            RecreateActivity();
        }

        // Animated menu config base on user choice.
        if(isAnimateMenu() != mFirstAnimatedMenu) {
            RecreateActivity();
        }

        // If Theme change, then recreate activity.
        if(isDarkMode() != mFirstDarkMode) {
            mBottomNavigationView.setSelectedItemId(R.id.navigation_favorites);
            RecreateActivity();
        }

        // Privacy Policy Dialog Alert
        if(!isAcceptPrivacyPolicy()) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit()
                                    .putBoolean(getString(R.string.preference_accept_privacy_policy), true).apply();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                finishAndRemoveTask();
                            } else {
                                finishAffinity();
                                System.exit(0);
                            }
                            break;
                    }
                }
            };
            String privacyPolicyLink = "<b><a href=\""+getString(R.string.link_privacy_policy)+"\"> "+getString(R.string.text_link_privacy_policy2)+"</a></b>";
            String privacyPolicyMessage = getString(R.string.text_link_privacy_policy)+ privacyPolicyLink;
            Spanned privacyPolicyText = Html.fromHtml(privacyPolicyMessage);
            AlertDialog.Builder privacyPolicyAlertBuilder = new AlertDialog.Builder(this);
            privacyPolicyAlertBuilder.setMessage(privacyPolicyText)
                    .setPositiveButton(getString(R.string.accept_privacy_policy), dialogClickListener)
                    .setNegativeButton(getString(R.string.recuse_privacy_policy), dialogClickListener)
                    .setCancelable(false);
            AlertDialog alertDialog = privacyPolicyAlertBuilder.create();
            alertDialog.show();
            TextView msgTxt = (TextView) alertDialog.findViewById(android.R.id.message);
            msgTxt.setMovementMethod(LinkMovementMethod.getInstance());
        }


        // Tips Configurations
        if (!isTipsOn()) {
            Utils.setLayoutInvisible(findViewById(R.id.tips_main_layout));
        } else {
            Utils.fadeInLayout(findViewById(R.id.tips_main_layout));
            ImageView buttonHideTips = (ImageView) findViewById(R.id.btn_hide_main_tips);
            buttonHideTips.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit()
                            .putBoolean(getString(R.string.preferences_tips_enable), false).apply();
                    Utils.fadeOutLayout(findViewById(R.id.tips_main_layout));
                }
            });
        }
    }

    private void pushFragments(String tag, Fragment fragment) {
        FragmentManager manager = fragment.getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (manager.findFragmentByTag(tag) == null) {
            ft.add(R.id.content_main, fragment, tag);
        }

        Fragment fragmentFavorites = manager.findFragmentByTag(AppConsts.TAG_FAVORITES);
        Fragment fragmentAirToday = manager.findFragmentByTag(AppConsts.TAG_AIR_TODAY);
        Fragment fragmentPopular = manager.findFragmentByTag(AppConsts.TAG_POPULAR);
        Fragment fragmentNotifications = manager.findFragmentByTag(AppConsts.TAG_NOTIFICATIONS);

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
        if (fragmentNotifications != null) {
            ft.hide(fragmentNotifications);
        }

        // Show  current Fragment
        if (tag.equals(AppConsts.TAG_FAVORITES)) {
            if (fragmentFavorites != null) {
                ft.show(fragmentFavorites);
            }
        }
        if (tag.equals(AppConsts.TAG_AIR_TODAY)) {
            if (fragmentAirToday != null) {
                ft.show(fragmentAirToday);
            }
        }

        if (tag.equals(AppConsts.TAG_POPULAR)) {
            if (fragmentPopular != null) {
                ft.show(fragmentPopular);
            }
        }

        if (tag.equals(AppConsts.TAG_NOTIFICATIONS)) {
            if (fragmentNotifications != null) {
                ft.show(fragmentNotifications);
            }
        }
        ft.commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_mode_delete:
                return false;
            case R.id.action_search_home:
                startActivity(new Intent(this, SearchActivity.class));
            case R.id.action_mode_forward:
                return false;
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
            case R.id.action_privacy:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_privacy_policy))));
                return true;
            default:
                return true;
        }
    }


    @Override
    public void onBackPressed() {
        if (getIsInActionMode()) {
            List<Fragment> allFragments = getSupportFragmentManager().getFragments();
            for (Fragment fragment : allFragments) {
                if (fragment instanceof FavoritesFragment) {
                    ((FavoritesFragment) fragment).clearActionModeWithNotify();
                }
            }
        } else {
            if (mBackPressed + AppConsts.TIME_INTERVAL_CLOSE_APP > System.currentTimeMillis()) {
                finish();
                return;
            } else {
                Toast.makeText(getBaseContext(), this.getString(R.string.twice_tap_close_app), Toast.LENGTH_SHORT).show();
            }
            mBackPressed = System.currentTimeMillis();
        }
    }

    public void refreshFavorites() {
        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : allFragments) {
            if (fragment instanceof FavoritesFragment) {
                ((FavoritesFragment) fragment).executeFavoriteList();
            }
        }
    }

    private boolean getIsInActionMode() {
        boolean isInActionMode = false;
        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : allFragments) {
            if (fragment instanceof FavoritesFragment) {
                isInActionMode = ((FavoritesFragment) fragment).isInActionMode();
            }
        }
        return isInActionMode;
    }
}
