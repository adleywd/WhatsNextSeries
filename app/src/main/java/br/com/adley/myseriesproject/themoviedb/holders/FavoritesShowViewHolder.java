package br.com.adley.myseriesproject.themoviedb.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.adley.myseriesproject.R;

/**
 * Created by Adley.Damaceno on 15/04/2016.
 * View Holder for Search List of tvshows
 */
public class FavoritesShowViewHolder extends RecyclerView.ViewHolder{
    protected ImageView mThumbnail;
    protected TextView mTitle;

    public FavoritesShowViewHolder(View view) {
        super(view);
        this.mThumbnail = (ImageView) view.findViewById(R.id.favorites_thumbnail);
        this.mTitle = (TextView) view.findViewById((R.id.favorites_title));
    }

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
}
