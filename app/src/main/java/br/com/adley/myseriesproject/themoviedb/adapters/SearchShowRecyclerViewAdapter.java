package br.com.adley.myseriesproject.themoviedb.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.balysv.materialripple.MaterialRippleLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.library.Utils;
import br.com.adley.myseriesproject.models.TVShow;
import br.com.adley.myseriesproject.themoviedb.holders.SearchShowViewHolder;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

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
        final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new SearchShowViewHolder(
                MaterialRippleLayout.on(inflater.inflate(R.layout.shared_search_tvshow, viewGroup, false))
                        .rippleOverlay(true)
                        .rippleAlpha(0.2f)
                        .rippleColor(Utils.getColor(mContext, R.color.myseriesPrimaryBackgroundColor))
                        .rippleHover(true)
                        .create()
        );
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
                    .transform(new RoundedCornersTransformation(15,2))
                    .into(holder.getThumbnail());
        } else {
            Picasso.with(mContext).load(R.drawable.noimageplaceholder)
                    .transform(new RoundedCornersTransformation(15,2))
                    .into(holder.getThumbnail());
        }
        holder.getTitle().setText(tvShow.getOriginalName());
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
