package br.com.adley.tvmaze;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.adley.library.TVShow;
import br.com.adley.myseriesproject.R;

/**
 * Created by Adley on 10/03/2016.
 */
public class TVMazeRecyclerViewAdapter extends RecyclerView.Adapter<TVMazeImageViewHolder> {
    private List<TVShow> tvShowsList;
    private Context context;

    public TVMazeRecyclerViewAdapter(Context context, List<TVShow> tvShowsList){
        this.context = context;
        this.tvShowsList = tvShowsList;
    }

    @Override
    public TVMazeImageViewHolder onCreateViewHolder (ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.browse, null);
        TVMazeImageViewHolder tvMazeImageViewHolder = new TVMazeImageViewHolder(view);
        return tvMazeImageViewHolder;
    }

    @Override
    public int getItemCount() {
        return (tvShowsList != null ? tvShowsList.size() : 0);
    }

    @Override
    public void onBindViewHolder(TVMazeImageViewHolder holder, int position) {
        TVShow tvShow = tvShowsList.get(position);
        Picasso.with(context).load(tvShow.getImageMedium())
                .error(R.drawable.placeholder)
                .placeholder((R.drawable.placeholder))
                .into(holder.thumbnail);
        holder.title.setText(tvShow.getName());
    }
}
