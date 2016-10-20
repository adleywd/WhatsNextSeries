package br.com.adley.whatsnextseries.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.adley.whatsnextseries.R;

/**
 * Created by Adley.Damaceno on 22/07/2016.
 * View Holder for Search List of tvshows
 */
public class PopularShowsViewHolder extends RecyclerView.ViewHolder{
    protected ImageView mThumbnail;
    protected TextView mTitle;
    protected TextView mRating;
    protected TextView mFirstAirDate;

    public ImageView getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(ImageView thumbnail) {
        mThumbnail = thumbnail;
    }

    public TextView getTitle() {
        return mTitle;
    }

    public void setTitle(TextView title) {
        mTitle = title;
    }

    public TextView getRating() {
        return mRating;
    }

    public void setRating(TextView rating) {
        mRating = rating;
    }

    public TextView getFirstAirDate() {
        return mFirstAirDate;
    }

    public void setFirstAirDate(TextView firstAirDate) {
        mFirstAirDate = firstAirDate;
    }

    public PopularShowsViewHolder(View view) {
        super(view);
        this.mThumbnail = (ImageView) view.findViewById(R.id.thumbnail_popular_show);
        this.mTitle = (TextView) view.findViewById((R.id.title_popular_show));
        this.mRating = (TextView) view.findViewById(R.id.rating_value_popular);
        this.mFirstAirDate = (TextView) view.findViewById(R.id.first_air_date_value_popular_shows);
    }
}
