package br.com.adley.myseriesproject.themoviedb.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.models.TVShow;
import br.com.adley.myseriesproject.models.TVShowDetails;
import br.com.adley.myseriesproject.themoviedb.holders.FavoritesShowViewHolder;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Adley on 10/03/2016.
 * TODO
 */
public class FavoritesRecyclerViewAdapter extends RecyclerView.Adapter<FavoritesShowViewHolder> {
    private List<TVShowDetails> mTVShowsList;
    private Context mContext;

    //
    public FavoritesRecyclerViewAdapter(Context context, List<TVShowDetails> tvShowsList) {
        this.mContext = context;
        this.mTVShowsList = tvShowsList;
    }

    //
    @Override
    public FavoritesShowViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shared_favorites_shows, null);
        FavoritesShowViewHolder favoritesShowViewHolder = new FavoritesShowViewHolder(view);
        return favoritesShowViewHolder;
    }

    //
    @Override
    public int getItemCount() {
        return (mTVShowsList != null ? mTVShowsList.size() : 0);
    }

    //
    @Override
    public void onBindViewHolder(FavoritesShowViewHolder holder, int position) {
        TVShowDetails tvShowDetails = mTVShowsList.get(position);
        if (tvShowDetails.getPosterPath() != null && !tvShowDetails.getPosterPath().isEmpty()) {
            Picasso.with(mContext).load(tvShowDetails.getPosterPath())
                    .error(R.drawable.placeholder)
                    .placeholder((R.drawable.placeholder))
                    .transform(new RoundedCornersTransformation(60,10))
                    .into(holder.getThumbnail());
        } else {
            Picasso.with(mContext).load(R.drawable.noimageplaceholder)
                    .transform(new RoundedCornersTransformation(60,10))
                    .into(holder.getThumbnail());
        }
        holder.getTitle().setText(tvShowDetails.getOriginalName());
    }

    //

    public void loadNewData(TVShowDetails newTVShow) {
        mTVShowsList.add(newTVShow);
        notifyDataSetChanged();
    }
    //
    public TVShow getTVShow(int position) {
        return (null != mTVShowsList ? mTVShowsList.get(position) : null);
    }
}
