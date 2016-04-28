package br.com.adley.myseriesproject.tvmaze;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.activities.BaseActivity;

public class TVMazeDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshow_details);
        activateToolbarWithHomeEnabled();

        Intent intent = getIntent();
        TVMazeTVShowObject show = (TVMazeTVShowObject) intent.getSerializableExtra(TVSHOW_TRANSFER);

        TextView tvshowTitle = (TextView) findViewById(R.id.title_tvshow_detail);
        TextView tvshowSynopsis = (TextView) findViewById(R.id.synopsis_tvshow);
        ImageView tvshowPoster = (ImageView) findViewById(R.id.poster_tvshow);
        TextView tvshowRatingNumber = (TextView) findViewById(R.id.note_number_tvshow);
        TextView tvshowNextEpisode = (TextView) findViewById(R.id.next_episode_tvshow);

        if (show.getName() != null && show.getSummary() != null) {
            if (tvshowTitle != null) {
                tvshowTitle.setText(show.getName());
            }

            if (tvshowSynopsis != null) {
                tvshowSynopsis.setText(Html.fromHtml(show.getSummary()));
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
