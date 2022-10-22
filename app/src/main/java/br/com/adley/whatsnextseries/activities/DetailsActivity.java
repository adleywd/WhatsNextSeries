package br.com.adley.whatsnextseries.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import br.com.adley.whatsnextseries.R;
import br.com.adley.whatsnextseries.adapters.recyclerview.ListSeasonRecyclerViewAdapter;
import br.com.adley.whatsnextseries.library.AppConsts;
import br.com.adley.whatsnextseries.library.RecyclerItemClickListener;
import br.com.adley.whatsnextseries.library.Utils;
import br.com.adley.whatsnextseries.models.TVShow;
import br.com.adley.whatsnextseries.models.TVShowDetails;
import br.com.adley.whatsnextseries.models.TVShowSeasons;
import br.com.adley.whatsnextseries.service.GetTVShowDetailsJsonData;
import br.com.adley.whatsnextseries.service.GetTVShowSeasonJsonData;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class DetailsActivity extends BaseActivity {

    private TVShowDetails mTVShowDetails;

    private TextView mRateTVShow;
    private ImageView mPoster;
    private String mTitle;
    private TextView mNextEpisodeDateName;
    private ImageView mNextEpisodePoster;
    private TextView mSynopsisTVShow;
    private TextView mTVShowDetailsNoSeason;
    private RecyclerView mRecyclerViewSeason;
    private FloatingActionButton mFab;
    private View mTVShowDetailsView;

    private List<TVShowSeasons> mTVShowSeasons;
    private ProgressDialog mProgress;
    private ListSeasonRecyclerViewAdapter mListSeasonRecyclerViewAdapter;
    private String mRestoredFavorites;
    private SharedPreferences mSharedPref;
    private int mLastSeasonNumber;
    private List<Integer> mIdShowList = new ArrayList<>();
    private SharedPreferences.Editor mSpEditor;
    private AdView mAdView;
    private boolean mIsTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Enable Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Ad Config
        // Initialize the Mobile Ads SDK.
        //TODO UNCOMMENT
//        MobileAds.initialize(this, getString(R.string.application_id_ad));

        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
//        mAdView = (AdView) findViewById(R.id.ad_view_detail_show);

        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .addTestDevice(getString(R.string.device_id_test1))
//                .build();

        // Start loading the ad in the background.
//        mAdView.loadAd(adRequest);
//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                Utils.setLayoutVisible(mAdView);
//                super.onAdLoaded();
//            }
//
//            @Override
//            public void onAdFailedToLoad(int i) {
//                Utils.setLayoutInvisible(mAdView);
//                super.onAdFailedToLoad(i);
//            }
//        });

        mIsTablet = Utils.isTablet(this);

        // Get View Elements
        mRateTVShow = (TextView) findViewById(R.id.rate_tvshow);
        mPoster = (ImageView) findViewById(R.id.background_poster);
        mNextEpisodeDateName = (TextView) findViewById(R.id.next_episode_date_name);
        mNextEpisodePoster = (ImageView) findViewById(R.id.next_episode_poster);
        mTVShowDetailsNoSeason = (TextView) findViewById(R.id.no_list_season_error);
        mSynopsisTVShow = (TextView) findViewById(R.id.synopsis_tvshow);
        mFab = (FloatingActionButton) findViewById(R.id.details_fab);
        mTVShowDetailsView = findViewById(R.id.activity_details);

        if (!Utils.checkAppConnectionStatus(this)) {
            Utils.createSnackbarIndefine(Color.WHITE, getString(R.string.error_no_internet_connection), mTVShowDetailsView);
        } else {
            mSharedPref = getSharedPreferences(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
            mRestoredFavorites = mSharedPref.getString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, null);

            mTVShowSeasons = new ArrayList<>();
            mRecyclerViewSeason = (RecyclerView) findViewById(R.id.recycler_view_season_list);
            mRecyclerViewSeason.setLayoutManager(new GridLayoutManager(this, 2));
            mListSeasonRecyclerViewAdapter = new ListSeasonRecyclerViewAdapter(DetailsActivity.this, new ArrayList<TVShowSeasons>());
            mRecyclerViewSeason.setAdapter(mListSeasonRecyclerViewAdapter);

            mRecyclerViewSeason.addOnItemTouchListener(new RecyclerItemClickListener(this,
                    mRecyclerViewSeason, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    startEpisodesActivity(view, position);
                }

                @Override
                public void onItemLongClick(View view, int position) {
                    //startEpisodesActivity(view, position);
                }
            }));

            Intent intent = getIntent();
            TVShow TVShow = (br.com.adley.whatsnextseries.models.TVShow) intent.getSerializableExtra(AppConsts.TVSHOW_TRANSFER);
            mTitle = (String) intent.getSerializableExtra(AppConsts.TVSHOW_TITLE);

            //Set Title
            if(mTitle != null && !mTitle.isEmpty()){setTitleActionBar(mTitle);}

            //Load Shared Preferences
            loadConfigPreferences(this);

            //Get Show Details Data
            DetailsActivity.ProcessTVShowsDetails processTVShowsDetails = new DetailsActivity.ProcessTVShowsDetails(TVShow, isLanguageUsePtBr());
            processTVShowsDetails.execute();
        }
    }

    public void startEpisodesActivity(View view, int position){
        TVShowSeasons seasonSelected = mListSeasonRecyclerViewAdapter.getSeason(position);
        Intent intentEpisodes = new Intent(DetailsActivity.this, EpisodesActivity.class);
        intentEpisodes.putExtra(AppConsts.SHOW_ID_INTENT, seasonSelected.getTVShowId());
        intentEpisodes.putExtra(AppConsts.SEASON_NUMBER_INTENT, seasonSelected.getSeasonNumber());
        intentEpisodes.putExtra(AppConsts.SHOW_NAME_INTENT, mTVShowDetails.getName());
        startActivity(intentEpisodes);
    }

    public void setTitleActionBar(String newTitle){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setTitle(newTitle);
    }
        // Process and execute data into recycler view
        public class ProcessTVShowsDetails extends GetTVShowDetailsJsonData {
            private DetailsActivity.ProcessTVShowsDetails.ProcessData processData;

            public ProcessTVShowsDetails(TVShow show, boolean isLanguagePtBr) {
                super(show, getPosterSize(), getBackDropSize(), DetailsActivity.this, isLanguagePtBr);
            }

            public void execute() {
                // Start loading dialog
                mProgress = Utils.configureProgressDialog(getString(R.string.loading_show_title), getString(R.string.loading_seasons_description), true, true, DetailsActivity.this);
                if(mProgress != null) {
                    mProgress.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel_button_progress_dialog), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            processData.cancel(true);
                            DetailsActivity.this.finish();
                        }
                    });
                    mProgress.setCancelable(false);
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();
                }

                // Start process data (download and get)
                processData = new DetailsActivity.ProcessTVShowsDetails.ProcessData();
                processData.execute();
            }

            public class ProcessData extends DownloadJsonData {
                protected void onPostExecute(String webData) {
                    super.onPostExecute(webData);
                    mTVShowDetails = getTVShowsDetails();
                    if (mTVShowDetails == null){
                        finish();
                    }else{
                        changeFabButton();
                        //Get and Process SeasonData
                        ArrayList<Integer> seasons = mTVShowDetails.getSeasonNumberList();
                        if (seasons.size() > 0) {
                            mLastSeasonNumber = Utils.maxNumber(seasons);
                            Utils.setLayoutInvisible(mTVShowDetailsNoSeason);
                            mProgress.setMessage(getString(R.string.loading_seasons_description));
                            for (int season: seasons) {
                                DetailsActivity.ProcessSeason processSeason = new DetailsActivity.ProcessSeason(mTVShowDetails.getId(), season);
                                processSeason.execute();
                            }
                        } else {
                            mProgress.dismiss();
                            bindParams();
                            bindFABAdd();
                        }
                    }
                }
            }
        }

        // Process Season Data
        public class ProcessSeason extends GetTVShowSeasonJsonData {

            public ProcessSeason(int showId, int serieNumber) {
                super(showId, serieNumber, DetailsActivity.this);
            }

            public void execute() {
                // Start process data (download and get)
                DetailsActivity.ProcessSeason.ProcessData processData = new DetailsActivity.ProcessSeason.ProcessData();
                processData.execute();
            }

            public class ProcessData extends DownloadJsonData {
                protected void onPostExecute(String webData) {
                    super.onPostExecute(webData);
                    mTVShowSeasons.add(getTVShowSeasons());
                    if (getSeasonNumberTVShow() == mLastSeasonNumber) {
                        mTVShowSeasons.removeAll(Collections.<TVShowSeasons>singleton(null));
                        mListSeasonRecyclerViewAdapter.loadNewData(mTVShowSeasons);
                        // Prevent to got view not attached error (closes app)
                        try { mProgress.dismiss(); } catch (Exception ignored){}
                        setTitleActionBar(mTVShowDetails.getName());
                        bindParams();
                        bindFABAdd();
                    }
                }
            }
        }

    private void changeFabButton() {
        // Check if the show has already has added
        mRestoredFavorites = mSharedPref.getString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, null);
        if (mRestoredFavorites != null) {
            List<Integer> ids = Utils.convertStringToIntegerList(AppConsts.FAVORITES_SHAREDPREFERENCES_DELIMITER, mRestoredFavorites);
            if (Utils.checkItemInIntegerList(ids, mTVShowDetails.getId())) {
                mFab.setBackgroundTintList(ColorStateList.valueOf(Color
                        .parseColor(getString(R.string.red_color_string))));
                mFab.setImageDrawable(ContextCompat.getDrawable(DetailsActivity.this, R.drawable.ic_favorite_border_white_24dp));
            } else {
                mFab.setBackgroundTintList(ColorStateList.valueOf(Color
                        .parseColor(getString(R.string.green_color_string))));
                mFab.setImageDrawable(ContextCompat.getDrawable(DetailsActivity.this, R.drawable.ic_favorite_white_24dp));
            }
        }
    }

    private void changeFabButtonLastItem() {
        mFab.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor(getString(R.string.green_color_string))));
        mFab.setImageDrawable(ContextCompat.getDrawable(DetailsActivity.this, R.drawable.ic_favorite_white_24dp));
    }

    /**
     * Bind Parameters after download data.
     */
    private void bindFABAdd() {
        Utils.setLayoutVisible(mFab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRestoredFavorites = mSharedPref.getString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, null);
                mSpEditor = mSharedPref.edit();
                if (mRestoredFavorites != null) {
                    mIdShowList = Utils.convertStringToIntegerList(AppConsts.FAVORITES_SHAREDPREFERENCES_DELIMITER, mRestoredFavorites);

                    if (Utils.checkItemInIntegerList(mIdShowList, mTVShowDetails.getId())) {
                        // Delete show
                        mIdShowList = Utils.removeIntegerItemFromList(mIdShowList, mTVShowDetails.getId());
                        String idsResult = Utils.convertListToString(AppConsts.FAVORITES_SHAREDPREFERENCES_DELIMITER, mIdShowList);
                        mSpEditor.putString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, idsResult);
                        mSpEditor.apply();
                        if (idsResult == null) {
                            changeFabButtonLastItem();
                        } else {
                            changeFabButton();
                        }
                        Snackbar favoritesSnackbar = Utils.createSnackbarObject(Color.RED, getString(R.string.success_remove_show_with_name, mTVShowDetails.getName()), mTVShowDetailsView);
                        favoritesSnackbar.show();
                    } else {
                        // Add show when the list is not null
                        mIdShowList.add(mTVShowDetails.getId());
                        String idsResult = Utils.convertListToString(AppConsts.FAVORITES_SHAREDPREFERENCES_DELIMITER, mIdShowList);
                        mSpEditor.putString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, idsResult);
                        mSpEditor.apply();
                        changeFabButton();
                        Snackbar favoritesSnackbar = Utils.createSnackbarObject(Color.GREEN, getString(R.string.success_add_new_show_with_name, mTVShowDetails.getName()), mTVShowDetailsView);
                        favoritesSnackbar.show();
                    }

                } else {
                    // Add show when the list is null
                    mSpEditor.putString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, String.valueOf(mTVShowDetails.getId()));
                    mSpEditor.apply();
                    changeFabButton();
                    Snackbar favoritesSnackbar = Utils.createSnackbarObject(Color.GREEN, getString(R.string.success_add_new_show_with_name, mTVShowDetails.getName()), mTVShowDetailsView);
                    /*favoritesSnackbar.setAction("Desfazer", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });*/
                    favoritesSnackbar.show();
                }

            }
        });
    }

    private void bindParams() {
        if (mProgress.isShowing()) mProgress.dismiss();

        if (mTVShowDetails.getOriginalName() != null && mTVShowDetails.getOverview() != null) {
            if (mSynopsisTVShow != null) {
                String synopsis = mTVShowDetails.getOverview();
                if(isLanguageUsePtBr() && synopsis.isEmpty()){
                    mSynopsisTVShow.setText(getString(R.string.empty_synopsis_ptbr));
                } else {
                    mSynopsisTVShow.setText(Utils.fromHtml(synopsis));
                }
            }

            if (mRateTVShow != null) {
                if (mTVShowDetails.getVoteAverage() > 0.0 & mTVShowDetails.getVoteCount() > 0) {
                    Locale ptBr = new Locale("pt", "BR");
                    String rating_and_max = String.format(ptBr, "%.2f", mTVShowDetails.getVoteAverage());
                    mRateTVShow.setText(rating_and_max);
                } else {
                    mRateTVShow.setText(getString(R.string.abbreviation_do_not_have));
                }
            }

            // Get next episode name and date
            if (mNextEpisodeDateName != null) {
                // Pass the last season, show and activity
                if (mTVShowDetails.getSeasonNumberList().size() == 0) {
                    Utils.setLayoutVisible(mTVShowDetailsNoSeason);
                    if (mTVShowDetails.getInProduction()) {
                        mNextEpisodeDateName.setText(getString(R.string.warning_no_next_episode));
                        Utils.setLayoutInvisible(mNextEpisodePoster);
                    } else {
                        mNextEpisodeDateName.setText(getString(R.string.no_more_in_production));
                        Utils.setLayoutInvisible(mNextEpisodePoster);
                    }
                } else {
                    Utils.setNextEpisode(mTVShowSeasons.get(mTVShowSeasons.size() - 1), mTVShowDetails, DetailsActivity.this);
                    mNextEpisodeDateName.setText(mTVShowDetails.getNextEpisode());
                    if (mTVShowDetails.getNextEpisodePoster() != null) {
                        Picasso.get().load(mTVShowDetails.getNextEpisodePoster())
                                .noPlaceholder()
                                .transform(new RoundedCornersTransformation(30, 10))
                                .into(mNextEpisodePoster);
                    } else {
                        Utils.setLayoutInvisible(mNextEpisodePoster);
                    }
                }
                //mTVShowNextDateEpisode.setMovementMethod(LinkMovementMethod.getInstance());
            }

            if (mPoster != null) {
                if (mTVShowDetails.getBackdropPath() != null) { //Com plano de fundo
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { // Deitado
                        if(!mIsTablet) mPoster.setScaleType(ImageView.ScaleType.FIT_XY);
                        else mPoster.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        Picasso.get()
                                .load(mTVShowDetails.getBackdropPath())
                                .into(mPoster);
                    } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) { // Em pé
                        Picasso.get()
                                .load(mTVShowDetails.getBackdropPath())
                                .into(mPoster);
                    }
                } else if (mTVShowDetails.getPosterPath() != null && mTVShowDetails.getBackdropPath() == null) { // Sem plano de fundo e com Poster
                    mPoster.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { // Deitado
                        Picasso.get()
                                .load(mTVShowDetails.getPosterPath())
                                .into(mPoster);
                    } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        Picasso.get()
                                .load(mTVShowDetails.getPosterPath())
                                .into(mPoster);
                    }
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.error_generic_message), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // AdView Configuration
        // Remove the ad keeping the attributes
        AdView ad = (AdView) findViewById(R.id.ad_view_detail_show);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) (ad != null ? ad.getLayoutParams() : null);
        RelativeLayout parentLayout = (RelativeLayout) (ad != null ? ad.getParent() : null);
        if (parentLayout != null) {
            parentLayout.removeView(ad);
        }

        // TODO REVIEW UNCOMMENT
        // Re-initialise the ad
//        mAdView.destroy();
//        mAdView = new AdView(this);
//        mAdView.setAdSize(com.google.android.gms.ads.AdSize.SMART_BANNER);
//        mAdView.setAdUnitId(getString(R.string.banner_ad_unit_id_detail_show));
//        mAdView.setId(R.id.ad_view_detail_show);
//        mAdView.setLayoutParams(lp);
//        if (parentLayout != null) {
//            parentLayout.addView(mAdView);
//        }
//
//        // Re-fetch add and check successful load
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .addTestDevice(getString(R.string.device_id_test1))
//                .build();
//        mAdView.loadAd(adRequest);
//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                Utils.setLayoutVisible(mAdView);
//            }
//
//            @Override
//            public void onAdFailedToLoad(int i) {
//                super.onAdFailedToLoad(i);
//                Utils.setLayoutInvisible(mAdView);
//            }
//        });
        super.onConfigurationChanged(newConfig);
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

}

