package br.com.adley.myseriesproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.activities.DetailsTVShowActivity;
import br.com.adley.myseriesproject.library.AppConsts;
import br.com.adley.myseriesproject.library.RecyclerItemClickListener;
import br.com.adley.myseriesproject.library.Utils;
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
    private boolean mHasInternetConnection = true;
    private View mNoInternetConnection;
    private View mLoadingTodayLayout;
    private ImageView mLoadAirTodayNoInternet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        airTodayFragment = inflater.inflate(R.layout.fragment_airtoday, container, false);
        mLoadingTodayLayout = airTodayFragment.findViewById(R.id.load_airing_today_layout);
        mNoInternetConnection = airTodayFragment.findViewById(R.id.no_internet_connection);
        mLoadAirTodayNoInternet = (ImageView) airTodayFragment.findViewById(R.id.refresh_button_no_internet);
        mLoadAirTodayNoInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setLayoutInvisible(mLoadingTodayLayout);
                if (!Utils.checkAppConnectionStatus(getContext())) {
                    Snackbar.make(mNoInternetConnection, getActivity().getString(R.string.error_no_internet_connection), Snackbar.LENGTH_LONG).show();
                } else {
                    executeAirTodayList(false);
                }
            }
        });

        mLoadAirToday = (ImageView) airTodayFragment.findViewById(R.id.load_airing_today_button);
        mLoadAirToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setLayoutInvisible(mLoadingTodayLayout);
                executeAirTodayList(false);
            }
        });
        return airTodayFragment;
    }

    // Process and execute data into recycler view
    public class ProcessTVShowsAiringToday extends GetAiringTodayJsonData {

        public ProcessTVShowsAiringToday(Context context, boolean isLanguageUsePtBr, String posterSize, String backDropSize) {
            super(context, isLanguageUsePtBr, posterSize, backDropSize);
        }

        public void execute() {
            ProcessData processData = new ProcessData();
            processData.execute();
        }

        public class ProcessData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                mAiringTodayRecyclerViewAdapter.loadNewData(getTVShows());
            }
        }
    }

    public void executeAirTodayList(boolean isRefreshing) {
        //HashMap<String,String> imagesSize = Utils.loadImagesPreferences(getContext());
        //String[] images = getActivity().getResources().getStringArray(R.array.poster_quality_values);

        mAiringTodayRecyclerViewAdapter = new AiringTodayRecyclerViewAdapter(getContext(), new ArrayList<TVShow>());
        mRecyclerView = (RecyclerView) airTodayFragment.findViewById(R.id.recycler_view_airing_today_list);
        mRecyclerView.setAdapter(mAiringTodayRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
            //Toast.makeText(getContext(), getString(R.string.error_no_internet_connection)+"- Airing Today", Toast.LENGTH_SHORT).show();
            Snackbar.make(mNoInternetConnection, getActivity().getString(R.string.error_no_internet_connection), Snackbar.LENGTH_LONG).show();
            if (mNoInternetConnection != null) Utils.setLayoutVisible(mNoInternetConnection);
            if (mRecyclerView != null) Utils.setLayoutInvisible(mRecyclerView);
            if (mLoadingTodayLayout != null) Utils.setLayoutInvisible(mLoadingTodayLayout);
        } else {
            // Set Layout Visible
            Utils.setLayoutVisible(mRecyclerView);

            // Create and generate the recycler view for list of results
            mRecyclerView = (RecyclerView) airTodayFragment.findViewById(R.id.recycler_view_airing_today_list);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            ProcessTVShowsAiringToday processTVShowsAiringToday = new ProcessTVShowsAiringToday(getContext(), true, AppConsts.POSTER_DEFAULT_SIZE, AppConsts.BACKDROP_DEFAULT_SIZE);
            processTVShowsAiringToday.execute();
        }
    }
}
