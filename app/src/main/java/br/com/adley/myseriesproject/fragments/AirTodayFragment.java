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
import android.widget.TextView;

import java.util.ArrayList;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.activities.AppPreferences;
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
    private View mProgressBarHomeLayout;
    private ProgressBar mProgressBarHome;
    private boolean mIsTablet = false;
    private boolean mIsRecyclerViewBind = false;
    private TextView mAutoLoadAirTodayLink;
    private int mPage = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        airTodayFragment = inflater.inflate(R.layout.fragment_airtoday, container, false);
        mLoadingTodayLayout = airTodayFragment.findViewById(R.id.load_airing_today_layout);
        mNoInternetConnection = airTodayFragment.findViewById(R.id.no_internet_connection);
        mProgressBarHomeLayout = airTodayFragment.findViewById(R.id.loading_progress_airing_today_layout);
        mProgressBarHome = (ProgressBar) airTodayFragment.findViewById(R.id.shared_progressbar_home);
        mProgressBarHome.setIndeterminate(true);

        mAutoLoadAirTodayLink = (TextView) airTodayFragment.findViewById(R.id.auto_load_airtoday_link);
        if(mAutoLoadAirTodayLink != null ){
            mAutoLoadAirTodayLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent aboutAppLink = new Intent(getActivity(), AppPreferences.class);
                    getActivity().startActivity(aboutAppLink);
                }
            });
        }

        mLoadAirTodayNoInternet = (ImageView) airTodayFragment.findViewById(R.id.refresh_button_no_internet);
        mIsTablet = Utils.isTablet(getContext());
        Activity activity = getActivity();
        if (activity instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) activity;
            homeActivity.loadConfigPreferences(getContext());
            if (homeActivity.autoLoadAirToday()) {
                Utils.setLayoutInvisible(mLoadingTodayLayout);
                Utils.setLayoutInvisible(mAutoLoadAirTodayLink);
                executeAirTodayList();
            }
        }
        mLoadAirTodayNoInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setLayoutInvisible(mLoadingTodayLayout);
                Utils.setLayoutInvisible(mAutoLoadAirTodayLink);
                if (!Utils.checkAppConnectionStatus(getContext())) {
                    Snackbar.make(mNoInternetConnection, getActivity().getString(R.string.cant_load_air_today), Snackbar.LENGTH_LONG).show();
                } else {
                    executeAirTodayList();
                }
            }
        });

        mLoadAirToday = (ImageView) airTodayFragment.findViewById(R.id.load_airing_today_button);
        mLoadAirToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setLayoutInvisible(mLoadingTodayLayout);
                Utils.setLayoutInvisible(mAutoLoadAirTodayLink);
                executeAirTodayList();
            }
        });
        return airTodayFragment;
    }

    public void executeAirTodayList() {
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

        // Check if recycler view was bind, if not, it'll bind
        if (!mIsRecyclerViewBind) {
            bindRecyclerView();
        }

        if (!Utils.checkAppConnectionStatus(getContext())) {
            Snackbar snackbarNoInternet = Snackbar
                    .make(mNoInternetConnection, getActivity().getString(R.string.cant_load_air_today), Snackbar.LENGTH_LONG)
                    .setAction(getActivity().getString(R.string.retry_snackbar), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            executeAirTodayList();
                        }
                    });
            snackbarNoInternet.setActionTextColor(Color.RED);
            snackbarNoInternet.show();
            //if (mNoInternetConnection != null) Utils.setLayoutVisible(mNoInternetConnection);
            if (mRecyclerView != null) Utils.setLayoutInvisible(mRecyclerView);
            if (mLoadingTodayLayout != null) Utils.setLayoutVisible(mLoadingTodayLayout);
            if (mAutoLoadAirTodayLink != null) Utils.setLayoutVisible(mAutoLoadAirTodayLink);

        } else {
            // Set Layout Visible
            Utils.setLayoutVisible(mRecyclerView);

            // Create and generate the recycler view for list of results
            mRecyclerView = (RecyclerView) airTodayFragment.findViewById(R.id.recycler_view_airing_today_list);
            if (mIsTablet) {
                if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), AppConsts.AIRTODAY_PORTRAIT_TABLET));
                } else {
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), AppConsts.AIRTODAY_LANDSCAPE_TABLET));
                }
            } else {
                if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), AppConsts.AIRTODAY_PORTRAIT_PHONE));
                } else {
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), AppConsts.AIRTODAY_LANDSCAPE_PHONE));
                }
            }
            ProcessTVShowsAiringToday processTVShowsAiringToday = new ProcessTVShowsAiringToday(getContext(), isLanguageUsePtBr, posterSize, backdropSize, mPage);
            processTVShowsAiringToday.execute();
        }
    }

    // Process and execute data into recycler view
    public class ProcessTVShowsAiringToday extends GetAiringTodayJsonData {

        public ProcessTVShowsAiringToday(Context context, boolean isLanguageUsePtBr, String posterSize, String backDropSize, int page) {
            super(context, isLanguageUsePtBr, posterSize, backDropSize, page);
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
                    Utils.setLayoutVisible(mAutoLoadAirTodayLink);
                    Snackbar snackbarNoInternet = Snackbar
                            .make(mNoInternetConnection, getActivity().getString(R.string.cant_load_air_today), Snackbar.LENGTH_LONG)
                            .setAction(getActivity().getString(R.string.retry_snackbar), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    executeAirTodayList();
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

    // Create the touch for the recyclerview list
    public void bindRecyclerView() {
        if (mRecyclerView != null) {
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
            mIsRecyclerViewBind = true;
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mRecyclerView != null) {
            if (mIsTablet) {
                if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), AppConsts.AIRTODAY_PORTRAIT_TABLET));
                } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), AppConsts.AIRTODAY_LANDSCAPE_TABLET));
                }
            } else {
                if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), AppConsts.AIRTODAY_PORTRAIT_PHONE));
                } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), AppConsts.AIRTODAY_LANDSCAPE_PHONE));
                }
            }
        }
    }
}
