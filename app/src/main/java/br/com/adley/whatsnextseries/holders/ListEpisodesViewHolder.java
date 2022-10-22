package br.com.adley.whatsnextseries.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import br.com.adley.whatsnextseries.R;

/**
 * Created by Adley on 28/04/2016.
 * View Holder for List of seasons
 */
public class ListEpisodesViewHolder extends RecyclerView.ViewHolder{
    private TextView mEpisodeNumber;
    private TextView mEpisodeName;
    private ImageView mStillImg;
    private TextView mOverview;
    private TextView mAirDate;

    public TextView getEpisodeNumber() {
        return mEpisodeNumber;
    }

    public TextView getEpisodeName() {
        return mEpisodeName;
    }

    public ImageView getStillImg() {
        return mStillImg;
    }

    public TextView getOverview() {
        return mOverview;
    }

    public TextView getAirDate() {
        return mAirDate;
    }

    public ListEpisodesViewHolder(View view) {
        super(view);
        mEpisodeName = (TextView) view.findViewById(R.id.season_name);
        mEpisodeNumber = (TextView) view.findViewById(R.id.episode_number);
        mAirDate = (TextView) view.findViewById(R.id.air_date_text);
        mOverview = (TextView) view.findViewById(R.id.overview_text);
        mStillImg = (ImageView) view.findViewById(R.id.episode_image);
    }
}
