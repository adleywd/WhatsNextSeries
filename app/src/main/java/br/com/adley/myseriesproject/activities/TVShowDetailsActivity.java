package br.com.adley.myseriesproject.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.library.TVShow;

public class TVShowDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshow_details);
        activateToolbarWithHomeEnabled();

        Intent intent = getIntent();
        TVShow show = (TVShow) intent.getSerializableExtra(TVSHOW_TRANSFER);

        TextView tvshowTitle = (TextView) findViewById(R.id.title_tvshow_detail);
        TextView tvshowSynopsis = (TextView) findViewById(R.id.synopsis_tvshow);
        ImageView tvshowPoster = (ImageView) findViewById(R.id.poster_tvshow);
        TextView tvshowRatingNumber = (TextView) findViewById(R.id.note_number_tvshow);
        RatingBar tvshowRatingBar = (RatingBar) findViewById(R.id.rating_tvshow);
        TextView tvshowNextEpisode = (TextView) findViewById(R.id.next_episode_tvshow);

        if (show.getName() != null && show.getSummary() != null) {
            if (tvshowTitle != null) {
                tvshowTitle.setText(show.getName());
            }

            if (tvshowSynopsis != null) {
                tvshowSynopsis.setText(Html.fromHtml(show.getSummary()));
            }

            if (tvshowRatingNumber != null) {
                LayerDrawable stars = (LayerDrawable) tvshowRatingBar.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                tvshowRatingNumber.setText("5.5");
            }

            if (tvshowRatingBar != null) {
                tvshowRatingBar.setRating((float)5/2);
            }

            if(show.getNextEpisode() != null && tvshowNextEpisode != null){
                    tvshowNextEpisode.setText(show.getNextEpisode());
            }else if(tvshowNextEpisode != null){
                tvshowNextEpisode.setText(getString(R.string.warning_no_next_episode));
                tvshowNextEpisode.setMovementMethod(LinkMovementMethod.getInstance());
            }

            if (show.getImageOriginal() != null) {
                Picasso.with(this).load(show.getImageOriginal())
                        .error(R.drawable.placeholder)
                        .placeholder(R.drawable.placeholder)
                        .into(tvshowPoster);
            } else if (show.getImageOriginal() == null && show.getImageMedium() != null) {
                Picasso.with(this).load(show.getImageMedium())
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
