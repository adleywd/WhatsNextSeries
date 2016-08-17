package br.com.adley.myseriesproject.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.activities.DetailsTVShowActivity;
import br.com.adley.myseriesproject.activities.HomeActivity;
import br.com.adley.myseriesproject.activities.SearchTVShowActivity;
import br.com.adley.myseriesproject.library.AppConsts;
import br.com.adley.myseriesproject.library.RecyclerItemClickListener;
import br.com.adley.myseriesproject.library.Utils;
import br.com.adley.myseriesproject.library.enums.DownloadStatus;
import br.com.adley.myseriesproject.models.TVShow;
import br.com.adley.myseriesproject.models.TVShowDetails;
import br.com.adley.myseriesproject.adapters.recyclerview.FavoritesRecyclerViewAdapter;
import br.com.adley.myseriesproject.service.GetTVShowDetailsJsonData;
import br.com.adley.myseriesproject.service.GetTVShowSeasonJsonData;

/**
 * Created by Adley.Damaceno on 21/07/2016.
 * Class to control data for favorites user's shows.
 */
public class FavoritesFragment extends Fragment {

    private List<TVShowDetails> mTVShowDetailsList;
    private List<Integer> mIdShowList;
    private FavoritesRecyclerViewAdapter mFavoritesRecyclerViewAdapter;
    private RecyclerView mRecyclerView;
    private AlertDialog mAlertDialog;
    private TVShow mTVShowSelected;
    private View favoritesFragment;
    private View mNoInternetConnection;
    private String mRestoredFavorites;
    private ImageButton mNoFavsSearchButton;
    private View mNoFavsSearchLayout;
    private View mProgressBarHomeLayout;
    private ProgressBar mProgressBarHome;
    private int mProgressBarCount = 0;
    private ImageView mLoadFavoritesNoInternet;
    private boolean mIsTablet = false;
    private boolean mIsRecyclerViewBind = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        favoritesFragment = inflater.inflate(R.layout.fragment_favorites, container, false);
        mIdShowList = new ArrayList<>();
        mNoInternetConnection = favoritesFragment.findViewById(R.id.no_internet_connection);
        mNoFavsSearchButton = (ImageButton) favoritesFragment.findViewById(R.id.no_favs_home_imagebutton_search);
        mNoFavsSearchLayout = favoritesFragment.findViewById(R.id.no_favs_home_layout);
        mProgressBarHomeLayout = favoritesFragment.findViewById(R.id.loading_panel_home);
        mProgressBarHome = (ProgressBar) favoritesFragment.findViewById(R.id.shared_progressbar_home);
        mLoadFavoritesNoInternet = (ImageView) favoritesFragment.findViewById(R.id.refresh_button_no_internet);
        mLoadFavoritesNoInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.checkAppConnectionStatus(getContext())) {
                    Snackbar.make(mNoInternetConnection, getActivity().getString(R.string.cant_load_favorites), Snackbar.LENGTH_LONG).show();
                } else {
                    executeFavoriteList();
                }
            }
        });

        mAlertDialog = null;
        return favoritesFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        executeFavoriteList();
        if (!Utils.checkAppConnectionStatus(getContext())) {
            if (getActivity() instanceof HomeActivity) {
                HomeActivity homeActivity = (HomeActivity) getActivity();
                homeActivity.setAdViewInvisible();
            }
            Snackbar.make(mNoInternetConnection, getActivity().getString(R.string.error_no_internet_connection), Snackbar.LENGTH_LONG).show();
        }else {
            if (getActivity() instanceof HomeActivity) {
                HomeActivity homeActivity = (HomeActivity) getActivity();
                homeActivity.setAdViewVisible();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPref = getActivity().getSharedPreferences(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
        String restartRestoredFavorites = sharedPref.getString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, null);
        if (!Utils.checkAppConnectionStatus(getContext())) {
            Snackbar.make(mNoInternetConnection, getActivity().getString(R.string.error_no_internet_connection), Snackbar.LENGTH_LONG).show();
        }
        if (restartRestoredFavorites != null) {
            if (!restartRestoredFavorites.equals(mRestoredFavorites)) {
                if (!Utils.checkAppConnectionStatus(getContext())) {
                    Snackbar.make(mNoInternetConnection, getActivity().getString(R.string.cant_load_favorites), Snackbar.LENGTH_LONG).show();
                } else {
                    executeFavoriteList();
                }
            }
        } else {
            executeFavoriteList();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_favorites, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle refresh button.
        switch (item.getItemId()) {
            case R.id.action_refresh: {
                if (!Utils.checkAppConnectionStatus(getContext())) {
                    Snackbar.make(mNoInternetConnection, getActivity().getString(R.string.cant_load_favorites), Snackbar.LENGTH_LONG).show();
                }
                executeFavoriteList();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    public void executeFavoriteList() {
        // Start progress bar in zero.
        if (mProgressBarHome != null) {
            mProgressBarHome.setProgress(0);
        }
        Utils.setLayoutVisible(mProgressBarHomeLayout);

        if (mRecyclerView != null) {
            Utils.setLayoutVisible(mRecyclerView);
        }
        if (Utils.checkAppConnectionStatus(getContext())) {
            Utils.setLayoutInvisible(mNoInternetConnection);

            SharedPreferences sharedPref = getContext().getSharedPreferences(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
            mRestoredFavorites = sharedPref.getString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, null);
            // Check connection Status
            // Clear the favorites show list (to not duplicate)
            mTVShowDetailsList = new ArrayList<>();
            if (mRestoredFavorites != null) {
                mIdShowList = Utils.convertStringToIntegerList(AppConsts.FAVORITES_SHAREDPREFERENCES_DELIMITER, mRestoredFavorites);
            }

            if (mRestoredFavorites != null) {
                Utils.setLayoutInvisible(mNoFavsSearchLayout);
            } else {
                Utils.setLayoutVisible(mNoFavsSearchLayout);
            }

            mAlertDialog = null;

            mFavoritesRecyclerViewAdapter = new FavoritesRecyclerViewAdapter(getContext(), new ArrayList<TVShowDetails>());
            mRecyclerView = (RecyclerView) favoritesFragment.findViewById(R.id.recycler_view_favorites_list);
            mRecyclerView.setAdapter(mFavoritesRecyclerViewAdapter);
            mIsTablet = Utils.isTablet(getContext());
            if (mIsTablet) {
                if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                } else {
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                }
            } else {
                if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
                } else {
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                }
            }
            mRecyclerView.setHasFixedSize(true);

            // Create the touch for the recycler view list
            /*if (!isRefreshing) {
            }*/
            // Check if recycler view was bind, if not, it'll bind
            if (!mIsRecyclerViewBind) {
                bindRecyclerView();
            }

            if (mIdShowList.size() == 0) {
                Utils.setLayoutInvisible(mProgressBarHomeLayout);
                mNoFavsSearchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), SearchTVShowActivity.class);
                        startActivity(intent);
                    }
                });
            } else {
                Activity activity = getActivity();
                if (activity instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) activity;
                    homeActivity.loadConfigPreferences(getContext());
                    homeActivity.setAdViewVisible();
                    for (int idShow : mIdShowList) {
                        ProcessFavoritesTVShowsDetails processFavoritesTVShowsDetails = new ProcessFavoritesTVShowsDetails(idShow, homeActivity.getPosterSize(), homeActivity.getBackDropSize(), homeActivity.isLanguageUsePtBr());
                        processFavoritesTVShowsDetails.execute();
                    }
                }
            }
        } else {
            if (getActivity() instanceof HomeActivity) {
                HomeActivity homeActivity = (HomeActivity) getActivity();
                homeActivity.setAdViewInvisible();
            }
            Utils.setLayoutInvisible(mProgressBarHomeLayout);
            if (mRecyclerView != null) {
                Utils.setLayoutInvisible(mRecyclerView);
            }
            if (mNoFavsSearchLayout != null) Utils.setLayoutInvisible(mNoFavsSearchLayout);
            Utils.setLayoutVisible(mNoInternetConnection);
        }
    }

    // Bind click in card views
    public void bindRecyclerView() {
        if (mRecyclerView != null) {
            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //Creates and configure intent to call tv show details activity
                    Intent intent = new Intent(getContext(), DetailsTVShowActivity.class);
                    intent.putExtra(AppConsts.TVSHOW_TRANSFER, mFavoritesRecyclerViewAdapter.getTVShow(position));
                    startActivity(intent);
                }

                @Override
                public void onItemLongClick(View view, int position) {
                    if (mAlertDialog != null && mAlertDialog.isShowing()) mAlertDialog.cancel();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    mTVShowSelected = mFavoritesRecyclerViewAdapter.getTVShow(position);
                    builder.setTitle(getString(R.string.warning_alert));
                    builder.setMessage(getString(R.string.delete_show_float_menu, mTVShowSelected.getOriginalName()));
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setPositiveButton(getString(R.string.yes_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sharedPref = getContext().getSharedPreferences(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
                            mIdShowList = Utils.removeIntegerItemFromList(mIdShowList, mTVShowSelected.getId());
                            String idsResult = Utils.convertListToString(AppConsts.FAVORITES_SHAREDPREFERENCES_DELIMITER, mIdShowList);
                            mRestoredFavorites = sharedPref.getString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, null);
                            SharedPreferences.Editor spEditor = sharedPref.edit();
                            spEditor.putString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, idsResult);
                            spEditor.apply();
                            if (mAlertDialog != null && mAlertDialog.isShowing())
                                mAlertDialog.dismiss();
                            executeFavoriteList();
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
            mIsRecyclerViewBind = true;
        }
    }

    // Process and execute data into recycler view
    public class ProcessFavoritesTVShowsDetails extends GetTVShowDetailsJsonData {
        private ProcessData processData;

        public ProcessFavoritesTVShowsDetails(int idShow, String posterSize, String backDropSize, boolean isLanguageUsePtBr) {
            super(idShow, posterSize, backDropSize, getContext(), isLanguageUsePtBr);
        }

        public void execute() {
            // Start process data (download and get)
            processData = new ProcessData();
            if (processData.isCancelled()){
                return;
            }
            processData.execute();
        }

        public class ProcessData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                if (processData.isCancelled()){
                    return;
                }
                super.onPostExecute(webData);
                if (getDownloadStatus() != DownloadStatus.OK || getTVShowsDetails() == null) {
                    Utils.setLayoutInvisible(mProgressBarHomeLayout);
                    Utils.setLayoutVisible(mNoInternetConnection);
                } else {
                    //Get and Process SeasonData
                    if (getTVShowsDetails().getSeasonNumberList() != null && getTVShowsDetails().getSeasonNumberList().size() > 0) {
                        int lastSeason = Utils.maxNumber(getTVShowsDetails().getSeasonNumberList());
                        ProcessSeason processSeason = new ProcessSeason(getTVShowsDetails(), lastSeason);
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
                            mFavoritesRecyclerViewAdapter.loadNewData(mTVShowDetailsList);

                        }
                    }
                }
            }
        }
    }

    // Process Season Data
    public class ProcessSeason extends GetTVShowSeasonJsonData {

        public ProcessSeason(TVShowDetails show, int showNumber) {
            super(show, showNumber, getContext());
        }

        public void execute() {
            // Start process data (download and get)
            ProcessData processData = new ProcessData();
            processData.execute();
        }

        public class ProcessData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                if (getContext() != null) {
                    // Set next episode
                    Utils.setNextEpisode(getTVShowSeasons(), getTVShowDetails(), getContext());
                    mTVShowDetailsList.add(getTVShowDetails());
                    mProgressBarCount += mProgressBarHome.getMax() / mIdShowList.size();
                    mProgressBarHome.setProgress(mProgressBarCount);
                    if (mIdShowList.size() == mTVShowDetailsList.size()) {
                        Utils.setLayoutInvisible(mProgressBarHomeLayout);
                        mFavoritesRecyclerViewAdapter.loadNewData(mTVShowDetailsList);
                    }
                }
            }
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mIsTablet) {
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), AppConsts.FAVORITES_PORTRAIT_TABLET));
            } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), AppConsts.FAVORITES_LANDSCAPE_TABLET));
            }
        } else {
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), AppConsts.FAVORITES_PORTRAIT_PHONE));
            } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), AppConsts.FAVORITES_LANDSCAPE_PHONE));
            }
        }
    }
}
