package br.com.adley.whatsnextseries.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

import br.com.adley.whatsnextseries.R;
import br.com.adley.whatsnextseries.adapters.recyclerview.SearchShowRecyclerViewAdapter;
import br.com.adley.whatsnextseries.library.AppConsts;
import br.com.adley.whatsnextseries.library.RecyclerItemClickListener;
import br.com.adley.whatsnextseries.library.Utils;
import br.com.adley.whatsnextseries.library.enums.DownloadStatus;
import br.com.adley.whatsnextseries.models.TVShow;
import br.com.adley.whatsnextseries.service.GetTVShowJsonData;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SearchShowRecyclerViewAdapter mSearchShowRecyclerViewAdapter;
    private View mNoInternetConnection;
    private View mTVShowSearchLayout;
    private View mLoadingBarLayout;
    private AdView mAdView;
    private AlertDialog mAlertDialog;
    private View mShowListEmpty;
    private boolean mIsLanguageUsePtBr = true;
    private String mPosterSize;
    private String mBackDropSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null && toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setTitle("");
        }
        //Ad Config
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, getString(R.string.application_id_ad));

        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
        mAdView = (AdView) findViewById(R.id.ad_view_searchshow);

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
        // Get activity layout
        mTVShowSearchLayout = findViewById(R.id.tvshow_search_layout);
        mShowListEmpty = findViewById(R.id.list_empty_layout);

        // Get ProgressBar layout and set invisible
        mLoadingBarLayout = findViewById(R.id.loading_panel);
        Utils.setLayoutInvisible(mLoadingBarLayout);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_home);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSearchShowRecyclerViewAdapter = new SearchShowRecyclerViewAdapter(SearchActivity.this, new ArrayList<TVShow>());
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(mSearchShowRecyclerViewAdapter);
        mRecyclerView.setAdapter(animationAdapter);

        // Create the touch for the recyclerview list
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Creates and configure intent to call tv show details activity
                Intent intent = new Intent(SearchActivity.this, DetailsActivity.class);
                intent.putExtra(AppConsts.TVSHOW_TRANSFER, mSearchShowRecyclerViewAdapter.getTVShow(position));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (mAlertDialog != null && mAlertDialog.isShowing()) mAlertDialog.cancel();
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                final TVShow tvshow = mSearchShowRecyclerViewAdapter.getTVShow(position);
                builder.setMessage(getString(R.string.add_show_float_menu, tvshow.getName()));
                builder.setPositiveButton(getString(R.string.yes_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPref = getSharedPreferences(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
                        String restoredFavorites = sharedPref.getString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, null);
                        SharedPreferences.Editor spEditor = sharedPref.edit();
                        List<Integer> idShowList = Utils.convertStringToIntegerList(AppConsts.FAVORITES_SHAREDPREFERENCES_DELIMITER, restoredFavorites);
                        if (!Utils.checkItemInIntegerList(idShowList, tvshow.getId())) {
                            idShowList.add(tvshow.getId());
                            String idsResult = Utils.convertListToString(AppConsts.FAVORITES_SHAREDPREFERENCES_DELIMITER, idShowList);
                            spEditor.putString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, idsResult);
                            spEditor.apply();
                            mAlertDialog.dismiss();
                            Snackbar favoriteSnackbar = Utils.createSnackbarObject(Color.GREEN, getString(R.string.success_add_new_show_with_name, tvshow.getName()), mTVShowSearchLayout);
                            favoriteSnackbar.setAction(SearchActivity.this.getString(R.string.undo_snackbar), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SharedPreferences sharedPref = getSharedPreferences(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
                                    String restoredFavorites = sharedPref.getString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, null);
                                    SharedPreferences.Editor spEditor = sharedPref.edit();
                                    List<Integer> idShowList = Utils.convertStringToIntegerList(AppConsts.FAVORITES_SHAREDPREFERENCES_DELIMITER, restoredFavorites);
                                    idShowList = Utils.removeIntegerItemFromList(idShowList, tvshow.getId());
                                    String idsResult = Utils.convertListToString(AppConsts.FAVORITES_SHAREDPREFERENCES_DELIMITER, idShowList);
                                    spEditor.putString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, idsResult);
                                    spEditor.apply();
                                    Utils.createSnackbar(Color.RED, getString(R.string.success_remove_show_with_name, tvshow.getName()), mTVShowSearchLayout);
                                }
                            });
                            favoriteSnackbar.setActionTextColor(Color.WHITE);
                            favoriteSnackbar.show();
                        }else{
                            // Already had in favorites.
                            Utils.createSnackbar(Color.RED, getString(R.string.error_already_has_show_with_name, tvshow.getName()), mTVShowSearchLayout);
                            mAlertDialog.dismiss();
                        }
                    }
                });
                builder.setNegativeButton(getString(R.string.no_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAlertDialog.dismiss();
                    }
                });
                mAlertDialog = builder.create();
                mAlertDialog.show();
            }

        }));


        mNoInternetConnection = findViewById(R.id.no_internet_connection);
        mTVShowSearchLayout = findViewById(R.id.tvshow_search_layout);
        // Load Preferences from BaseActivity
        loadConfigPreferences(this);
        /*
        String searchQuery = getIntent().getStringExtra(AppConsts.TOOLBAR_SEARCH_QUERY);
        if(searchQuery != null) {
            executeSearchShow(searchQuery);
        }*/
    }
    public void loadConfigPreferences(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mIsLanguageUsePtBr = sharedPreferences.getBoolean(AppConsts.LANGUAGE_USE_PTBR, false);
        mPosterSize = sharedPreferences.getString(context.getString(R.string.preferences_poster_size_key), AppConsts.POSTER_DEFAULT_SIZE);
        mBackDropSize = sharedPreferences.getString(context.getString(R.string.preferences_backdrop_size_key), AppConsts.BACKDROP_DEFAULT_SIZE);
    }

    private void executeSearchShow(String query){
        //Hide keyboard when button was clicked.
        // Set No Internet Layout Invisible
        Utils.setLayoutVisible(mRecyclerView);
        Utils.setLayoutInvisible(mNoInternetConnection);
        Utils.setLayoutInvisible(mShowListEmpty);
        //Check the phone connection status.
        if (!Utils.checkAppConnectionStatus(SearchActivity.this)) {
            //Toast.makeText(SearchActivity.this, getString(R.string.error_no_internet_connection), Toast.LENGTH_SHORT).show();
            Snackbar.make(mNoInternetConnection, getString(R.string.error_no_internet_connection), Snackbar.LENGTH_LONG).show();
            Utils.setLayoutInvisible(mRecyclerView);
            Utils.setLayoutVisible(mNoInternetConnection);
        } else if (query.isEmpty()) {
            Snackbar.make(mTVShowSearchLayout,  getString(R.string.error_blank_search_field), Snackbar.LENGTH_LONG).show();
        } else {
            // Set loading layout visible
            Utils.setLayoutVisible(mLoadingBarLayout);
            Utils.setLayoutInvisible(mRecyclerView);
            // Create and generate the recycler view for list of results
            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_home);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
            SearchActivity.ProcessTVShows processTVShows = new SearchActivity.ProcessTVShows(query);
            processTVShows.execute();
        }
    }

    // Process and execute data into recycler view
    public class ProcessTVShows extends GetTVShowJsonData {

        ProcessTVShows(String showName) {
            super(showName, SearchActivity.this, mIsLanguageUsePtBr, mPosterSize, mBackDropSize);
        }

        public void execute() {
            SearchActivity.ProcessTVShows.ProcessData processData = new SearchActivity.ProcessTVShows.ProcessData();
            processData.execute();
        }

        class ProcessData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                Utils.setLayoutInvisible(mLoadingBarLayout);
                if(getTVShows().size() == 0 && getDownloadStatus() == DownloadStatus.OK){
                    Snackbar.make(mTVShowSearchLayout,  getString(R.string.error_no_series_found), Snackbar.LENGTH_LONG).show();
                    Utils.setLayoutVisible(mShowListEmpty);
                    Utils.setLayoutInvisible(mRecyclerView);
                }else if(getDownloadStatus() != DownloadStatus.OK){
                    Utils.setLayoutInvisible(mShowListEmpty);
                    Utils.setLayoutInvisible(mRecyclerView);
                    Snackbar.make(mTVShowSearchLayout,  getString(R.string.error_no_internet_connection), Snackbar.LENGTH_LONG).show();
                }else{
                    Utils.setLayoutInvisible(mShowListEmpty);
                    Utils.setLayoutVisible(mRecyclerView);
                    mSearchShowRecyclerViewAdapter.loadNewData(getTVShows());
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.onActionViewExpanded();
        searchView.setSubmitButtonEnabled(true);
        // Change submit button
        ImageView submitButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_go_btn);
        submitButton.setImageResource(R.drawable.ic_search_white_24dp);
        final EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setImeOptions(DEFAULT_KEYS_SEARCH_LOCAL);
        //searchEditText.setTextColor(ContextCompat.getColor(this, R.color.md_black_1000));
        //View searchPlate = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        //searchPlate.setBackgroundResource(R.color.md_white_1000);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                executeSearchShow(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // AdView Configuration
        // Remove the ad keeping the attributes
        AdView ad = (AdView) findViewById(R.id.ad_view_searchshow);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) (ad != null ? ad.getLayoutParams() : null);
        RelativeLayout parentLayout = (RelativeLayout) (ad != null ? ad.getParent() : null);
        if (parentLayout != null) {
            parentLayout.removeView(ad);
        }

        // Re-initialise the ad
        mAdView.destroy();
        mAdView = new AdView(this);
        mAdView.setAdSize(com.google.android.gms.ads.AdSize.SMART_BANNER);
        mAdView.setAdUnitId(getString(R.string.banner_ad_unit_id_search_show));
        mAdView.setId(R.id.ad_view_searchshow);
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


    @Override
    public void onBackPressed() {
        onNavigateUp();
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
    protected void onRestart() {
        super.onRestart();
    }
}
