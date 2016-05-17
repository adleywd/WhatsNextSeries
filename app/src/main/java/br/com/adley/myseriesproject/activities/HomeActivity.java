package br.com.adley.myseriesproject.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.library.AppConsts;
import br.com.adley.myseriesproject.library.RecyclerItemClickListener;
import br.com.adley.myseriesproject.library.Utils;
import br.com.adley.myseriesproject.models.TVShowDetails;
import br.com.adley.myseriesproject.themoviedb.adapters.FavoritesRecyclerViewAdapter;
import br.com.adley.myseriesproject.themoviedb.service.GetTVShowDetailsJsonData;
import br.com.adley.myseriesproject.themoviedb.service.GetTVShowSeasonJsonData;

public class HomeActivity extends BaseActivity {
    private List<Integer> mIdShowList;
    private ProgressDialog mProgress;
    private int mShowListCount;
    private RecyclerView mRecyclerView;
    private FavoritesRecyclerViewAdapter mFavoritesRecyclerViewAdapter;
    private View mNoInternetConnection;
    private String mRestoredFavorites;
    private ImageButton mNoFavsSearchButton;
    private View mNoFavsSearchLayout;
    private SwipeRefreshLayout mSwipeRefreshLayoutHome;
    private List<TVShowDetails> mTVShowDetailsList;
    private View mProgressBarHomeLayout;
    private ProgressBar mProgressBarHome;
    private int mProgressBarCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        activateToolbarWithNavigationView(HomeActivity.this);
        mIdShowList = new ArrayList<>();
        mNoInternetConnection = findViewById(R.id.no_internet_connection);
        mNoFavsSearchButton = (ImageButton) findViewById(R.id.no_favs_home_imagebutton_search);
        mNoFavsSearchLayout = findViewById(R.id.no_favs_home_layout);
        mSwipeRefreshLayoutHome = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_home);
        mProgressBarHomeLayout = findViewById(R.id.loading_panel_home);
        mProgressBarHome = (ProgressBar) findViewById(R.id.shared_progressbar_home);
        executeHomeContent(false);


    }

    @Override
    public void onRestart() {
        super.onRestart();
        SharedPreferences sharedPref = getSharedPreferences(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
        String restartRestoredFavorites = sharedPref.getString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, null);
        if (restartRestoredFavorites != null) {
            if (!restartRestoredFavorites.equals(mRestoredFavorites)) {
                finish();
                startActivity(getIntent());
            }
        } else {
            finish();
            startActivity(getIntent());
        }
    }

    private void executeHomeContent(boolean isSwipeRefresh) {
        SharedPreferences sharedPref = getSharedPreferences(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
        mRestoredFavorites = sharedPref.getString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, null);
        // Clear the favorites show list (to not duplicate)
        mTVShowDetailsList = new ArrayList<>();

        if (mRestoredFavorites != null) {
            mIdShowList = Utils.convertStringToIntegerList(AppConsts.FAVORITES_SHAREDPREFERENCES_DELIMITER, mRestoredFavorites);
        }

        // Check connection Status
        if (Utils.checkAppConnectionStatus(this)) {
            Utils.setLayoutInvisible(mNoInternetConnection);

            if (mRestoredFavorites != null) {
                Utils.setLayoutInvisible(mNoFavsSearchLayout);
            } else {
                Utils.setLayoutVisible(mNoFavsSearchLayout);
                createRefreshListener();
            }

            mShowListCount = 0;

            mFavoritesRecyclerViewAdapter = new FavoritesRecyclerViewAdapter(this, new ArrayList<TVShowDetails>());
            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_favorites_list);

            if (mRecyclerView != null) {
                mRecyclerView.setAdapter(mFavoritesRecyclerViewAdapter);
            }
            mRecyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
            mRecyclerView.setHasFixedSize(true);

            // Check if the loading don't come to a swipe refresh
            if (!isSwipeRefresh) {
                // Create the touch for the recyclerview list
                mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Creates and configure intent to call tv show details activity
                        Intent intent = new Intent(HomeActivity.this, DetailsTVShowActivity.class);
                        intent.putExtra(AppConsts.TVSHOW_TRANSFER, mFavoritesRecyclerViewAdapter.getTVShow(position));
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                    }
                }));
            }
            //GetList Show Details Data
            if (mIdShowList.size() == 0) {
                Utils.setLayoutInvisible(mProgressBarHomeLayout);
                if (mSwipeRefreshLayoutHome != null) mSwipeRefreshLayoutHome.setRefreshing(false);
            }
            mNoFavsSearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, SearchTVShowActivity.class);
                    startActivity(intent);
                }
            });
            for (int idShow : mIdShowList) {
                ProcessFavoritesTVShowsDetails processFavoritesTVShowsDetails = new ProcessFavoritesTVShowsDetails(idShow, AppConsts.PREFIX_IMG_DIMENSION_FAVORITES);
                processFavoritesTVShowsDetails.execute();
            }
        } else {
            Utils.setLayoutInvisible(mProgressBarHomeLayout);
            if (mSwipeRefreshLayoutHome != null) mSwipeRefreshLayoutHome.setRefreshing(false);
            if (mNoFavsSearchLayout != null) Utils.setLayoutInvisible(mNoFavsSearchLayout);

            Utils.setLayoutVisible(mNoInternetConnection);
            createRefreshListener();

            Snackbar.make(mNoInternetConnection, getString(R.string.error_no_internet_connection), Snackbar.LENGTH_LONG).show();
        }
    }

    // Process and execute data into recycler view
    public class ProcessFavoritesTVShowsDetails extends GetTVShowDetailsJsonData {
        private ProcessData processData;

        public ProcessFavoritesTVShowsDetails(int idShow, String prefixImg) {
            super(idShow, prefixImg, HomeActivity.this);
        }

        public void execute() {
            // Start process data (download and get)
            processData = new ProcessData();
            processData.execute();
        }

        public class ProcessData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                //Get and Process SeasonData
                mShowListCount++;
                if (getTVShowsDetails().getNumberOfSeasons() > 0) {
                    ProcessSeason processSeason = new ProcessSeason(getTVShowsDetails(), getTVShowsDetails().getNumberOfSeasons());
                    processSeason.execute();
                } else {
                    if (getTVShowsDetails().getInProduction())
                        getTVShowsDetails().setNextEpisode(getString(R.string.warning_no_next_episode));
                    else {
                        getTVShowsDetails().setNextEpisode(getString(R.string.no_more_in_production));
                    }
                    // Add show to list of Favorites shows.
                    mTVShowDetailsList.add(getTVShowsDetails());

                    mProgressBarCount += mProgressBarHome.getMax() / mIdShowList.size();
                    mProgressBarHome.setProgress(mProgressBarCount);

                    // If the last show in restored items, load list.
                    if (mIdShowList.size() == mTVShowDetailsList.size()) {

                        Utils.setLayoutInvisible(mProgressBarHomeLayout);
                        if (mSwipeRefreshLayoutHome != null)
                            mSwipeRefreshLayoutHome.setRefreshing(false);

                        mFavoritesRecyclerViewAdapter.loadNewData(mTVShowDetailsList);
                        createRefreshListener();
                    }
                }
            }
        }
    }

    // Process Season Data
    public class ProcessSeason extends GetTVShowSeasonJsonData {

        public ProcessSeason(TVShowDetails show, int showNumber) {
            super(show, showNumber, HomeActivity.this);
        }

        public void execute() {
            // Start process data (download and get)
            ProcessData processData = new ProcessData();
            processData.execute();
        }

        public class ProcessData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                // Set next episode
                Utils.setNextEpisode(getTVShowSeasons(), getTVShowDetails(), HomeActivity.this);
                mTVShowDetailsList.add(getTVShowDetails());
                mProgressBarCount += mProgressBarHome.getMax() / mIdShowList.size();
                mProgressBarHome.setProgress(mProgressBarCount);
                if (getSeasonNumberTVShow() == getTVShowDetails().getNumberOfSeasons() &&
                        mIdShowList.size() == mTVShowDetailsList.size()) {

                    Utils.setLayoutInvisible(mProgressBarHomeLayout);
                    if (mSwipeRefreshLayoutHome != null)
                        mSwipeRefreshLayoutHome.setRefreshing(false);

                    mFavoritesRecyclerViewAdapter.loadNewData(mTVShowDetailsList);
                    createRefreshListener();
                }
            }
        }
    }

    private void createRefreshListener() {
        mSwipeRefreshLayoutHome.setColorSchemeResources(android.R.color.holo_purple,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_bright);
        mSwipeRefreshLayoutHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Utils.setLayoutVisible(findViewById(R.id.loading_panel));
                if (mRecyclerView != null) {
                    if (!Utils.checkAppConnectionStatus(HomeActivity.this)) {
                        Utils.setLayoutInvisible(mRecyclerView);
                    } else {
                        Utils.setLayoutVisible(mRecyclerView);
                    }
                }
                executeHomeContent(true);
            }
        });
    }
}


