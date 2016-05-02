package br.com.adley.myseriesproject.themoviedb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.models.TVShow;

/**
 * Created by Adley on 10/03/2016.
 * TODO
 */
public class SearchShowRecyclerViewAdapter extends RecyclerView.Adapter<SearchShowViewHolder> {
    private List<TVShow> mTVShowsList;
    private Context mContext;

    //
    public SearchShowRecyclerViewAdapter(Context context, List<TVShow> tvShowsList) {
        this.mContext = context;
        this.mTVShowsList = tvShowsList;
    }

    //
    @Override
    public SearchShowViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shared_search_tvshow, null);
        view.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        SearchShowViewHolder searchShowViewHolder = new SearchShowViewHolder(view);
        return searchShowViewHolder;
    }

    //
    @Override
    public int getItemCount() {
        return (mTVShowsList != null ? mTVShowsList.size() : 0);
    }

    //
    @Override
    public void onBindViewHolder(SearchShowViewHolder holder, int position) {
        TVShow tvShow = mTVShowsList.get(position);
        if (tvShow.getPosterPath() != null && !tvShow.getPosterPath().isEmpty()) {
            Picasso.with(mContext).load(tvShow.getPosterPath())
                    .error(R.drawable.placeholder)
                    .placeholder((R.drawable.placeholder))
                    .into(holder.mThumbnail);
        } else {
            Picasso.with(mContext).load(R.drawable.noimageplaceholder)
                    .into(holder.mThumbnail);
        }
        holder.mTitle.setText(tvShow.getOriginalName());
    }

    //
    public void loadNewData(List<TVShow> newTvShows) {
        mTVShowsList = newTvShows;
        notifyDataSetChanged();
    }

    //
    public TVShow getTVShow(int position) {
        return (null != mTVShowsList ? mTVShowsList.get(position) : null);
    }
}
