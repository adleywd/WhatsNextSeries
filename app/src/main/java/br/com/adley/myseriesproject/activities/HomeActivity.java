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
    private final String PREFIX_IMG_DIMENSION_FAVORITES = "w92";
    private View mNoInternetConnection;
    private String mRestoredFavorites;
    private ImageButton mNoFavsSearchButton;
    private View mNoFavsSearchLayout;
    private SwipeRefreshLayout mSwipeRefreshLayoutHome;

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
        executeHomeContent(false);
        mSwipeRefreshLayoutHome.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayoutHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Utils.setLayoutVisible(findViewById(R.id.loading_panel));
                if(mRecyclerView != null) {
                    if (!Utils.checkAppConnectionStatus(HomeActivity.this)) {
                        Utils.setLayoutInvisible(mRecyclerView);
                    } else {
                        Utils.setLayoutVisible(mRecyclerView);
                    }
                }
                executeHomeContent(true);
                mSwipeRefreshLayoutHome.setRefreshing(false);
            }
        });

    }

    @Override
    public void onRestart() {
        super.onRestart();
        SharedPreferences sharedPref = getSharedPreferences(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
        String restartRestoredFavorites = sharedPref.getString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, null);
        if(restartRestoredFavorites != null){
            if (!restartRestoredFavorites .equals(mRestoredFavorites)){
                finish();
                startActivity(getIntent());
            }
        }else{
            finish();
            startActivity(getIntent());
        }
    }

    private void executeHomeContent(boolean isSwipeRefresh){
        SharedPreferences sharedPref = getSharedPreferences(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
        mRestoredFavorites = sharedPref.getString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, null);

        if (mRestoredFavorites != null) {
            List<Integer> ids = Utils.convertStringToIntegerList(AppConsts.FAVORITES_SHAREDPREFERENCES_DELIMITER, mRestoredFavorites);
            mIdShowList = ids;
        }

        if (Utils.checkAppConnectionStatus(this)) {
            if(mRestoredFavorites != null){
                Utils.setLayoutInvisible(mNoFavsSearchLayout);
            }else{
                Utils.setLayoutVisible(mNoFavsSearchLayout);
            }

            mShowListCount = 0;
            Utils.setLayoutInvisible(mNoInternetConnection);

            mFavoritesRecyclerViewAdapter = new FavoritesRecyclerViewAdapter(this, new ArrayList<TVShowDetails>());
            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_favorites_list);

            if (mRecyclerView != null) {
                mRecyclerView.setAdapter(mFavoritesRecyclerViewAdapter);
            }
            mRecyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
            mRecyclerView.setHasFixedSize(true);
            if(!isSwipeRefresh) {
                // Create the touch for the recyclerview list
                mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Creates and configure intent to call tv show details activity
                        Intent intent = new Intent(HomeActivity.this, DetailsTVShowActivity.class);
                        intent.putExtra(TVSHOW_TRANSFER, mFavoritesRecyclerViewAdapter.getTVShow(position));
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                    }
                }));
            }
            //GetList Show Details Data
            if(mIdShowList.size() == 0){
                Utils.setLayoutInvisible(findViewById(R.id.loading_panel));
            }
            mNoFavsSearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, SearchTVShowActivity.class);
                    startActivity(intent);
                }
            });
            for (int idShow : mIdShowList) {
                ProcessFavoritesTVShowsDetails processFavoritesTVShowsDetails = new ProcessFavoritesTVShowsDetails(idShow, PREFIX_IMG_DIMENSION_FAVORITES);
                processFavoritesTVShowsDetails.execute();
            }
        } else {
            Utils.setLayoutInvisible(findViewById(R.id.loading_panel));
            Utils.setLayoutVisible(mNoInternetConnection);
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
            /*mProgress.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Utils.setLayoutInvisible(findViewById(R.id.loadingPanel));
                    //dialog.dismiss();
                    processData.cancel(true);
                }
            });
            mProgress.show();*/
        }

        public class ProcessData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                //Get and Process SeasonData
                mShowListCount++;
                if(getTVShowsDetails().getNumberOfSeasons() > 0) {
                    ProcessSeason processSeason = new ProcessSeason(getTVShowsDetails(), getTVShowsDetails().getNumberOfSeasons());
                    processSeason.execute();
                }else{
                    if(getTVShowsDetails().getInProduction())
                        getTVShowsDetails().setNextEpisode(getString(R.string.warning_no_next_episode));
                    else{
                        getTVShowsDetails().setNextEpisode(getString(R.string.no_more_in_production));
                    }
                    mFavoritesRecyclerViewAdapter.loadNewData(getTVShowsDetails());
                }
                if (mShowListCount >= mIdShowList.size()) {
                    Utils.setLayoutInvisible(findViewById(R.id.loading_panel));
                    //mProgress.dismiss();
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
                mFavoritesRecyclerViewAdapter.loadNewData(getTVShowDetails());
            }
        }
    }
}


