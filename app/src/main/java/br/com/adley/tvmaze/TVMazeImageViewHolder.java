package br.com.adley.tvmaze;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.adley.myseriesproject.R;

/**
 * Created by Adley on 10/03/2016.
 */
public class TVMazeImageViewHolder extends RecyclerView.ViewHolder {
    protected ImageView thumbnail;
    protected TextView title;

    public TVMazeImageViewHolder(View view) {
        super(view);
        this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        this.title = (TextView) view.findViewById((R.id.title));
    }
}
