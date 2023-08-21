package br.com.adley.whatsnextseries.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.adley.whatsnextseries.R;
import br.com.adley.whatsnextseries.activities.AppPreferences;
import br.com.adley.whatsnextseries.activities.DetailsActivity;
import br.com.adley.whatsnextseries.activities.MainActivity;
import br.com.adley.whatsnextseries.adapters.recyclerview.AiringTodayRecyclerViewAdapter;
import br.com.adley.whatsnextseries.library.AppConsts;
import br.com.adley.whatsnextseries.library.RecyclerItemClickListener;
import br.com.adley.whatsnextseries.library.Utils;
import br.com.adley.whatsnextseries.library.enums.DownloadStatus;
import br.com.adley.whatsnextseries.models.TVShow;
import br.com.adley.whatsnextseries.service.GetAiringTodayJsonData;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

/**
 * Created by Adley.Damaceno on 21/07/2016.
 * Class to control data for shows airing today
 */
public class AirTodayFragment extends Fragment {

    public static AirTodayFragment newInstance(){
        return new AirTodayFragment();
    }

    private View airTodayFragment;
    private RecyclerView mRecyclerView;
    private ImageView mLoadAirToday;
    private AiringTodayRecyclerViewAdapter mAiringTodayRecyclerViewAdapter;
    private static final String LOG_TAG = "AirTodayFragment";
    private View mNoInternetConnection;
    private View mLoadingTodayLayout;
    private View mProgressBarHomeLayout;
    private ProgressBar mProgressBarHome;
    private boolean mIsTablet = false;
    //private boolean mIsRecyclerViewBind = false;
    private TextView mAutoLoadAirTodayLink;
    private ProgressBar mLoadMoreItensLayout;
    private int mPage = 1;
    private int mTotalPages = 1;
    private GridLayoutManager mLayoutManager;
    private boolean mIsLoadMore = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private List<TVShow> mTVShowList;
    private ImageView mRefreshButtonNoConnection;
    private AlertDialog mAlertDialog;
    private View mAiringTodayMainLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mTVShowList = new ArrayList<>();
        airTodayFragment = inflater.inflate(R.layout.fragment_airtoday, container, false);
        mLoadingTodayLayout = airTodayFragment.findViewById(R.id.load_airing_today_layout);
        mNoInternetConnection = airTodayFragment.findViewById(R.id.no_internet_connection);
        mRefreshButtonNoConnection = (ImageView) airTodayFragment.findViewById(R.id.refresh_button_no_internet);
        mProgressBarHomeLayout = airTodayFragment.findViewById(R.id.loading_progress_airing_today_layout);
        mLoadMoreItensLayout = (ProgressBar) airTodayFragment.findViewById(R.id.load_more_air_today_progressbar);
        mProgressBarHome = (ProgressBar) airTodayFragment.findViewById(R.id.shared_progressbar_home);
        mProgressBarHome.setIndeterminate(true);

        mAiringTodayMainLayout = airTodayFragment.findViewById(R.id.airing_today_main_layout);

