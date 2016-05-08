package br.com.adley.myseriesproject.themoviedb.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.models.TVShowSeasons;
import br.com.adley.myseriesproject.themoviedb.holders.ListSeasonViewHolder;

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
            if(String.valueOf(season.getSeasonName()).contains("Season")) season.setSeasonName("pt-br");
            holder.getSeasonName().setText(String.valueOf(season.getSeasonName()));
            holder.getEpisodes().setText(String.valueOf("nº episódios: "+season.getEpisodes().size()));
        }
    }

    //
    public void loadNewData(List<TVShowSeasons> newSeasons) {
        mSeasonsList = newSeasons;
        mSeasonsListReverse = newSeasons;
        Collections.reverse(mSeasonsListReverse);
        notifyDataSetChanged();
    }

    //
    public TVShowSeasons getSeason(int position) {
        return (null != mSeasonsList ? mSeasonsList.get(position) : null);
    }
}
