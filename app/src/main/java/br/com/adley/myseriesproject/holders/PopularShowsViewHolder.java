package br.com.adley.myseriesproject.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.adley.myseriesproject.R;

/**
 * Created by Adley.Damaceno on 22/07/2016.
 * View Holder for Search List of tvshows
 */
public class PopularShowsViewHolder extends RecyclerView.ViewHolder{
    protected ImageView mThumbnail;
    protected TextView mTitle;

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

    public PopularShowsViewHolder(View view) {
        super(view);
        this.mThumbnail = (ImageView) view.findViewById(R.id.thumbnail_popular_show);
        this.mTitle = (TextView) view.findViewById((R.id.title_popular_show));
    }
}
