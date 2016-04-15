package br.com.adley.myseriesproject.themoviedb;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.adley.myseriesproject.R;

/**
 * Created by Adley.Damaceno on 15/04/2016.
 */
public class BrowseShowViewHolder extends RecyclerView.ViewHolder{
    protected ImageView thumbnail;
    protected TextView title;

    public BrowseShowViewHolder(View view) {
        super(view);
        this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        this.title = (TextView) view.findViewById((R.id.title));
    }
}
