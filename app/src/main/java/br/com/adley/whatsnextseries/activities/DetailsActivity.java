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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
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

    private TVShow mTVShow;
    private List<TVShowSeasons> mTVShowSeasons;
    private ProgressDialog mProgress;
    private ListSeasonRecyclerViewAdapter mListSeasonRecyclerViewAdapter;
    private String mRestoredFavorites;
    private SharedPreferences mSharedPref;
    private int mLastSeasonNumber;
    private List<Integer> mIdShowList = new ArrayList<>();
    private SharedPreferences.Editor mSpEditor;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        // Enable Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get View Elements
        mRateTVShow = (TextView) findViewById(R.id.rate_tvshow);
        mPoster = (ImageView) findViewById(R.id.background_poster);
        mNextEpisodeDateName = (TextView) findViewById(R.id.next_episode_date_name);
        mNextEpisodePoster = (ImageView) findViewById(R.id.next_episode_poster);
        mTVShowDetailsNoSeason = (TextView) findViewById(R.id.no_list_season_error);
        mSynopsisTVShow = (TextView) findViewById(R.id.synopsis_tvshow);
        mFab = (FloatingActionButton) findViewById(R.id.details_fab);
        mTVShowDetailsView = findViewById(R.id.activity_tvshow_details);

        if (!Utils.checkAppConnectionStatus(this)) {
            Utils.createSnackbarIndefine(Color.WHITE, getString(R.string.error_no_internet_connection), mTVShowDetailsView);
        } else {
            mSharedPref = getSharedPreferences(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
            mRestoredFavorites = mSharedPref.getString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, null);

            mTVShowSeasons = new ArrayList<>();
            mRecyclerViewSeason = (RecyclerView) findViewById(R.id.recycler_view_season_list);
            mRecyclerViewSeason.setLayoutManager(new LinearLayoutManager(this));
            mListSeasonRecyclerViewAdapter = new ListSeasonRecyclerViewAdapter(DetailsActivity.this, new ArrayList<TVShowSeasons>());
            mRecyclerViewSeason.setAdapter(mListSeasonRecyclerViewAdapter);

            mRecyclerViewSeason.addOnItemTouchListener(new RecyclerItemClickListener(this,
                    mRecyclerViewSeason, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //Toast.makeText(DetailsActivity.this, "Não implementado.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onItemLongClick(View view, int position) {
                    //Toast.makeText(DetailsActivity.this, "Não implementado.", Toast.LENGTH_SHORT).show();
                }
            }));

            Intent intent = getIntent();
            mTVShow = (TVShow) intent.getSerializableExtra(AppConsts.TVSHOW_TRANSFER);

            //Load Shared Preferences
            loadConfigPreferences(this);

            //Get Show Details Data
            DetailsActivity.ProcessTVShowsDetails processTVShowsDetails = new DetailsActivity.ProcessTVShowsDetails(mTVShow, isLanguageUsePtBr());
            processTVShowsDetails.execute();
        }
    }
        // Process and execute data into recycler view
        public class ProcessTVShowsDetails extends GetTVShowDetailsJsonData {
            private DetailsActivity.ProcessTVShowsDetails.ProcessData processData;

            public ProcessTVShowsDetails(TVShow show, boolean isLanguagePtBr) {
                super(show, getPosterSize(), getBackDropSize(), DetailsActivity.this, isLanguagePtBr);
            }

            public void execute() {
                // Start process data (download and get)
                processData = new DetailsActivity.ProcessTVShowsDetails.ProcessData();
                processData.execute();

                // Start loading dialog
                mProgress = Utils.configureProgressDialog(getString(R.string.loading_show_title), getString(R.string.loading_seasons_description), true, true, DetailsActivity.this);
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
                        mProgress.dismiss();
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
                        /*favoritesSnackbar.setAction(DetailsActivity.this.getString(R.string.undo_snackbar), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Add show when the list is not null
                                mIdShowList.add(mTVShowDetails.getId());
                                String idsResult = Utils.convertListToString(AppConsts.FAVORITES_SHAREDPREFERENCES_DELIMITER, mIdShowList);
                                DetailsActivity.this.mSpEditor.putString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, idsResult);
                                DetailsActivity.this.mSpEditor.apply();
                                changeFabButton();
                                Utils.createSnackbar(Color.GREEN, getString(R.string.success_add_new_show), mTVShowDetailsView);
                            }
                        });
                        favoritesSnackbar.setActionTextColor(Color.YELLOW);*/
                        favoritesSnackbar.show();
                    } else {
                        // Add show when the list is not null
                        mIdShowList.add(mTVShowDetails.getId());
                        String idsResult = Utils.convertListToString(AppConsts.FAVORITES_SHAREDPREFERENCES_DELIMITER, mIdShowList);
                        mSpEditor.putString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, idsResult);
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
        // Checks the orientation of the screen
        if (mTVShowDetails.getBackdropPath() == null) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mPoster.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mPoster.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }

        if (mProgress.isShowing()) mProgress.dismiss();
        if (mTVShowDetails.getOriginalName() != null && mTVShowDetails.getOverview() != null) {
            if (mTitle != null) {
                if (mTVShowDetails.getName().equals(mTVShowDetails.getOriginalName())) {
                    mTitle = (mTVShowDetails.getName());
                } else {
                    mTitle = (getString(R.string.title_holder_text, mTVShowDetails.getName(), mTVShowDetails.getOriginalName()));
                }
                setTitle(mTitle);
            }

            if (mSynopsisTVShow != null) {
                mSynopsisTVShow.setText(Utils.fromHtml(mTVShowDetails.getOverview()));
            }

            if (mRateTVShow != null) {
                if (mTVShowDetails.getVoteAverage() > 0.0 & mTVShowDetails.getVoteCount() > 0) {
                    Locale ptBr = new Locale("pt", "BR");
                    String rating_and_max = String.format(ptBr, "%.2f", mTVShowDetails.getVoteAverage());
                    mRateTVShow.setText(rating_and_max);
                } else {
                    mRateTVShow.setText(getString(R.string.abbreviation_do_not_have));
                }
            } else {
                mRateTVShow.setText(getString(R.string.error_generic_message));
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
                        Picasso.with(this).load(mTVShowDetails.getNextEpisodePoster())
                                .noPlaceholder()
                                .transform(new RoundedCornersTransformation(30, 10))
                                .into(mNextEpisodePoster);
                    } else {
                        Utils.setLayoutInvisible(mNextEpisodePoster);
                        if (mNextEpisodePoster != null) {
                        }
                    }
                }
                //mTVShowNextDateEpisode.setMovementMethod(LinkMovementMethod.getInstance());
            }

            if (mPoster != null) {
                DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
                int width = displayMetrics.widthPixels;
                int height = displayMetrics.heightPixels;
                if (mTVShowDetails.getBackdropPath() != null) { //Com plano de fundo
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { // Deitado
                        mPoster.setMaxHeight(width / 2);
                        mPoster.setMinimumHeight(width / 2);
                        mPoster.setScaleType(ImageView.ScaleType.FIT_XY);
                        Picasso.with(DetailsActivity.this)
                                .load(mTVShowDetails.getBackdropPath())
                                .into(mPoster);
                    } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) { // Em pé
                        Picasso.with(DetailsActivity.this)
                                .load(mTVShowDetails.getBackdropPath())
                                .resize(width / 3, height / 3)
                                .into(mPoster);
                    }
                } else if (mTVShowDetails.getPosterPath() != null && mTVShowDetails.getBackdropPath() == null) { // Sem plano de fundo e com Poster
                    mPoster.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { // Deitado
                        Picasso.with(DetailsActivity.this)
                                .load(mTVShowDetails.getPosterPath())
                                .resize(height / 3, width / 3)
                                .into(mPoster);
                    } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        Picasso.with(DetailsActivity.this)
                                .load(mTVShowDetails.getPosterPath())
                                .resize(width / 3, height / 3)
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

    }
}

