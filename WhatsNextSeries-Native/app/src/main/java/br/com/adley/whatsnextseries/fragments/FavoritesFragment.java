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
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.adley.whatsnextseries.R;
import br.com.adley.whatsnextseries.activities.DetailsActivity;
import br.com.adley.whatsnextseries.activities.MainActivity;
import br.com.adley.whatsnextseries.adapters.recyclerview.FavoritesRecyclerViewAdapter;
import br.com.adley.whatsnextseries.library.AppConsts;
import br.com.adley.whatsnextseries.library.Utils;
import br.com.adley.whatsnextseries.library.enums.DownloadStatus;
import br.com.adley.whatsnextseries.models.TVShowDetails;
import br.com.adley.whatsnextseries.service.GetTVShowDetailsJsonData;
import br.com.adley.whatsnextseries.service.GetTVShowSeasonJsonData;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by Adley.Damaceno on 21/07/2016.
 * Class to control data for favorites user's shows.
 */
public class FavoritesFragment extends Fragment implements View.OnLongClickListener {
    public static FavoritesFragment newInstance(){
        return new FavoritesFragment();
    }
    
    private String LOG_TAG = FavoritesFragment.class.getSimpleName();
    private List<TVShowDetails> mTVShowDetailsList;
    private List<Integer> mIdShowList;
    private FavoritesRecyclerViewAdapter mFavoritesRecyclerViewAdapter;
    private RecyclerView mRecyclerView;
    private AlertDialog mAlertDialog;
    private View favoritesFragment;
    private View mNoInternetConnection;
    private String mRestoredFavorites;
    private View mNoFavsSearchLayout;
    private View mProgressBarHomeLayout;
    private ProgressBar mProgressBarHome;
    private int mProgressBarCount = 0;
    private ImageView mLoadFavoritesNoInternet;
    private boolean mIsTablet = false;
    private boolean mIsRecyclerViewBind = false;
    private boolean mIsInActionMode = false;
    private TextView mTitleCounterTextView;
    private ArrayList<String> mSelectionList = new ArrayList<>();
    private ArrayList<String> mSelectionListPostiion = new ArrayList<>();
    private LinearLayout.LayoutParams mLayoutParamsTitleToolbar;
    private int mMarginTopTitleToolbar = 15;
    private int mTextSizeToolbar = 18;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        favoritesFragment = inflater.inflate(R.layout.fragment_favorites, container, false);
        mIdShowList = new ArrayList<>();
        mNoInternetConnection = favoritesFragment.findViewById(R.id.no_internet_connection);
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



        // Set emoji to end of text
        /*
        TextView noFavsText = (TextView)mNoFavsSearchLayout.findViewById(R.id.no_show_text);
        int wearyFaceEmoji = 0x1F629;
        String newNoFavsText = String.valueOf(noFavsText.getText()) + Utils.getEmojiUnicode(wearyFaceEmoji);
        noFavsText.setText(newNoFavsText);
        */
        mAlertDialog = null;
        return favoritesFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        executeFavoriteList();
        if (!Utils.checkAppConnectionStatus(getContext())) {
            Snackbar.make(mNoInternetConnection, getActivity().getString(R.string.error_no_internet_connection), Snackbar.LENGTH_LONG).show();
        }
        mLayoutParamsTitleToolbar = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParamsTitleToolbar.setMargins(0,mMarginTopTitleToolbar,0,0);
        mTitleCounterTextView = (TextView) getActivity().findViewById(R.id.item_counter_selected);
        mTitleCounterTextView.setText(getString(R.string.app_name_with_icon));
        mTitleCounterTextView.setTextSize(mTextSizeToolbar);
        mTitleCounterTextView.setTextColor(Color.WHITE);
        mTitleCounterTextView.setLayoutParams(mLayoutParamsTitleToolbar);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload configs
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.loadConfigPreferences(getActivity());

