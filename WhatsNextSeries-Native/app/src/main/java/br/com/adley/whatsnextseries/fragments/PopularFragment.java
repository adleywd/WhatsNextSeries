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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import br.com.adley.whatsnextseries.R;
import br.com.adley.whatsnextseries.activities.DetailsActivity;
import br.com.adley.whatsnextseries.activities.MainActivity;
import br.com.adley.whatsnextseries.adapters.recyclerview.PopularShowsRecyclerViewAdapter;
import br.com.adley.whatsnextseries.library.AppConsts;
import br.com.adley.whatsnextseries.library.RecyclerItemClickListener;
import br.com.adley.whatsnextseries.library.Utils;
import br.com.adley.whatsnextseries.library.enums.DownloadStatus;
import br.com.adley.whatsnextseries.models.TVShow;
import br.com.adley.whatsnextseries.service.GetPopularShowsJsonData;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopularFragment extends Fragment {
    private int mPage = 1;
    private int mTotalPages;
    private boolean mIsLoadMore = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private List<TVShow> mTVShowList;
    private RecyclerView mRecyclerView;
    private PopularShowsRecyclerViewAdapter mPopularShowsRecyclerViewAdapter;
    private GridLayoutManager mLayoutManager;
    private boolean mIsTablet = false;
    private ProgressBar mLoadMoreItensLayout;
    private View mProgressBarHomeLayout;
    private View mNoInternetConnection;
    private ImageView mRefreshButtonNoConnection;
    private AdView mAdView;
    private AlertDialog mAlertDialog;
    private View popularFragment;
    private CoordinatorLayout mMainPopularShowLayout;

    public static PopularFragment newInstance(){
        return new PopularFragment();
    }

    public PopularFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        popularFragment  = inflater.inflate(R.layout.fragment_popular, container, false);
        mTVShowList = new ArrayList<>();
        mNoInternetConnection = popularFragment.findViewById(R.id.no_internet_connection_layout);
        mRefreshButtonNoConnection = (ImageView) popularFragment.findViewById(R.id.refresh_button_no_internet);
        mLoadMoreItensLayout = (ProgressBar) popularFragment.findViewById(R.id.load_more_air_today_progressbar);
        mProgressBarHomeLayout = popularFragment.findViewById(R.id.loading_progress_popular_show_layout);
        mMainPopularShowLayout = (CoordinatorLayout) popularFragment.findViewById(R.id.popular_show_fragment_main);
        Utils.setLayoutVisible(mProgressBarHomeLayout);
        return popularFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Create and generate the recycler view for list of results
        mPopularShowsRecyclerViewAdapter = new PopularShowsRecyclerViewAdapter(getActivity(), new ArrayList<TVShow>());
        mRecyclerView = (RecyclerView) popularFragment.findViewById(R.id.recycler_view_popular_show_list);
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(mPopularShowsRecyclerViewAdapter);
        mRecyclerView.setAdapter(animationAdapter);

        mIsTablet = Utils.isTablet(getActivity());
        if (mIsTablet) {
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mLayoutManager = new GridLayoutManager(getActivity(), AppConsts.AIRTODAY_PORTRAIT_TABLET);
            } else {
                mLayoutManager = new GridLayoutManager(getActivity(), AppConsts.AIRTODAY_LANDSCAPE_TABLET);
            }
        } else {
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mLayoutManager = new GridLayoutManager(getActivity(), AppConsts.AIRTODAY_PORTRAIT_PHONE);
            } else {
                mLayoutManager = new GridLayoutManager(getActivity(), AppConsts.AIRTODAY_LANDSCAPE_PHONE);
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
                                executePopularShowList();
                            }
                        }
                    }
                }
            }
        });
        mRefreshButtonNoConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executePopularShowList();
            }
        });
        bindRecyclerView();
        executePopularShowList();
    }

    public void executePopularShowList() {
        Activity activity = getActivity();
        if (Utils.checkAppConnectionStatus(activity)) {
            Utils.setLayoutInvisible(mNoInternetConnection);
            if (activity instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) activity;
                mainActivity.loadConfigPreferences(activity);
                String posterSize = mainActivity.getPosterSize();
                String backdropSize = mainActivity.getBackDropSize();
                boolean isLanguageUsePtBr = mainActivity.isLanguageUsePtBr();
                // Set Layout Visible
                Utils.setLayoutVisible(mRecyclerView);
                if (mPage < mTotalPages) mIsLoadMore = true;
                ProcessPopularTVShow processTVShowsAiringToday = new ProcessPopularTVShow(activity, isLanguageUsePtBr, posterSize, backdropSize, mPage);
                processTVShowsAiringToday.execute();
            }
        } else {
            Utils.setLayoutInvisible(mRecyclerView);
            Utils.setLayoutInvisible(mProgressBarHomeLayout);
            Utils.setLayoutVisible(mNoInternetConnection);
            Utils.setLayoutInvisible(mProgressBarHomeLayout);
            Snackbar snackbarNoInternet = Snackbar
                    .make(mNoInternetConnection, this.getString(R.string.cant_load_popular_shows), Snackbar.LENGTH_LONG)
                    .setAction(this.getString(R.string.retry_snackbar), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            executePopularShowList();
                        }
                    });
            snackbarNoInternet.setActionTextColor(Color.RED);
            snackbarNoInternet.show();
        }
    }

    // Create the touch for the recyclerview list
    public void bindRecyclerView() {
        if (mRecyclerView != null) {
            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //Creates and configure intent to call tv show details activity
                    Intent intent = new Intent(getContext(), DetailsActivity.class);
                    intent.putExtra(AppConsts.TVSHOW_TRANSFER, mPopularShowsRecyclerViewAdapter.getTVShow(position));
                    intent.putExtra(AppConsts.TVSHOW_TITLE, mPopularShowsRecyclerViewAdapter.getTVShow(position).getName());
                    startActivity(intent);
                }

                @Override
                public void onItemLongClick(View view, int position) {
                    if (mAlertDialog != null && mAlertDialog.isShowing()) mAlertDialog.cancel();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    final TVShow tvshow = mPopularShowsRecyclerViewAdapter.getTVShow(position);
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
                                Snackbar favoriteSnackbar = Utils.createSnackbarObject(Color.GREEN, getString(R.string.success_add_new_show_with_name, tvshow.getName()), mMainPopularShowLayout);
                                favoriteSnackbar.setAction(getString(R.string.undo_snackbar), new View.OnClickListener() {
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
                                        Utils.createSnackbar(Color.RED, getString(R.string.success_remove_show_with_name, tvshow.getName()), mMainPopularShowLayout);
                                        if (getActivity() instanceof MainActivity) ((MainActivity) getActivity()).refreshFavorites();
                                    }
                                });
                                favoriteSnackbar.setActionTextColor(Color.WHITE);
                                favoriteSnackbar.show();
                                if (getActivity() instanceof MainActivity) ((MainActivity) getActivity()).refreshFavorites();
                            }else{
                                // Already had in favorites.
                                Utils.createSnackbar(Color.RED, getString(R.string.error_already_has_show_with_name, tvshow.getName()), mMainPopularShowLayout);
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

    // Process and execute data into recycler view
    public class ProcessPopularTVShow extends GetPopularShowsJsonData {

        public ProcessPopularTVShow(Context context, boolean isLanguageUsePtBr, String posterSize, String backDropSize, int page) {
            super(context, isLanguageUsePtBr, posterSize, backDropSize, page);
        }

        public void execute() {
            ProcessData processData = new ProcessData();
            processData.execute();
        }

        public class ProcessData extends GetPopularShowsJsonData.DownloadJsonData {
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                Utils.setLayoutInvisible(mProgressBarHomeLayout);
                if (getDownloadStatus() != DownloadStatus.OK || getTVShows() == null) {
                    Utils.setLayoutInvisible(mRecyclerView);
                    Utils.setLayoutVisible(mNoInternetConnection);
                    Toast.makeText(getActivity() ,getString(R.string.cant_load_popular_shows), Toast.LENGTH_LONG).show();
                } else {
                    // Set TVShow List to get when update.
                    mTotalPages = getTotalPages();
                    mPage = getPage();
                    Log.v("PAGE", String.valueOf(mPage));
                    if (mTVShowList.isEmpty() || mTVShowList == null) {
                        mTVShowList = getTVShows();
                    } else {
                        mTVShowList.addAll(getTVShows());
                    }
                    mPopularShowsRecyclerViewAdapter.loadNewData(mTVShowList);
                }
                Utils.setLayoutInvisible(mLoadMoreItensLayout);
            }
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
