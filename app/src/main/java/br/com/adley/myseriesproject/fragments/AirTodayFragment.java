package br.com.adley.myseriesproject.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.activities.DetailsTVShowActivity;
import br.com.adley.myseriesproject.activities.HomeActivity;
import br.com.adley.myseriesproject.library.AppConsts;
import br.com.adley.myseriesproject.library.RecyclerItemClickListener;
import br.com.adley.myseriesproject.library.Utils;
import br.com.adley.myseriesproject.library.enums.DownloadStatus;
import br.com.adley.myseriesproject.models.TVShow;
import br.com.adley.myseriesproject.themoviedb.adapters.AiringTodayRecyclerViewAdapter;
import br.com.adley.myseriesproject.themoviedb.service.GetAiringTodayJsonData;

/**
 * Created by Adley.Damaceno on 21/07/2016.
 * Class to control data for shows airing today
 */
public class AirTodayFragment extends Fragment {

    private View airTodayFragment;
    private RecyclerView mRecyclerView;
    private ImageView mLoadAirToday;
    private AiringTodayRecyclerViewAdapter mAiringTodayRecyclerViewAdapter;
    private static final String LOG_TAG = "AirTodayFragment";
    private View mNoInternetConnection;
    private View mLoadingTodayLayout;
    private ImageView mLoadAirTodayNoInternet;
    private boolean mNotFirstRun = false;
    private View mProgressBarHomeLayout;
    private ProgressBar mProgressBarHome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        airTodayFragment = inflater.inflate(R.layout.fragment_airtoday, container, false);
        mLoadingTodayLayout = airTodayFragment.findViewById(R.id.load_airing_today_layout);
        mNoInternetConnection = airTodayFragment.findViewById(R.id.no_internet_connection);
        mProgressBarHomeLayout = airTodayFragment.findViewById(R.id.loading_progress_airing_today_layout);
        mProgressBarHome = (ProgressBar) airTodayFragment.findViewById(R.id.shared_progressbar_home);
        //mProgressBarHome.setIndeterminate(true);
        mLoadAirTodayNoInternet = (ImageView) airTodayFragment.findViewById(R.id.refresh_button_no_internet);
        Activity activity = getActivity();
        if (activity instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) activity;
            homeActivity.loadConfigPreferences(getContext());
            if (homeActivity.autoLoadAirToday()){
                Utils.setLayoutInvisible(mLoadingTodayLayout);
                executeAirTodayList(false);
            }
        }
        mLoadAirTodayNoInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setLayoutInvisible(mLoadingTodayLayout);
                if (!Utils.checkAppConnectionStatus(getContext())) {
                    Snackbar.make(mNoInternetConnection, getActivity().getString(R.string.cant_load_air_today), Snackbar.LENGTH_LONG).show();
                } else {
                    executeAirTodayList(mNotFirstRun);
                }
            }
        });

        mLoadAirToday = (ImageView) airTodayFragment.findViewById(R.id.load_airing_today_button);
        mLoadAirToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setLayoutInvisible(mLoadingTodayLayout);
                executeAirTodayList(mNotFirstRun);
            }
        });
        return airTodayFragment;
    }

    public void executeAirTodayList(boolean isRefreshing) {
        Activity activity = getActivity();
        String posterSize = AppConsts.POSTER_DEFAULT_SIZE;
        String backdropSize = AppConsts.BACKDROP_DEFAULT_SIZE;
        boolean isLanguageUsePtBr = false;
        if (activity instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) activity;
            homeActivity.loadConfigPreferences(getContext());
            posterSize = homeActivity.getPosterSize();
            backdropSize = homeActivity.getBackDropSize();
            isLanguageUsePtBr = homeActivity.isLanguageUsePtBr();
        }
        mAiringTodayRecyclerViewAdapter = new AiringTodayRecyclerViewAdapter(getContext(), new ArrayList<TVShow>());
        mRecyclerView = (RecyclerView) airTodayFragment.findViewById(R.id.recycler_view_airing_today_list);
        mRecyclerView.setAdapter(mAiringTodayRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mNotFirstRun = true;

        if (!isRefreshing) {
            // Create the touch for the recyclerview list
            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //Creates and configure intent to call tv show details activity
                    Intent intent = new Intent(getContext(), DetailsTVShowActivity.class);
                    intent.putExtra(AppConsts.TVSHOW_TRANSFER, mAiringTodayRecyclerViewAdapter.getTVShow(position));
                    startActivity(intent);
                }

                @Override
                public void onItemLongClick(View view, int position) {
                    //Creates and configure intent to call tv show details activity
                    Intent intent = new Intent(getContext(), DetailsTVShowActivity.class);
                    intent.putExtra(AppConsts.TVSHOW_TRANSFER, mAiringTodayRecyclerViewAdapter.getTVShow(position));
                    startActivity(intent);
                }
            }
            ));
        }
        if (!Utils.checkAppConnectionStatus(getContext())) {
            Snackbar snackbarNoInternet = Snackbar
                    .make(mNoInternetConnection, getActivity().getString(R.string.cant_load_air_today), Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            executeAirTodayList(mNotFirstRun);
                        }
                    });
            snackbarNoInternet.setActionTextColor(Color.RED);
            snackbarNoInternet.show();
            //if (mNoInternetConnection != null) Utils.setLayoutVisible(mNoInternetConnection);
            if (mRecyclerView != null) Utils.setLayoutInvisible(mRecyclerView);
            if (mLoadingTodayLayout != null) Utils.setLayoutVisible(mLoadingTodayLayout);

        } else {
            // Set Layout Visible
            Utils.setLayoutVisible(mRecyclerView);

            // Create and generate the recycler view for list of results
            mRecyclerView = (RecyclerView) airTodayFragment.findViewById(R.id.recycler_view_airing_today_list);
            if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            }
            else{
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
            }
            ProcessTVShowsAiringToday processTVShowsAiringToday = new ProcessTVShowsAiringToday(getContext(), isLanguageUsePtBr, posterSize, backdropSize);
            processTVShowsAiringToday.execute();
        }
    }

    // Process and execute data into recycler view
    public class ProcessTVShowsAiringToday extends GetAiringTodayJsonData {

        public ProcessTVShowsAiringToday(Context context, boolean isLanguageUsePtBr, String posterSize, String backDropSize) {
            super(context, isLanguageUsePtBr, posterSize, backDropSize);
        }

        public void execute() {
            Utils.setLayoutVisible(mProgressBarHomeLayout);
            ProcessData processData = new ProcessData();
            processData.execute();
        }

        public class ProcessData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                Utils.setLayoutInvisible(mProgressBarHomeLayout);
                if (getDownloadStatus() != DownloadStatus.OK || getTVShows() == null) {
                    Utils.setLayoutVisible(mLoadingTodayLayout);
                    Snackbar snackbarNoInternet = Snackbar
                            .make(mNoInternetConnection, getActivity().getString(R.string.cant_load_air_today), Snackbar.LENGTH_LONG)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    executeAirTodayList(mNotFirstRun);
                                }
                            });
                    snackbarNoInternet.setActionTextColor(Color.RED);
                    snackbarNoInternet.show();
                } else {
                    mAiringTodayRecyclerViewAdapter.loadNewData(getTVShows());
                }
            }
        }
    }
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if(mRecyclerView != null)mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if(mRecyclerView != null)mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
    }
}
