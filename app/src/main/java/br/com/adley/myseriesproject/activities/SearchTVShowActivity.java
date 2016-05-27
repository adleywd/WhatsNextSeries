package br.com.adley.myseriesproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.library.AppConsts;
import br.com.adley.myseriesproject.library.RecyclerItemClickListener;
import br.com.adley.myseriesproject.library.Utils;
import br.com.adley.myseriesproject.library.enums.DownloadStatus;
import br.com.adley.myseriesproject.models.TVShow;
import br.com.adley.myseriesproject.themoviedb.adapters.SearchShowRecyclerViewAdapter;
import br.com.adley.myseriesproject.themoviedb.service.GetTVShowJsonData;

public class SearchTVShowActivity extends BaseActivity {

    private Button idSearchButton;
    private EditText idInputNameShow;
    private static final String LOG_TAG = "MainActiviry";
    private RecyclerView mRecyclerView;
    private SearchShowRecyclerViewAdapter mSearchShowRecyclerViewAdapter;
    private View mNoInternetConnection;
    private View mTVShowSearchLayout;
    private View mLoadingBarLayout;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshow_search);

        activateToolbarWithHomeEnabled();


        //Ad Config
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, getString(R.string.application_id_ad));

        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
        mAdView = (AdView) findViewById(R.id.ad_view_searchshow);

        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(getString(R.string.device_id_test1))
                .build();

        // Start loading the ad in the background.
        mAdView.setVisibility(View.VISIBLE);
        mAdView.loadAd(adRequest);

        // Get activity layout
        mTVShowSearchLayout = findViewById(R.id.tvshow_search_layout);

        // Get ProgressBar layout and set invisible
        mLoadingBarLayout = findViewById(R.id.loading_panel);
        Utils.setLayoutInvisible(mLoadingBarLayout);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_home);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSearchShowRecyclerViewAdapter = new SearchShowRecyclerViewAdapter(SearchTVShowActivity.this, new ArrayList<TVShow>());
        mRecyclerView.setAdapter(mSearchShowRecyclerViewAdapter);

        // Create the touch for the recyclerview list
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Creates and configure intent to call tv show details activity
                Intent intent = new Intent(SearchTVShowActivity.this, DetailsTVShowActivity.class);
                intent.putExtra(AppConsts.TVSHOW_TRANSFER, mSearchShowRecyclerViewAdapter.getTVShow(position));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //Creates and configure intent to call tv show details activity
                //Intent intent = new Intent(SearchTVShowActivity.this, DetailsTVShowActivity.class);
                //intent.putExtra(TVSHOW_TRANSFER, mSearchShowRecyclerViewAdapter.getTVShow(position));
                //startActivity(intent);
            }

        }));


        idSearchButton = (Button) findViewById(R.id.id_search_button);
        idInputNameShow = (EditText) findViewById(R.id.id_input_name_serie);
        mNoInternetConnection = findViewById(R.id.no_internet_connection);
        mTVShowSearchLayout = findViewById(R.id.tvshow_search_layout);
        // Load Preferences from BaseActivity
        loadConfigPreferences();

        idSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeSearchShow(v);
            }
        });

        idInputNameShow.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    switch (keyCode){
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            executeSearchShow(v);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }

    private void executeSearchShow(View v){
        //Hide keyboard when button was clicked.
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        // Set No Internet Layout Invisible
        Utils.setLayoutVisible(mRecyclerView);
        Utils.setLayoutInvisible(mNoInternetConnection);
        //Check the phone connection status.
        if (!Utils.checkAppConnectionStatus(SearchTVShowActivity.this)) {
            //Toast.makeText(SearchTVShowActivity.this, getString(R.string.error_no_internet_connection), Toast.LENGTH_SHORT).show();
            Snackbar.make(mNoInternetConnection, getString(R.string.error_no_internet_connection), Snackbar.LENGTH_LONG).show();
            Utils.setLayoutInvisible(mRecyclerView);
            Utils.setLayoutVisible(mNoInternetConnection);
        } else if (idInputNameShow.getText().toString().isEmpty()) {
            Snackbar.make(mTVShowSearchLayout,  getString(R.string.error_blank_search_field), Snackbar.LENGTH_LONG).show();
        } else {
            // Set loading layout visible
            Utils.setLayoutVisible(mLoadingBarLayout);

            // Create and generate the recycler view for list of results
            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_home);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(SearchTVShowActivity.this));
            ProcessTVShows processTVShows = new ProcessTVShows(idInputNameShow.getText().toString());
            processTVShows.execute();
        }
    }

    // Process and execute data into recycler view
    public class ProcessTVShows extends GetTVShowJsonData {

        public ProcessTVShows(String showName) {
            super(showName, SearchTVShowActivity.this, isLanguageUsePtBr());
        }

        public void execute() {
            ProcessData processData = new ProcessData();
            processData.execute();
        }

        public class ProcessData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                Utils.setLayoutInvisible(mLoadingBarLayout);
                if(getTVShows().size() == 0 && getDownloadStatus() == DownloadStatus.OK){
                    Snackbar.make(mTVShowSearchLayout,  getString(R.string.error_no_series_found), Snackbar.LENGTH_LONG).show();
                }else if(getDownloadStatus() != DownloadStatus.OK){
                    Snackbar.make(mTVShowSearchLayout,  getString(R.string.error_no_internet_connection), Snackbar.LENGTH_LONG).show();
                }else{
                    mSearchShowRecyclerViewAdapter.loadNewData(getTVShows());
                }
            }
        }
    }
}
