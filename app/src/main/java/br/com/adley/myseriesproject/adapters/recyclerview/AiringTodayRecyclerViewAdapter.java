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
import java.util.Locale;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.library.AppConsts;
import br.com.adley.myseriesproject.library.Utils;
import br.com.adley.myseriesproject.models.TVShow;
import br.com.adley.myseriesproject.holders.AiringTodayViewHolder;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Adley on 22/07/2016.
 * TODO
 */
public class AiringTodayRecyclerViewAdapter extends RecyclerView.Adapter<AiringTodayViewHolder> {
    private List<TVShow> mTVShowsList;
    private Context mContext;

    //
    public AiringTodayRecyclerViewAdapter(Context context, List<TVShow> tvShowsList) {
        this.mContext = context;
        this.mTVShowsList = tvShowsList;
    }

    //
    @Override
    public AiringTodayViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new AiringTodayViewHolder(
                MaterialRippleLayout.on(inflater.inflate(R.layout.shared_airing_today_list, viewGroup, false))
                        .rippleOverlay(true)
                        .rippleAlpha(0.2f)
                        .rippleColor(Utils.getColor(mContext, R.color.myseriesPrimaryBackgroundColor))
                        .rippleHover(true)
                        .create()
        );
    }

    @Override
    public void onBindViewHolder(AiringTodayViewHolder holder, int position) {
        TVShow tvShow = mTVShowsList.get(position);
        HashMap<String, String> imagesSize = Utils.loadImagesPreferences(mContext);
        String[] images = mContext.getResources().getStringArray(R.array.poster_quality_values);
        int radiusSize = 15;
        if (tvShow.getPosterPath() != null && !tvShow.getPosterPath().isEmpty()) {
            for (String image : images) {
                if (image.contains(imagesSize.get(AppConsts.POSTER_KEY_NAME))) {
                    // Remove All non numeric characters
                    image = image.replaceAll("[^\\d.]", "");
                    try {
                        radiusSize = (Integer.parseInt(image)) / 10;
                    } catch (NumberFormatException nfe) {
                        Log.e(this.getClass().getSimpleName(), nfe.getMessage());
                    }
                    break;
                }
            }
            Picasso.with(mContext).load(tvShow.getPosterPath())
                    .error(R.drawable.placeholder)
                    .placeholder((R.drawable.placeholder))
                    .transform(new RoundedCornersTransformation(radiusSize, 2))
                    .into(holder.getThumbnail());
        } else {
            Picasso.with(mContext).load(R.drawable.noimageplaceholder)
                    .transform(new RoundedCornersTransformation(50, 2))
                    .into(holder.getThumbnail());
        }

        // Setup Rating
        if (tvShow.getVoteAverage() > 0.0 & tvShow.getVoteCount() > 0) {
            Locale ptBr = new Locale("pt", "BR");
            String rating_and_max = String.format(ptBr, "%.2f", tvShow.getVoteAverage());
            holder.getRating().setText(rating_and_max);
        } else {
            holder.getRating().setText(mContext.getString(R.string.abbreviation_do_not_have));
        }

        // Setup Title
        holder.getTitle().setText(tvShow.getName());

        // Setup First Air Date
        String year = Utils.getYearFromStringDate(tvShow.getFirstAirDate(),mContext);
        holder.getFirstAirDate().setText(year);

    }

    @Override
    public int getItemCount() {
        return (mTVShowsList != null ? mTVShowsList.size() : 0);
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
