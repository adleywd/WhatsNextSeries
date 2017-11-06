package br.com.adley.whatsnextseries.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;

import br.com.adley.whatsnextseries.R;
import br.com.adley.whatsnextseries.adapters.recyclerview.ListEpisodesRecyclerViewAdapter;
import br.com.adley.whatsnextseries.library.AppConsts;
import br.com.adley.whatsnextseries.models.remote.ApiUtils;
import br.com.adley.whatsnextseries.models.remote.TheMovieDBService;
import br.com.adley.whatsnextseries.models.retrofit.Episodes;
import br.com.adley.whatsnextseries.models.retrofit.SeasonEpisodes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EpisodesActivity extends BaseActivity {

    private TheMovieDBService mService;
    private RecyclerView mRecyclerViewEpisodes;
    private ListEpisodesRecyclerViewAdapter mEpisodesAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get Intent Data
        int showId = getIntent().getIntExtra(AppConsts.SHOW_ID_INTENT, 0);
        int seasonNumber = getIntent().getIntExtra(AppConsts.SEASON_NUMBER_INTENT, 0);
        String tvShowName = getIntent().getStringExtra(AppConsts.SHOW_NAME_INTENT);

        // Change Toolbar Title
        getSupportActionBar().setTitle(getString(R.string.title_activity_season_episodes) + " - " + tvShowName);
        mRecyclerViewEpisodes = (RecyclerView) findViewById(R.id.recycler_view_episodes_list);
        mRecyclerViewEpisodes.setLayoutManager(new LinearLayoutManager(this));
        mEpisodesAdapter = new ListEpisodesRecyclerViewAdapter(EpisodesActivity.this, new ArrayList<Episodes>());
        mRecyclerViewEpisodes.setAdapter(mEpisodesAdapter);

        // Load shared Configs
        loadConfigPreferences(this);

        String languageSelected = isLanguageUsePtBr() ? "pt-BR" : "en-US";
        mService = ApiUtils.getTMDBService(getString(R.string.url_base));
        mService.getEpisodesSeason(String.valueOf(showId), (String.valueOf(seasonNumber)), getString(R.string.api_key), languageSelected).enqueue(new Callback<SeasonEpisodes>() {
            @Override
            public void onResponse(Call<SeasonEpisodes> call, Response<SeasonEpisodes> response) {
                if (response.isSuccessful()){
                    Log.d("TAG_TESTE", "Season name: " + response.body().getName());
                    if (response.body() != null && response.body().getEpisodes() != null) {
                        mEpisodesAdapter.loadNewData(response.body().getEpisodes());
                    }

                }else{
                    Log.e("TAG_TESTE", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<SeasonEpisodes> call, Throwable t) {
                Log.e("TAG_TESTE", String.valueOf(t.getMessage()));
            }
        });

    }

}
