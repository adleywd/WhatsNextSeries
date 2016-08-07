package br.com.adley.myseriesproject.adapters.recyclerview;

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
import br.com.adley.myseriesproject.models.TVShowDetails;
import br.com.adley.myseriesproject.holders.FavoritesShowViewHolder;
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
        //View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shared_favorites_shows, null);
        //FavoritesShowViewHolder favoritesShowViewHolder = new FavoritesShowViewHolder(view);
        /* Ripple Effect Return */
        final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new FavoritesShowViewHolder(
                MaterialRippleLayout.on(inflater.inflate(R.layout.shared_favorites_shows, viewGroup, false))
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
    public void onBindViewHolder(FavoritesShowViewHolder holder, int position) {
        TVShowDetails tvShowDetails = mTVShowsList.get(position);
        HashMap<String,String> imagesSize = Utils.loadImagesPreferences(mContext);
        String[] images = mContext.getResources().getStringArray(R.array.poster_quality_values);

        // Check if the next
        boolean nextEpisodeIsToday = Utils.isToday(tvShowDetails.getNextEpisodeDate(), mContext);
        if(nextEpisodeIsToday) {
            Utils.setLayoutVisible(holder.getIsTodayLabel());
        }else {
            Utils.setLayoutInvisible(holder.getIsTodayLabel());
        }
        int radiusSize = 15;
        if (tvShowDetails.getPosterPath() != null && !tvShowDetails.getPosterPath().isEmpty()) {
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
            Picasso.with(mContext).load(tvShowDetails.getPosterPath())
                    .error(R.drawable.placeholder)
                    .placeholder((R.drawable.placeholder))
                    .transform(new RoundedCornersTransformation(radiusSize, 2))
                    .into(holder.getThumbnail());
        } else {
            Picasso.with(mContext).load(R.drawable.noimageplaceholder)
                    .transform(new RoundedCornersTransformation(radiusSize,2))
                    .into(holder.getThumbnail());
        }
        if(tvShowDetails.getName().equals(tvShowDetails.getOriginalName())) {
            holder.getTitle().setText(tvShowDetails.getName());
        }else{
            holder.getTitle().setText(mContext.getString(R.string.title_holder_text, tvShowDetails.getName(), tvShowDetails.getOriginalName()));
        }
        holder.getDateNextFavorites().setText(tvShowDetails.getNextEpisode());
    }

    //

    public void loadNewData(List<TVShowDetails> newTVShow) {
       List<TVShowDetails> tvShowDetailsOrdered = Utils.orderShowByNextDate(newTVShow, mContext);
        mTVShowsList = tvShowDetailsOrdered;
        notifyDataSetChanged();
    }
    //
    public TVShow getTVShow(int position) {
        return (null != mTVShowsList ? mTVShowsList.get(position) : null);
    }
}
