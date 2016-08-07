package br.com.adley.myseriesproject.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.adapters.recyclerview.AiringTodayRecyclerViewAdapter;
import br.com.adley.myseriesproject.adapters.recyclerview.PopularShowsRecyclerViewAdapter;
import br.com.adley.myseriesproject.library.AppConsts;
import br.com.adley.myseriesproject.library.RecyclerItemClickListener;
import br.com.adley.myseriesproject.library.Utils;
import br.com.adley.myseriesproject.library.enums.DownloadStatus;
import br.com.adley.myseriesproject.models.TVShow;
import br.com.adley.myseriesproject.service.GetAiringTodayJsonData;
import br.com.adley.myseriesproject.service.GetPopularShowsJsonData;
import br.com.adley.myseriesproject.service.GetTVShowJsonData;

public class PopularTVShowActivity extends BaseActivity {
    private View mNoInternetConnection;
    private int mPage = 1;
    private int mTotalPages;
    private boolean mIsLoadMore= true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private List<TVShow> mTVShowList;
    private RecyclerView mRecyclerView;
    private PopularShowsRecyclerViewAdapter mPopularShowsRecyclerViewAdapter;
    private GridLayoutManager mLayoutManager;
    private boolean mIsTablet = false;
    private ProgressBar mLoadMoreItensLayout;
    private View mProgressBarHomeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_tvshow);
        activateToolbarWithHomeEnabled();
        mTVShowList = new ArrayList<>();
        mLoadMoreItensLayout = (ProgressBar) findViewById(R.id.load_more_air_today_progressbar);
        mProgressBarHomeLayout = findViewById(R.id.loading_progress_airing_today_layout);
        Utils.setLayoutVisible(mProgressBarHomeLayout);

        // Create and generate the recycler view for list of results
        mPopularShowsRecyclerViewAdapter = new PopularShowsRecyclerViewAdapter(PopularTVShowActivity.this, new ArrayList<TVShow>());
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_popular_show_list);
        mRecyclerView.setAdapter(mPopularShowsRecyclerViewAdapter);

        mIsTablet = Utils.isTablet(this);
        if (mIsTablet) {
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mLayoutManager = new GridLayoutManager(PopularTVShowActivity.this, AppConsts.AIRTODAY_PORTRAIT_TABLET);
            } else {
                mLayoutManager = new GridLayoutManager(PopularTVShowActivity.this, AppConsts.AIRTODAY_LANDSCAPE_TABLET);
            }
        } else {
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mLayoutManager = new GridLayoutManager(PopularTVShowActivity.this, AppConsts.AIRTODAY_PORTRAIT_PHONE);
            } else {
                mLayoutManager = new GridLayoutManager(PopularTVShowActivity.this, AppConsts.AIRTODAY_LANDSCAPE_PHONE);
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
        bindRecyclerView();
        executePopularShowList();
    }

    public void executePopularShowList() {
        loadConfigPreferences(PopularTVShowActivity.this);
        String posterSize = getPosterSize();
        String backdropSize = getBackDropSize();
        boolean isLanguageUsePtBr = isLanguageUsePtBr();
        // Set Layout Visible
        Utils.setLayoutVisible(mRecyclerView);
        
        if (mPage < mTotalPages) mIsLoadMore = true;
        ProcessPopularTVShow processTVShowsAiringToday = new ProcessPopularTVShow(PopularTVShowActivity.this, isLanguageUsePtBr, posterSize, backdropSize, mPage);
        processTVShowsAiringToday.execute();
    }

    // Process and execute data into recycler view
    public class ProcessPopularTVShow extends GetPopularShowsJsonData {

        public ProcessPopularTVShow(Context context, boolean isLanguageUsePtBr, String posterSize, String backDropSize, int page) {
            super(context, isLanguageUsePtBr, posterSize, backDropSize, page);
        }

        public void execute() {
            //Utils.setLayoutVisible(mProgressBarHomeLayout);
            ProcessData processData = new ProcessData();
            processData.execute();
        }

        public class ProcessData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                Utils.setLayoutInvisible(mProgressBarHomeLayout);
                if (getDownloadStatus() != DownloadStatus.OK || getTVShows() == null) {
                    //Utils.setLayoutVisible(mLoadingTodayLayout);
                    //Utils.setLayoutVisible(mAutoLoadAirTodayLink);
                    Snackbar snackbarNoInternet = Snackbar
                            .make(mNoInternetConnection, PopularTVShowActivity.this.getString(R.string.cant_load_popular_shows), Snackbar.LENGTH_LONG)
                            .setAction(PopularTVShowActivity.this.getString(R.string.retry_snackbar), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    executePopularShowList();
                                }
                            });
                    snackbarNoInternet.setActionTextColor(Color.RED);
                    snackbarNoInternet.show();
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

    // Create the touch for the recyclerview list
    public void bindRecyclerView() {
        if (mRecyclerView != null) {
            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(PopularTVShowActivity.this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //Creates and configure intent to call tv show details activity
                    Intent intent = new Intent(PopularTVShowActivity.this, DetailsTVShowActivity.class);
                    intent.putExtra(AppConsts.TVSHOW_TRANSFER, mPopularShowsRecyclerViewAdapter.getTVShow(position));
                    startActivity(intent);
                }

                @Override
                public void onItemLongClick(View view, int position) {
                    //Creates and configure intent to call tv show details activity
                    Intent intent = new Intent(PopularTVShowActivity.this, DetailsTVShowActivity.class);
                    intent.putExtra(AppConsts.TVSHOW_TRANSFER, mPopularShowsRecyclerViewAdapter.getTVShow(position));
                    startActivity(intent);
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
                    mLayoutManager = new GridLayoutManager(PopularTVShowActivity.this, AppConsts.AIRTODAY_PORTRAIT_TABLET);
                } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mLayoutManager = new GridLayoutManager(PopularTVShowActivity.this, AppConsts.AIRTODAY_LANDSCAPE_TABLET);
                }
            } else {
                if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mLayoutManager = new GridLayoutManager(PopularTVShowActivity.this, AppConsts.AIRTODAY_PORTRAIT_PHONE);
                } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mLayoutManager = new GridLayoutManager(PopularTVShowActivity.this, AppConsts.AIRTODAY_LANDSCAPE_PHONE);
                }
            }
            mRecyclerView.setLayoutManager(mLayoutManager);
        }
    }
    
}
