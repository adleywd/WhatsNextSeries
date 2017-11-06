package br.com.adley.whatsnextseries.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.adley.whatsnextseries.R;

/**
 * Created by Adley on 28/04/2016.
 * View Holder for List of seasons
 */
public class ListEpisodesViewHolder extends RecyclerView.ViewHolder{
    protected TextView mEpisodeNumber;
    protected TextView mSeasonName;

    public TextView getEpisodeNumber() {
        return mEpisodeNumber;
    }

    public void setEpisodeNumber(TextView mEpisodeNumber) {
        this.mEpisodeNumber = mEpisodeNumber;
    }

    public TextView getSeasonName() {
        return mSeasonName;
    }

    public void setSeasonName(TextView mSeasonName) {
        this.mSeasonName = mSeasonName;
    }

    public ListEpisodesViewHolder(View view) {
        super(view);
        this.mSeasonName = (TextView) view.findViewById(R.id.season_name);
        this.mEpisodeNumber = (TextView) view.findViewById(R.id.episode_number);
    }
}
