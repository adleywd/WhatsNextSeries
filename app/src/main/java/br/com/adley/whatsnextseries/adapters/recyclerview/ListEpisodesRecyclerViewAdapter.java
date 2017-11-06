package br.com.adley.whatsnextseries.adapters.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.adley.whatsnextseries.R;
import br.com.adley.whatsnextseries.holders.ListEpisodesViewHolder;
import br.com.adley.whatsnextseries.models.retrofit.Episodes;

/**
 * Created by adley on 29/04/16.
 * Adapter to list of seasons
 */
public class ListEpisodesRecyclerViewAdapter extends RecyclerView.Adapter<ListEpisodesViewHolder>{
    private List<Episodes> mEpisodesList;
    private Context mContext;

    public ListEpisodesRecyclerViewAdapter(Context context, List<Episodes> episodesList) {
        this.mContext = context;
        this.mEpisodesList = episodesList;
    }

    @Override
    public ListEpisodesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shared_list_episodes, null);
        return new ListEpisodesViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return (mEpisodesList != null ? mEpisodesList.size() : 0);
    }

    @Override
    public void onBindViewHolder(ListEpisodesViewHolder holder, int position) {
        Episodes episodes = mEpisodesList.get(position);
        if(episodes != null) {
            holder.getEpisodeNumber().setText(String.valueOf(episodes.getEpisodeNumber()));
            holder.getSeasonName().setText(String.valueOf(episodes.getName()));
        }
    }

    //
    public void loadNewData(List<Episodes> newEpisodes) {
        mEpisodesList = newEpisodes;
        notifyDataSetChanged();
    }
}
