package br.com.adley.whatsnextseries.adapters.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.adley.whatsnextseries.R;
import br.com.adley.whatsnextseries.models.TVShowSeasons;
import br.com.adley.whatsnextseries.holders.ListSeasonViewHolder;

/**
 * Created by adley on 29/04/16.
 * Adapter to list of seasons
 */
public class ListSeasonRecyclerViewAdapter extends RecyclerView.Adapter<ListSeasonViewHolder>{
    private List<TVShowSeasons> mSeasonsList;
    private List<TVShowSeasons> mSeasonsListReverse;
    private Context mContext;

    //
    public ListSeasonRecyclerViewAdapter(Context context, List<TVShowSeasons> seasonsList) {
        this.mContext = context;
        this.mSeasonsList = seasonsList;
    }

    //
    @Override
    public ListSeasonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shared_list_seasons, null);
        ListSeasonViewHolder listSeasonViewHolder = new ListSeasonViewHolder(view);
        return listSeasonViewHolder;
    }

    //
    @Override
    public int getItemCount() {
        return (mSeasonsList != null ? mSeasonsList.size() : 0);
    }

    //
    @Override
    public void onBindViewHolder(ListSeasonViewHolder holder, int position) {
        TVShowSeasons season = mSeasonsListReverse.get(position);
        if(season != null) {
            if(season.getSeasonName() != null || !season.getSeasonName().isEmpty()){
                if(season.getSeasonNumber() == 0){
                    season.setSeasonName(mContext.getString(R.string.season_specials));
                }else {
                    season.setSeasonName(mContext.getString(R.string.season_title, season.getSeasonNumber()));
                }
            }
            holder.getSeasonName().setText(String.valueOf(season.getSeasonName()));
            holder.getEpisodes().setText(mContext.getString(R.string.season_number_episodes, season.getEpisodes().size()));
        }
    }

    //
    public void loadNewData(List<TVShowSeasons> newSeasons) {
        mSeasonsList = newSeasons;
        mSeasonsListReverse = new ArrayList<>(newSeasons);
        Collections.reverse(mSeasonsListReverse);
        notifyDataSetChanged();
    }

    //
    public TVShowSeasons getSeason(int position) {
        return (null != mSeasonsListReverse ? mSeasonsListReverse.get(position) : null);
    }
}