        mIsInActionMode = false;
        clearActionMode();
        //Validate because offline will result in a null object
        if(mFavoritesRecyclerViewAdapter != null) {
            mFavoritesRecyclerViewAdapter.notifyDataSetChanged();
        }
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
            mIdShowList = new ArrayList<>();
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
        if (item.getItemId() == R.id.action_mode_delete) {
            if(!mSelectionList.isEmpty()){
                    removeFavorites();
            }else {
                Utils.createSnackbar(Color.RED, getString(R.string.empty_delete_favorite_list), mRecyclerView);
            }
            return true;
        }else if(item.getItemId() == R.id.action_mode_forward){
            clearActionMode();
            mFavoritesRecyclerViewAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void executeFavoriteList() {
        // Start progress bar in zero.
        if (mProgressBarHome != null) {
            mProgressBarHome.setProgress(0);
        }
        Utils.setLayoutVisible(mProgressBarHomeLayout);

        if (mRecyclerView != null && mIdShowList.size() > 0) {
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

            mFavoritesRecyclerViewAdapter = new FavoritesRecyclerViewAdapter(getContext(), new ArrayList<TVShowDetails>(), this);
            mRecyclerView = (RecyclerView) favoritesFragment.findViewById(R.id.recycler_view_favorites_list);
            ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(mFavoritesRecyclerViewAdapter);
            mRecyclerView.setAdapter(animationAdapter);
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

            if (mIdShowList.size() == 0) {
                Utils.setLayoutInvisible(mProgressBarHomeLayout);
            } else {
                Activity activity = getActivity();
                if (activity instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) activity;
                    mainActivity.loadConfigPreferences(getContext());
                    for (int idShow : mIdShowList) {
                        ProcessFavoritesTVShowsDetails processFavoritesTVShowsDetails = new ProcessFavoritesTVShowsDetails(idShow, mainActivity.getPosterSize(), mainActivity.getBackDropSize(), mainActivity.isLanguageUsePtBr());
                        processFavoritesTVShowsDetails.execute();
                    }
                }
            }
        } else {
            Utils.setLayoutInvisible(mProgressBarHomeLayout);
            if (mRecyclerView != null) {
                Utils.setLayoutInvisible(mRecyclerView);
            }
            if (mNoFavsSearchLayout != null) Utils.setLayoutInvisible(mNoFavsSearchLayout);
            Utils.setLayoutVisible(mNoInternetConnection);
        }
    }

