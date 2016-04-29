package br.com.adley.myseriesproject.themoviedb;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.adley.myseriesproject.R;

/**
 * Created by Adley.Damaceno on 15/04/2016.
 * View Holder for Search List of tvshows
 */
public class SearchShowViewHolder extends RecyclerView.ViewHolder{
    protected ImageView mThumbnail;
    protected TextView mTitle;

    public SearchShowViewHolder(View view) {
        super(view);
        this.mThumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        this.mTitle = (TextView) view.findViewById((R.id.title));
    }
}
