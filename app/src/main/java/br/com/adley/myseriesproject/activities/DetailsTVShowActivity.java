package br.com.adley.myseriesproject.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.library.RecyclerItemClickListener;
import br.com.adley.myseriesproject.library.Utils;
import br.com.adley.myseriesproject.models.TVShow;
import br.com.adley.myseriesproject.models.TVShowDetails;
import br.com.adley.myseriesproject.models.TVShowSeasonEpisodes;
import br.com.adley.myseriesproject.models.TVShowSeasons;
import br.com.adley.myseriesproject.themoviedb.GetTVShowDetailsJsonData;
import br.com.adley.myseriesproject.themoviedb.GetTVShowSeasonJsonData;
import br.com.adley.myseriesproject.themoviedb.ListSeasonRecyclerViewAdapter;

public class DetailsTVShowActivity extends BaseActivity {

    private TVShowDetails mTVShowDetails;
    private TextView mTVShowTitle;
    private TextView mTVShowSynopsis;
    private View mTVShowPoster;
    private TextView mTVShowRatingNumber;
    private TextView mTVShowNextDateEpisode;
    private TextView mTVShowNextNameEpisode;
    private TextView mTVShowDetailsNoSeason;
    private TVShow mTVShow;
    private List<TVShowSeasons> mTVShowSeasons;
    private ProgressDialog mProgress;
    private RecyclerView mRecyclerViewSeason;
    private ListSeasonRecyclerViewAdapter mListSeasonRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshow_details);
        activateToolbarWithHomeEnabled();

        // Get View Elements
        mTVShowTitle = (TextView) findViewById(R.id.title_tvshow_detail);
        mTVShowSynopsis = (TextView) findViewById(R.id.synopsis_tvshow);
        mTVShowPoster = findViewById(R.id.introduction_layout);
        mTVShowRatingNumber = (TextView) findViewById(R.id.note_number_tvshow);
        mTVShowNextNameEpisode = (TextView) findViewById(R.id.next_episode_name);
        mTVShowNextDateEpisode = (TextView) findViewById(R.id.next_episode_date);
        mTVShowDetailsNoSeason = (TextView) findViewById(R.id.no_list_season_error);
        mTVShowSeasons = new ArrayList<>();
        mRecyclerViewSeason = (RecyclerView) findViewById(R.id.recycler_view_season_list);
        mRecyclerViewSeason.setLayoutManager(new LinearLayoutManager(this));
        mListSeasonRecyclerViewAdapter = new ListSeasonRecyclerViewAdapter(DetailsTVShowActivity.this, new ArrayList<TVShowSeasons>());
        mRecyclerViewSeason.setAdapter(mListSeasonRecyclerViewAdapter);

        mRecyclerViewSeason.addOnItemTouchListener(new RecyclerItemClickListener(this,
                mRecyclerViewSeason, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(DetailsTVShowActivity.this, "Não implementado.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(DetailsTVShowActivity.this, "Não implementado.", Toast.LENGTH_SHORT).show();
            }
        }));

        Intent intent = getIntent();
        mTVShow = (TVShow) intent.getSerializableExtra(TVSHOW_TRANSFER);

        //Get Show Details Data
        ProcessTVShowsDetails processTVShowsDetails = new ProcessTVShowsDetails(mTVShow);
        processTVShowsDetails.execute();

    }

    // Process and execute data into recycler view
    public class ProcessTVShowsDetails extends GetTVShowDetailsJsonData {
        private ProcessData processData;

        public ProcessTVShowsDetails(TVShow show) {
            super(show, DetailsTVShowActivity.this);
        }

        public void execute() {
            // Start process data (download and get)
            processData = new ProcessData();
            processData.execute();

            // Start loading dialog
            mProgress = Utils.configureProgressDialog("Aguarde...", "Carregando os dados da série...", true, true, DetailsTVShowActivity.this);
            mProgress.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    processData.cancel(true);
                    DetailsTVShowActivity.this.finish();
                }
            });
            mProgress.show();
        }

        public class ProcessData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                mTVShowDetails = getTVShowsDetails();
                //Get and Process SeasonData
                if (mTVShowDetails.getNumberOfSeasons() > 0) {
                    Utils.setLayoutInvisible(mTVShowDetailsNoSeason);
                    mProgress.setMessage("Carregando as temporadas...");
                    for (int seasonNumber = 1; seasonNumber <= mTVShowDetails.getNumberOfSeasons(); seasonNumber++) {
                        ProcessSeason processSeason = new ProcessSeason(mTVShowDetails.getId(), seasonNumber);
                        processSeason.execute();
                    }
                } else {
                    mProgress.dismiss();
                    bindParams();
                }
            }
        }
    }

    // Process Season Data
    public class ProcessSeason extends GetTVShowSeasonJsonData {

        public ProcessSeason(int showId, int serieNumber) {
            super(showId, serieNumber, DetailsTVShowActivity.this);
        }

        public void execute() {
            // Start process data (download and get)
            ProcessData processData = new ProcessData();
            processData.execute();
        }

        public class ProcessData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                mTVShowSeasons.add(getTVShowSeasons());
                if (getSeasonNumberTVShow() == mTVShowDetails.getNumberOfSeasons()) {
                    mListSeasonRecyclerViewAdapter.loadNewData(mTVShowSeasons);
                    mProgress.dismiss();
                    bindParams();
                }
            }
        }
    }

    /**
     * Bind Parameters after download data.
     */
    private void bindParams() {
        if (mProgress.isShowing()) mProgress.dismiss();
        if (mTVShowDetails.getOriginalName() != null && mTVShowDetails.getOverview() != null) {
            if (mTVShowTitle != null) {
                mTVShowTitle.setText(mTVShowDetails.getOriginalName());
            }

            if (mTVShowSynopsis != null) {
                mTVShowSynopsis.setText(Html.fromHtml(mTVShowDetails.getOverview()));
            }

            if (mTVShowRatingNumber != null) {
                Locale ptBr = new Locale("pt", "BR");
                mTVShowRatingNumber.setText(String.format(ptBr, "%.2f", mTVShowDetails.getVoteAverage()));
            }

            if (!mTVShowDetails.getFirstAirDate().isEmpty() && mTVShowDetails.getFirstAirDate() != null
                    && mTVShowNextDateEpisode != null && mTVShowDetails.getNumberOfSeasons() > 0) {
                try {
                    TVShowSeasonEpisodes lastSeasonEpisode = null;
                    SimpleDateFormat sdfPtBr = new SimpleDateFormat("dd/MM/yyyy");
                    Date dateTimeNow = sdfPtBr.parse(Utils.getDateTimeNowPtBr(false));
                    List<TVShowSeasonEpisodes> lastSeasonEpisodes = mTVShowSeasons.get(mTVShowDetails.getNumberOfSeasons() - 1).getEpisodes();
                    for (TVShowSeasonEpisodes episode : lastSeasonEpisodes) {
                        Date episodeAirDate = sdfPtBr.parse(Utils.convertStringDateToPtBr(episode.getAirDate()));
                        if (episodeAirDate.after(dateTimeNow) || episodeAirDate.equals(dateTimeNow)) {
                            lastSeasonEpisode = episode;
                            break;
                        }
                    }
                    if (lastSeasonEpisode != null) {
                        mTVShowNextDateEpisode.setText(Utils.convertStringDateToPtBr(lastSeasonEpisode.getAirDate()));
                        mTVShowNextNameEpisode.setText(String.valueOf(lastSeasonEpisode.getEpisodeName()));
                    } else {
                        mTVShowNextDateEpisode.setText(getString(R.string.no_next_episode_info));
                        mTVShowNextNameEpisode.setText(getString(R.string.no_next_episode_info));
                    }
                } catch (ParseException e) {
                    mTVShowNextDateEpisode.setText(getString(R.string.generic_error_message));
                    e.printStackTrace();
                }
            } else {
                mTVShowNextDateEpisode.setText(getString(R.string.warning_no_next_episode));
                mTVShowNextNameEpisode.setText(getString(R.string.warning_no_next_episode));
                if (mTVShowDetails.getNumberOfSeasons() == 0) {
                    Utils.setLayoutVisible(mTVShowDetailsNoSeason);
                }
                //mTVShowNextDateEpisode.setMovementMethod(LinkMovementMethod.getInstance());
            }
            //TODO: Set Image Background.

        } else {
            Toast.makeText(this, getString(R.string.generic_error_message), Toast.LENGTH_SHORT).show();
        }
    }
}