    public void removeFavorites(){
        if (mAlertDialog != null && mAlertDialog.isShowing()) mAlertDialog.cancel();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //builder.setTitle();
        if(mSelectionList.size() == 1) {
            builder.setMessage(getString(R.string.delete_favorite_message_one));
        }else{
            builder.setMessage(getString(R.string.delete_favorite_message, mSelectionList.size()));
        }
        //builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton(getString(R.string.yes_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPref = getContext().getSharedPreferences(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
                final List<Integer> favoritesListToRestore = Utils.cloneList(mIdShowList);
                mIdShowList = Utils.removeStringsListFromIntegerList(getContext(), mIdShowList, mSelectionList);
                String idsResult = Utils.convertListToString(AppConsts.FAVORITES_SHAREDPREFERENCES_DELIMITER, mIdShowList);
                mRestoredFavorites = sharedPref.getString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, null);
                SharedPreferences.Editor spEditor = sharedPref.edit();
                spEditor.putString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, idsResult);
                spEditor.apply();
                if (mAlertDialog != null && mAlertDialog.isShowing())
                    mAlertDialog.dismiss();
                /*try{
                    for (String pos : mSelectionListPostiion){
                        mFavoritesRecyclerViewAdapter.remove(Integer.parseInt(pos));
                    }
                }catch (IndexOutOfBoundsException ex){
                    Log.e(LOG_TAG, ex.getMessage());
                    mFavoritesRecyclerViewAdapter.notifyDataSetChanged();
                }*/
                mFavoritesRecyclerViewAdapter.notifyDataSetChanged();
                executeFavoriteList();
                clearActionMode();
                //executeFavoriteList();
                Snackbar favoritesSnackbar = Utils.createSnackbarObject(Color.RED,getString(R.string.success_remove_show), mRecyclerView);
                favoritesSnackbar.setAction(getString(R.string.undo_snackbar), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences sharedPref = getContext().getSharedPreferences(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
                        mIdShowList = favoritesListToRestore;
                        String idsResult = Utils.convertListToString(AppConsts.FAVORITES_SHAREDPREFERENCES_DELIMITER, mIdShowList);
                        mRestoredFavorites = sharedPref.getString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, null);
                        SharedPreferences.Editor spEditor = sharedPref.edit();
                        spEditor.putString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, idsResult);
                        spEditor.apply();
                        clearActionMode();
                        //TODO EFFECT TO ADD ITEM TO POSITION
                        executeFavoriteList();
                    }
                });
                favoritesSnackbar.setActionTextColor(Color.WHITE);
                favoritesSnackbar.show();
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

    public boolean isInActionMode() {
        return mIsInActionMode;
    }

    public void prepareSelection(View view, int position) {
        // Check if the touch came from checkbox or Item
        if (view instanceof CheckBox) {
            if (((AppCompatCheckBox) view).isChecked()) {
                mSelectionList.add(String.valueOf(mFavoritesRecyclerViewAdapter.getTVShow(position).getId()));
                mSelectionListPostiion.add(String.valueOf(position));
            } else {
                mSelectionList.remove(String.valueOf(mFavoritesRecyclerViewAdapter.getTVShow(position).getId()));
                mSelectionListPostiion.remove(String.valueOf(position));
            }
        } else {
            CheckBox checkItem = (CheckBox) view.findViewById(R.id.check_list_item);
            if (checkItem.isChecked()) {
                mSelectionList.add(String.valueOf(mFavoritesRecyclerViewAdapter.getTVShow(position).getId()));
                mSelectionListPostiion.add(String.valueOf(position));
            } else {
                mSelectionList.remove(String.valueOf(mFavoritesRecyclerViewAdapter.getTVShow(position).getId()));
                mSelectionListPostiion.remove(String.valueOf(position));
            }
        }
        if (mSelectionList.size() == 0) {
            mTitleCounterTextView.setText(getString(R.string.zero_items_selected));
        } else if (mSelectionList.size() == 1) {
            mTitleCounterTextView.setText(getString(R.string.one_item_selected));
        } else {
            mTitleCounterTextView.setText(getString(R.string.num_items_selected, mSelectionList.size()));
        }
    }
    public void clearActionModeWithNotify(){
        mFavoritesRecyclerViewAdapter.notifyDataSetChanged();
        clearActionMode();
    }

    public void clearActionMode() {
        mIsInActionMode = false;
        mSelectionList = new ArrayList<>();
        mSelectionListPostiion = new ArrayList<>();
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.getToolbar().getMenu().clear();
        mainActivity.getToolbar().inflateMenu(R.menu.menu_main);
        mainActivity.getToolbar().inflateMenu(R.menu.menu_favorites);
        mainActivity.getToolbar().setLogo(R.mipmap.ic_logo);
        mLayoutParamsTitleToolbar.setMargins(0,mMarginTopTitleToolbar,0,0);
        mTitleCounterTextView.setLayoutParams(mLayoutParamsTitleToolbar);
        mTitleCounterTextView.setText(getString(R.string.app_name_with_icon));
    }


    @Override
    public boolean onLongClick(View v) {
        if(!isInActionMode()) {
            mIsInActionMode = true;
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.getToolbar().getMenu().clear();
            mainActivity.getToolbar().inflateMenu(R.menu.menu_action_mode);
            mainActivity.getToolbar().setLogo(android.R.color.transparent);
            mTitleCounterTextView.setText(R.string.zero_items_selected);
            mFavoritesRecyclerViewAdapter.notifyDataSetChanged();
            mLayoutParamsTitleToolbar.setMargins(0, 0, 0, 0);
            mTitleCounterTextView.setLayoutParams(mLayoutParamsTitleToolbar);
            return true;
        }
        return false;
    }

    public void onClickOpenDetail(View v, int position) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra(AppConsts.TVSHOW_TRANSFER, mFavoritesRecyclerViewAdapter.getTVShow(position));
        intent.putExtra(AppConsts.TVSHOW_TITLE, mFavoritesRecyclerViewAdapter.getTVShow(position).getName());
        startActivity(intent);
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
            if (processData.isCancelled()) {
                return;
            }
            processData.execute();
        }

        public class ProcessData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                if (processData.isCancelled()) {
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
        if (mRecyclerView != null) {
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
}
