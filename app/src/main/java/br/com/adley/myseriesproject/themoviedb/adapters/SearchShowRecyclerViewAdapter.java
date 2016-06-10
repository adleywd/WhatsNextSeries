package br.com.adley.myseriesproject.themoviedb.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.balysv.materialripple.MaterialRippleLayout;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.library.AppConsts;
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
        HashMap<String,String> imagesSize = Utils.loadImagesPreferences(mContext);
        String[] images = mContext.getResources().getStringArray(R.array.poster_quality_values);
        int radiusSize = 15;
        if (tvShow.getPosterPath() != null && !tvShow.getPosterPath().isEmpty()) {
            for (String image: images) {
                if(image.contains(imagesSize.get(AppConsts.POSTER_KEY_NAME))){
                    // Remove All non numeric characters
                    image = image.replaceAll("[^\\d.]", "");
                    try {
                        radiusSize = (Integer.parseInt(image)) / 10;
                    }catch (NumberFormatException nfe){
                        Log.e(this.getClass().getSimpleName(), nfe.getMessage());
                    }
                    break;
                }
            }
            Picasso.with(mContext).load(tvShow.getPosterPath())
                    .error(R.drawable.placeholder)
                    .placeholder((R.drawable.placeholder))
                    .transform(new RoundedCornersTransformation(radiusSize,2))
                    .into(holder.getThumbnail());
        } else {
            Picasso.with(mContext).load(R.drawable.noimageplaceholder)
                    .transform(new RoundedCornersTransformation(50,2))
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
