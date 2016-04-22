package br.com.adley.myseriesproject.activities;

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

public class TVShowDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshow_details);
        activateToolbarWithHomeEnabled();

        Intent intent = getIntent();
        TVShow show = (TVShow) intent.getSerializableExtra(TVSHOW_TRANSFER);
        TVShowDetails detailedShow = new TVShowDetails(show);
        TextView tvshowTitle = (TextView) findViewById(R.id.title_tvshow_detail);
        TextView tvshowSynopsis = (TextView) findViewById(R.id.synopsis_tvshow);
        ImageView tvshowPoster = (ImageView) findViewById(R.id.poster_tvshow);
        TextView tvshowRatingNumber = (TextView) findViewById(R.id.note_number_tvshow);
        RatingBar tvshowRatingBar = (RatingBar) findViewById(R.id.rating_tvshow);
        TextView tvshowNextEpisode = (TextView) findViewById(R.id.next_episode_tvshow);

        if (show.getName() != null && show.getOverview() != null) {
            if (tvshowTitle != null) {
                tvshowTitle.setText(show.getName());
            }

            if (tvshowSynopsis != null) {
                tvshowSynopsis.setText(Html.fromHtml(show.getOverview()));
            }

            if (tvshowRatingNumber != null) {
                LayerDrawable stars = (LayerDrawable) tvshowRatingBar.getProgressDrawable();
                //stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                tvshowRatingNumber.setText(Float.toString(show.getVoteAverage()));
            }

            if (tvshowRatingBar != null) {
                tvshowRatingBar.setRating(show.getVoteAverage()/2);
            }
                /*show.getNextEpisode() */
            if(!show.getFirstAirDate().isEmpty() && show.getFirstAirDate() != null && tvshowNextEpisode != null){
                String firstAirDate = show.getFirstAirDate();
                try {
                    String firstAirDateResult = Utils.convertStringDateToPtBr(firstAirDate);
                    tvshowNextEpisode.setText("Dia do lançamento: "+firstAirDateResult.toString());//getNextEpisode());
                } catch (ParseException e) {
                    tvshowNextEpisode.setText("Dia do lançamento: "+show.getFirstAirDate());//getNextEpisode());
                    e.printStackTrace();
                }
            }else if(tvshowNextEpisode != null){
                tvshowNextEpisode.setText(getString(R.string.warning_no_next_episode));
                tvshowNextEpisode.setMovementMethod(LinkMovementMethod.getInstance());
            }

            if (show.getPosterPath() != null) {
                Picasso.with(this).load(show.getPosterPath())
                        .error(R.drawable.placeholder)
                        .placeholder(R.drawable.placeholder)
                        .into(tvshowPoster);
            } else {
                Picasso.with(this).load(R.drawable.noimageplaceholder)
                        .error(R.drawable.placeholder)
                        .placeholder(R.drawable.placeholder)
                        .into(tvshowPoster);
            }

        }else{
            Toast.makeText(this, getString(R.string.generic_error_message), Toast.LENGTH_SHORT).show();
        }

    }

}
