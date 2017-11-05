package br.com.adley.whatsnextseries.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import br.com.adley.whatsnextseries.R;
import br.com.adley.whatsnextseries.models.remote.ApiUtils;
import br.com.adley.whatsnextseries.models.remote.TheMovieDBService;
import br.com.adley.whatsnextseries.models.retrofit.SeasonEpisodes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeasonEpisodesActivity extends AppCompatActivity {

    private TheMovieDBService mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season_episodes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mService = ApiUtils.getTMDBService(getString(R.string.url_base));

        mService.getEpisodesSeason("1402", "8", getString(R.string.api_key), "en-US").enqueue(new Callback<SeasonEpisodes>() {
            @Override
            public void onResponse(Call<SeasonEpisodes> call, Response<SeasonEpisodes> response) {
                if (response.isSuccessful()){
                    Log.d("TAG_TESTE", "Season Number: " + response.body().getSeasonNumber());
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