        mAutoLoadAirTodayLink = (TextView) airTodayFragment.findViewById(R.id.auto_load_airtoday_link);
        if (mAutoLoadAirTodayLink != null) {
            mAutoLoadAirTodayLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent aboutAppLink = new Intent(getActivity(), AppPreferences.class);
                    getActivity().startActivity(aboutAppLink);
                    Utils.setLayoutInvisible(mLoadingTodayLayout);
                    Utils.setLayoutInvisible(mAutoLoadAirTodayLink);
                    Utils.setLayoutVisible(mProgressBarHomeLayout);
                    executeAirTodayList();
                }
            });
        }

        // Create and generate the recycler view for list of results
        mAiringTodayRecyclerViewAdapter = new AiringTodayRecyclerViewAdapter(getContext(), new ArrayList<TVShow>());
        mRecyclerView = (RecyclerView) airTodayFragment.findViewById(R.id.recycler_view_airing_today_list);
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(mAiringTodayRecyclerViewAdapter);
        mRecyclerView.setAdapter(animationAdapter);
        mIsTablet = Utils.isTablet(getContext());
        if (mIsTablet) {
            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mLayoutManager = new GridLayoutManager(getContext(), AppConsts.AIRTODAY_PORTRAIT_TABLET);
            } else {
                mLayoutManager = new GridLayoutManager(getContext(), AppConsts.AIRTODAY_LANDSCAPE_TABLET);
            }
        } else {
            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mLayoutManager = new GridLayoutManager(getContext(), AppConsts.AIRTODAY_PORTRAIT_PHONE);
            } else {
                mLayoutManager = new GridLayoutManager(getContext(), AppConsts.AIRTODAY_LANDSCAPE_PHONE);
            }
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (mIsLoadMore) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            mIsLoadMore = false;
                            mPage++;
                            if (mPage <= mTotalPages) {
                                Utils.setLayoutVisible(mLoadMoreItensLayout);
                                executeAirTodayList();
                            }
                        }
                    }
                }
            }
        });
        mRefreshButtonNoConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setLayoutInvisible(mLoadingTodayLayout);
                Utils.setLayoutInvisible(mAutoLoadAirTodayLink);
                if (!Utils.checkAppConnectionStatus(getContext())) {
                    Snackbar.make(mNoInternetConnection, getActivity().getString(R.string.cant_load_air_today), Snackbar.LENGTH_LONG).show();
                } else {
                    Utils.setLayoutVisible(mProgressBarHomeLayout);
                    executeAirTodayList();
                }
            }
        });
        bindRecyclerView();

        mLoadAirToday = (ImageView) airTodayFragment.findViewById(R.id.load_airing_today_button);
        mLoadAirToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setLayoutInvisible(mLoadingTodayLayout);
                Utils.setLayoutInvisible(mAutoLoadAirTodayLink);
                Utils.setLayoutVisible(mProgressBarHomeLayout);
                executeAirTodayList();
            }
        });
        return airTodayFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!Utils.checkAppConnectionStatus(getContext())) {
            Utils.setLayoutInvisible(mLoadingTodayLayout);
            Utils.setLayoutInvisible(mAutoLoadAirTodayLink);
            Utils.setLayoutVisible(mNoInternetConnection);
        }else{
            Activity activity = getActivity();
            if (activity instanceof MainActivity) {
                MainActivity hainActivity = (MainActivity) activity;
                hainActivity.loadConfigPreferences(getContext());
                if (hainActivity.autoLoadAirToday()) {
                    Utils.setLayoutInvisible(mLoadingTodayLayout);
                    Utils.setLayoutInvisible(mAutoLoadAirTodayLink);
                    Utils.setLayoutVisible(mProgressBarHomeLayout);
                    executeAirTodayList();
                }
            }
        }
    }

    public void executeAirTodayList() {
        Activity activity = getActivity();
        String posterSize = AppConsts.POSTER_DEFAULT_SIZE;
        String backdropSize = AppConsts.BACKDROP_DEFAULT_SIZE;
        boolean isLanguageUsePtBr = false;
        if (activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;
            mainActivity.loadConfigPreferences(getContext());
            posterSize = mainActivity.getPosterSize();
            backdropSize = mainActivity.getBackDropSize();
            isLanguageUsePtBr = mainActivity.isLanguageUsePtBr();
        }

        if (!Utils.checkAppConnectionStatus(getContext())) {
            if (mNoInternetConnection != null) Utils.setLayoutVisible(mNoInternetConnection);
            if (mRecyclerView != null) Utils.setLayoutInvisible(mRecyclerView);
            if (mLoadingTodayLayout != null) Utils.setLayoutInvisible(mLoadingTodayLayout);
            if (mAutoLoadAirTodayLink != null) Utils.setLayoutInvisible(mAutoLoadAirTodayLink);
            if (mLoadMoreItensLayout != null) Utils.setLayoutInvisible(mLoadMoreItensLayout);
            if (mPage < mTotalPages) mIsLoadMore = true;

        } else {
            // Set Layout Visible
            Utils.setLayoutVisible(mRecyclerView);
            Utils.setLayoutInvisible(mNoInternetConnection);
            if (mPage < mTotalPages) mIsLoadMore = true;
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
                    // Set TVShow List to get when update.
                    mTotalPages = getTotalPages();
                    mPage = getPage();
                    if (mTVShowList.isEmpty() || mTVShowList == null) {
                        mTVShowList = getTVShows();
                    } else {
                        mTVShowList.addAll(getTVShows());
                    }
                    mAiringTodayRecyclerViewAdapter.loadNewData(mTVShowList);
                }
                Utils.setLayoutInvisible(mLoadMoreItensLayout);
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
                    Intent intent = new Intent(getContext(), DetailsActivity.class);
                    intent.putExtra(AppConsts.TVSHOW_TRANSFER, mAiringTodayRecyclerViewAdapter.getTVShow(position));
                    intent.putExtra(AppConsts.TVSHOW_TITLE, mAiringTodayRecyclerViewAdapter.getTVShow(position).getName());
                    startActivity(intent);
                }

                @Override
                public void onItemLongClick(View view, int position) {
                    if (mAlertDialog != null && mAlertDialog.isShowing()) mAlertDialog.cancel();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    final TVShow tvshow = mAiringTodayRecyclerViewAdapter.getTVShow(position);
                    builder.setMessage(getString(R.string.add_show_float_menu, tvshow.getName()));
                    builder.setPositiveButton(getString(R.string.yes_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sharedPref = getActivity().getSharedPreferences(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
                            String restoredFavorites = sharedPref.getString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, null);
                            SharedPreferences.Editor spEditor = sharedPref.edit();
                            List<Integer> idShowList = Utils.convertStringToIntegerList(AppConsts.FAVORITES_SHAREDPREFERENCES_DELIMITER, restoredFavorites);
                            if (!Utils.checkItemInIntegerList(idShowList, tvshow.getId())) {
                                idShowList.add(tvshow.getId());
                                String idsResult = Utils.convertListToString(AppConsts.FAVORITES_SHAREDPREFERENCES_DELIMITER, idShowList);
                                spEditor.putString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, idsResult);
                                spEditor.apply();
                                mAlertDialog.dismiss();
                                Snackbar favoriteSnackbar = Utils.createSnackbarObject(Color.GREEN, getString(R.string.success_add_new_show_with_name, tvshow.getName()), mAiringTodayMainLayout);
                                favoriteSnackbar.setAction(getContext().getString(R.string.undo_snackbar), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        SharedPreferences sharedPref = getActivity().getSharedPreferences(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
                                        String restoredFavorites = sharedPref.getString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, null);
                                        SharedPreferences.Editor spEditor = sharedPref.edit();
                                        List<Integer> idShowList = Utils.convertStringToIntegerList(AppConsts.FAVORITES_SHAREDPREFERENCES_DELIMITER, restoredFavorites);
                                        idShowList = Utils.removeIntegerItemFromList(idShowList, tvshow.getId());
                                        String idsResult = Utils.convertListToString(AppConsts.FAVORITES_SHAREDPREFERENCES_DELIMITER, idShowList);
                                        spEditor.putString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, idsResult);
                                        spEditor.apply();
                                        Utils.createSnackbar(Color.RED, getString(R.string.success_remove_show), mAiringTodayMainLayout);
                                        // Refresh Favorites
                                        if (getActivity() instanceof MainActivity) ((MainActivity) getActivity()).refreshFavorites();
                                    }
                                });
                                favoriteSnackbar.setActionTextColor(Color.WHITE);
                                // Refresh Favorites
                                if (getActivity() instanceof MainActivity) ((MainActivity) getActivity()).refreshFavorites();
                                favoriteSnackbar.show();
                            }else{
                                // Already had in favorites.
                                Utils.createSnackbar(Color.RED, getString(R.string.error_already_has_show_with_name, tvshow.getName()), mAiringTodayMainLayout);
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
            }
            ));
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mRecyclerView != null) {
            if (mIsTablet) {
                if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mLayoutManager = new GridLayoutManager(getContext(), AppConsts.AIRTODAY_PORTRAIT_TABLET);
                } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mLayoutManager = new GridLayoutManager(getContext(), AppConsts.AIRTODAY_LANDSCAPE_TABLET);
                }
            } else {
                if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mLayoutManager = new GridLayoutManager(getContext(), AppConsts.AIRTODAY_PORTRAIT_PHONE);
                } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mLayoutManager = new GridLayoutManager(getContext(), AppConsts.AIRTODAY_LANDSCAPE_PHONE);
                }
            }
            mRecyclerView.setLayoutManager(mLayoutManager);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload configs
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.loadConfigPreferences(getActivity());
    }
}
