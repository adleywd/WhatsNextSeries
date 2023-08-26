package br.com.adley.whatsnextseries.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.adley.whatsnextseries.R;

/**
 * Created by Adley on 28/04/2016.
 * Veiw Holder for List of seasons
 */
public class ListSeasonViewHolder extends RecyclerView.ViewHolder{
    protected TextView mEpisodes;
    protected TextView mSeasonName;

    public TextView getEpisodes() {
        return mEpisodes;
    }

    public void setEpisodes(TextView episodes) {
        mEpisodes = episodes;
    }

    public TextView getSeasonName() {
        return mSeasonName;
    }

    public void setSeasonName(TextView seasonName) {
        mSeasonName = seasonName;
    }

    public ListSeasonViewHolder(View view) {
        super(view);
        this.mSeasonName = (TextView) view.findViewById(R.id.season_name);
        this.mEpisodes = (TextView) view.findViewById(R.id.episodes_count);
    }
}
