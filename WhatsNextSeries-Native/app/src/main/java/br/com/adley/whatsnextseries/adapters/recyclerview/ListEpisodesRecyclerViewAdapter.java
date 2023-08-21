package br.com.adley.whatsnextseries.adapters.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.List;

import br.com.adley.whatsnextseries.R;
import br.com.adley.whatsnextseries.holders.ListEpisodesViewHolder;
import br.com.adley.whatsnextseries.library.Utils;
import br.com.adley.whatsnextseries.models.retrofit.Episodes;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by adley on 29/04/16.
 * Adapter to list of seasons
 */
public class ListEpisodesRecyclerViewAdapter extends RecyclerView.Adapter<ListEpisodesViewHolder> {
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
        Episodes episode = mEpisodesList.get(position);
        if (episode != null) {
            holder.getEpisodeNumber().setText(mContext.getString(R.string.episode_number, episode.getEpisodeNumber()));
            holder.getEpisodeName().setText(episode.getName());
            try {
                String airDate = Utils.convertToLocationStringDate(episode.getAirDate(), mContext);
                holder.getAirDate().setText(airDate);
            } catch (ParseException e) {
                holder.getAirDate().setText(episode.getAirDate());
            }
            if (episode.getOverview().isEmpty()) {
                holder.getOverview().setText(mContext.getString(R.string.overview_empty_message));
            } else {
                holder.getOverview().setText(episode.getOverview());
            }
            if (episode.getStillPath() != null && !episode.getStillPath().isEmpty()) {
                Picasso.with(mContext).load(episode.getFullStillPath())
                        .error(R.drawable.noimageplaceholder)
                        .placeholder((R.drawable.noimageplaceholder))
                        .transform(new RoundedCornersTransformation(15, 2))
                        .into(holder.getStillImg());
                Log.d("FULL_STILL_PATH", episode.getFullStillPath());
            } else {
                holder.getStillImg().setVisibility(View.GONE);
            }
        }
    }

    public void loadNewData(List<Episodes> newEpisodes) {
        mEpisodesList = newEpisodes;
        notifyDataSetChanged();
    }
}
