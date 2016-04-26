package br.com.adley.myseriesproject.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.ParseException;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.library.Utils;
import br.com.adley.myseriesproject.models.TVShow;
import br.com.adley.myseriesproject.models.TVShowDetails;
import br.com.adley.myseriesproject.themoviedb.GetTVShowDetailsJsonData;
import br.com.adley.myseriesproject.themoviedb.GetTVShowSeasonJsonData;

public class TVShowDetailsActivity extends BaseActivity {

    private TVShowDetails mTVShowDetails;
    private TextView mTVShowTitle;
    private TextView mTVShowSynopsis;
    private ImageView mTVShowPoster;
    private TextView TVShowRatingNumber;
    private RatingBar TVShowRatingBar;
    private TextView TVShowNextEpisode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshow_details);
        activateToolbarWithHomeEnabled();

        // Get View Elements
        mTVShowTitle = (TextView) findViewById(R.id.title_tvshow_detail);
        mTVShowSynopsis = (TextView) findViewById(R.id.synopsis_tvshow);
        mTVShowPoster = (ImageView) findViewById(R.id.poster_tvshow);
        TVShowRatingNumber = (TextView) findViewById(R.id.note_number_tvshow);
        TVShowRatingBar = (RatingBar) findViewById(R.id.rating_tvshow);
        TVShowNextEpisode = (TextView) findViewById(R.id.next_episode_tvshow);

        Intent intent = getIntent();
        TVShow tvshow = (TVShow) intent.getSerializableExtra(TVSHOW_TRANSFER);

        //Get Show Details Data
        ProcessTVShowsDetails processTVShowsDetails = new ProcessTVShowsDetails(tvshow);
        processTVShowsDetails.execute();

    }

    // Process and execute data into recycler view
    public class ProcessTVShowsDetails extends GetTVShowDetailsJsonData {

        private ProgressDialog progress;

        public ProcessTVShowsDetails(TVShow show) {
            super(show, TVShowDetailsActivity.this);
        }

        public void execute() {
            // Start loading dialog
            progress = ProgressDialog.show(TVShowDetailsActivity.this, "Aguarde...", "Estamos carregando os dados da série.", true);
            // Start process data (download and get)
            ProcessData processData = new ProcessData();
            processData.execute();
        }

        public class ProcessData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                mTVShowDetails = getTVShowsDetails();
                //Process SeasonData
                /*for(int seasonNumber = 1; seasonNumber <= mTVShowDetails.getNumberOfSeasons(); seasonNumber++) {
                    ProcessSeason processSeason = new ProcessSeason(mTVShowDetails.getId(), seasonNumber);
                    processSeason.execute();
                }*/
                bindParams();
                // Close loading dialog.
                if (progress.isShowing()) progress.dismiss();
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
            }
        }
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

            if (TVShowRatingNumber != null) {
                LayerDrawable stars = (LayerDrawable) TVShowRatingBar.getProgressDrawable();
                //stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                TVShowRatingNumber.setText(Float.toString(mTVShowDetails.getVoteAverage()));
            }

            if (TVShowRatingBar != null) {
                TVShowRatingBar.setRating(mTVShowDetails.getVoteAverage() / 2);
            }
                /*show.getNextEpisode() */
            if (!mTVShowDetails.getFirstAirDate().isEmpty() && mTVShowDetails.getFirstAirDate() != null && TVShowNextEpisode != null) {
                String firstAirDate = mTVShowDetails.getFirstAirDate();
                try {
                    String firstAirDateResult = Utils.convertStringDateToPtBr(firstAirDate);
                    TVShowNextEpisode.setText("Dia do lançamento: " + firstAirDateResult.toString());//getNextEpisode());
                } catch (ParseException e) {
                    TVShowNextEpisode.setText("Dia do lançamento: " + mTVShowDetails.getFirstAirDate());//getNextEpisode());
                    e.printStackTrace();
                }
            } else if (TVShowNextEpisode != null) {
                TVShowNextEpisode.setText(getString(R.string.warning_no_next_episode));
                TVShowNextEpisode.setMovementMethod(LinkMovementMethod.getInstance());
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
