package br.com.adley.myseriesproject.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
    private ImageView mTVShowPoster;
    private TextView mTVShowRatingNumber;
    private TextView mTVShowNextDateEpisode;
    private TextView mTVShowNextNameEpisode;
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
        mTVShowPoster = (ImageView) findViewById(R.id.poster_tvshow);
        mTVShowRatingNumber = (TextView) findViewById(R.id.note_number_tvshow);
        mTVShowNextNameEpisode = (TextView) findViewById(R.id.next_episode_name);
        mTVShowNextDateEpisode = (TextView) findViewById(R.id.next_episode_date);
        mTVShowSeasons = new ArrayList<>();
        mRecyclerViewSeason = (RecyclerView) findViewById(R.id.recycler_view_season_list);
        mRecyclerViewSeason.setLayoutManager(new LinearLayoutManager(this));
        mListSeasonRecyclerViewAdapter = new ListSeasonRecyclerViewAdapter(DetailsTVShowActivity.this, new ArrayList<TVShowSeasons>());
        mRecyclerViewSeason.setAdapter(mListSeasonRecyclerViewAdapter);

        mRecyclerViewSeason.addOnItemTouchListener(new RecyclerItemClickListener(this,
                mRecyclerViewSeason, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(DetailsTVShowActivity.this, "Normal tap", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(DetailsTVShowActivity.this, "Long tap", Toast.LENGTH_SHORT).show();
            }
        }));

        Intent intent = getIntent();
        TVShow tvshow = (TVShow) intent.getSerializableExtra(TVSHOW_TRANSFER);

        //Get Show Details Data
        ProcessTVShowsDetails processTVShowsDetails = new ProcessTVShowsDetails(tvshow);
        processTVShowsDetails.execute();

    }

    // Process and execute data into recycler view
    public class ProcessTVShowsDetails extends GetTVShowDetailsJsonData {

        public ProcessTVShowsDetails(TVShow show) {
            super(show, DetailsTVShowActivity.this);
        }

        public void execute() {
            // Start loading dialog
            mProgress = ProgressDialog.show(DetailsTVShowActivity.this, "Aguarde...", "Carregando os dados da série...", true);
            // Start process data (download and get)
            ProcessData processData = new ProcessData();
            processData.execute();
        }

        public class ProcessData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                mTVShowDetails = getTVShowsDetails();
                mProgress.dismiss();
                //Get and Process SeasonData
                mProgress = ProgressDialog.show(DetailsTVShowActivity.this, "Aguarde...", "Carregando os dados das temporadas...", true);
                for(int seasonNumber = 1; seasonNumber <= mTVShowDetails.getNumberOfSeasons(); seasonNumber++) {
                    ProcessSeason processSeason = new ProcessSeason(mTVShowDetails.getId(), seasonNumber);
                    processSeason.execute();

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
                if(getSeasonNumberTVShow() == mTVShowDetails.getNumberOfSeasons()){
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
        if (mTVShowDetails.getName() != null && mTVShowDetails.getOverview() != null) {
            if (mTVShowTitle != null) {
                mTVShowTitle.setText(mTVShowDetails.getName());
            }

            if (mTVShowSynopsis != null) {
                mTVShowSynopsis.setText(Html.fromHtml(mTVShowDetails.getOverview()));
            }

            if (mTVShowRatingNumber != null) {
                Locale ptBr = new Locale("pt", "BR");
                mTVShowRatingNumber.setText(String.format(ptBr,"%.2f", mTVShowDetails.getVoteAverage()));
            }

            if (!mTVShowDetails.getFirstAirDate().isEmpty() && mTVShowDetails.getFirstAirDate() != null && mTVShowNextDateEpisode != null) {
                try {
                    String nextEpisode = null;
                    TVShowSeasonEpisodes lastSeasonEpisode = null;
                    SimpleDateFormat sdfPtBr = new SimpleDateFormat("dd/MM/yyyy");
                    Date dateTimeNow = sdfPtBr.parse(Utils.getDateTimeNowPtBr(false));
                    List<TVShowSeasonEpisodes> lastSeasonEpisodes = mTVShowSeasons.get(mTVShowDetails.getNumberOfSeasons()-1).getEpisodes();
                    for(TVShowSeasonEpisodes episode : lastSeasonEpisodes){
                        Date episodeAirDate = sdfPtBr.parse(Utils.convertStringDateToPtBr(episode.getAirDate()));
                        if(episodeAirDate.after(dateTimeNow)|| episodeAirDate.equals(dateTimeNow)){
                            nextEpisode = Utils.convertStringDateToPtBr(episode.getAirDate());
                            lastSeasonEpisode = episode;
                            break;
                        }
                    }
                    if(lastSeasonEpisode != null){
                        mTVShowNextDateEpisode.setText(Utils.convertStringDateToPtBr(lastSeasonEpisode.getAirDate()));
                        if(!lastSeasonEpisode.getEpisodeName().isEmpty() || lastSeasonEpisode != null){
                            mTVShowNextNameEpisode.setText(String.valueOf(lastSeasonEpisode.getEpisodeName()));
                        }else{
                            mTVShowNextNameEpisode.setText(getString(R.string.no_next_episode_info));
                        }
                    }else{
                        mTVShowNextDateEpisode.setText(getString(R.string.no_next_episode_info));
                    }
                } catch (ParseException e) {
                    mTVShowNextDateEpisode.setText(getString(R.string.generic_error_message));//getNextEpisode());
                    e.printStackTrace();
                }
            } else if (mTVShowNextDateEpisode != null) {
                mTVShowNextDateEpisode.setText(getString(R.string.warning_no_next_episode));
                mTVShowNextDateEpisode.setMovementMethod(LinkMovementMethod.getInstance());
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
