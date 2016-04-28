package br.com.adley.myseriesproject.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.library.Utils;
import br.com.adley.myseriesproject.models.TVShow;
import br.com.adley.myseriesproject.models.TVShowDetails;
import br.com.adley.myseriesproject.models.TVShowSeasons;
import br.com.adley.myseriesproject.themoviedb.GetTVShowDetailsJsonData;
import br.com.adley.myseriesproject.themoviedb.GetTVShowSeasonJsonData;

public class TVShowDetailsActivity extends BaseActivity {

    private TVShowDetails mTVShowDetails;
    private TextView mTVShowTitle;
    private TextView mTVShowSynopsis;
    private ImageView mTVShowPoster;
    private TextView mTVShowRatingNumber;
    private TextView mTVShowNextEpisode;
    private ListView mSeasonsListView;
    private List<TVShowSeasons> mTVShowSeasons;
    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshow_details);
        activateToolbarWithHomeEnabled();

        // Get View Elements
        mTVShowTitle = (TextView) findViewById(R.id.title_tvshow_detail);
        mTVShowSynopsis = (TextView) findViewById(R.id.synopsis_tvshow);
        mTVShowPoster = (ImageView) findViewById(R.id.poster_tvshow);
        mTVShowRatingNumber = (TextView) findViewById(R.id.note_number_tvshow);
        mTVShowNextEpisode = (TextView) findViewById(R.id.next_episode_tvshow);
        mSeasonsListView = (ListView) findViewById(R.id.list_seasons);
        mTVShowSeasons = new ArrayList<>();

        Intent intent = getIntent();
        TVShow tvshow = (TVShow) intent.getSerializableExtra(TVSHOW_TRANSFER);

        //Get Show Details Data
        ProcessTVShowsDetails processTVShowsDetails = new ProcessTVShowsDetails(tvshow);
        processTVShowsDetails.execute();

    }

    // Process and execute data into recycler view
    public class ProcessTVShowsDetails extends GetTVShowDetailsJsonData {

        public ProcessTVShowsDetails(TVShow show) {
            super(show, TVShowDetailsActivity.this);
        }

        public void execute() {
            // Start loading dialog
            mProgress = ProgressDialog.show(TVShowDetailsActivity.this, "Aguarde...", "Carregando os dados da série...", true);
            // Start process data (download and get)
            ProcessData processData = new ProcessData();
            processData.execute();
        }

        public class ProcessData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                mTVShowDetails = getTVShowsDetails();

                //Get and Process SeasonData
                mProgress = ProgressDialog.show(TVShowDetailsActivity.this, "Aguarde...", "Carregando os dados das temporadas...", true);
                for(int seasonNumber = 1; seasonNumber <= mTVShowDetails.getNumberOfSeasons(); seasonNumber++) {
                    ProcessSeason processSeason = new ProcessSeason(mTVShowDetails.getId(), seasonNumber);
                    processSeason.execute();

                }
                bindParams();
                // Close loading dialog.
                if (mProgress.isShowing()) mProgress.dismiss();
            }
        }
    }

    // Process Season Data
    public class ProcessSeason extends GetTVShowSeasonJsonData {

        public ProcessSeason(int showId, int serieNumber) {
            super(showId, serieNumber, TVShowDetailsActivity.this);
        }

        public void execute() {
            // Start process data (download and get)
            ProcessData processData = new ProcessData();
            processData.execute();
        }

        public class ProcessData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                Log.v("TAG_TEST", getTVShowSeasons().toString());
            }
        }
    }

    /**
     * Bind Season List Parameters after download data.
     */
    private void bindSeasonList(){
        List items = new ArrayList<>();
        for (int i = 0; i < mTVShowSeasons.size(); i++){
            items.add(mTVShowSeasons.get(i).getSeasonName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);

        mSeasonsListView.setAdapter(adapter);
    }

    /**
     * Bind Parameters after download data.
     */
    private void bindParams() {
        if (mTVShowDetails.getName() != null && mTVShowDetails.getOverview() != null) {
            if (mTVShowTitle != null) {
                mTVShowTitle.setText(mTVShowDetails.getName());
            }

            if (mTVShowSynopsis != null) {
                mTVShowSynopsis.setText(Html.fromHtml(mTVShowDetails.getOverview()));
            }

            if (mTVShowRatingNumber != null) {
                mTVShowRatingNumber.setText(Float.toString(mTVShowDetails.getVoteAverage()));
            }

            if (!mTVShowDetails.getFirstAirDate().isEmpty() && mTVShowDetails.getFirstAirDate() != null && mTVShowNextEpisode != null) {
                String firstAirDate = mTVShowDetails.getFirstAirDate();
                try {
                    String firstAirDateResult = Utils.convertStringDateToPtBr(firstAirDate);
                    mTVShowNextEpisode.setText("Dia do lançamento: " + firstAirDateResult.toString());//getNextEpisode());
                } catch (ParseException e) {
                    mTVShowNextEpisode.setText("Dia do lançamento: " + mTVShowDetails.getFirstAirDate());//getNextEpisode());
                    e.printStackTrace();
                }
            } else if (mTVShowNextEpisode != null) {
                mTVShowNextEpisode.setText(getString(R.string.warning_no_next_episode));
                mTVShowNextEpisode.setMovementMethod(LinkMovementMethod.getInstance());
            }

            if (mTVShowDetails.getPosterPath() != null) {
                Picasso.with(this).load(mTVShowDetails.getPosterPath())
                        .error(R.drawable.placeholder)
                        .placeholder(R.drawable.placeholder)
                        .into(mTVShowPoster);
            } else {
                Picasso.with(this).load(R.drawable.noimageplaceholder)
                        .error(R.drawable.placeholder)
                        .placeholder(R.drawable.placeholder)
                        .into(mTVShowPoster);
            }

        } else {
            Toast.makeText(this, getString(R.string.generic_error_message), Toast.LENGTH_SHORT).show();
        }
    }

}
