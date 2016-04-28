package br.com.adley.myseriesproject.tvmaze;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.adley.myseriesproject.R;

/**
 * Created by Adley on 10/03/2016.
 */
public class TVMazeRecyclerViewAdapter extends RecyclerView.Adapter<TVMazeImageViewHolder> {
    private List<TVMazeTVShowObject> TVMazeTvShowsListObject;
    private Context context;

    public TVMazeRecyclerViewAdapter(Context context, List<TVMazeTVShowObject> TVMazeTvShowsListObject){
        this.context = context;
        this.TVMazeTvShowsListObject = TVMazeTvShowsListObject;
    }

    @Override
    public TVMazeImageViewHolder onCreateViewHolder (ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shared_search, null);
        TVMazeImageViewHolder tvMazeImageViewHolder = new TVMazeImageViewHolder(view);
        return tvMazeImageViewHolder;
    }

    @Override
    public int getItemCount() {
        return (TVMazeTvShowsListObject != null ? TVMazeTvShowsListObject.size() : 0);
    }

    @Override
    public void onBindViewHolder(TVMazeImageViewHolder holder, int position) {
        TVMazeTVShowObject TVMazeTvShowObject = TVMazeTvShowsListObject.get(position);
        if(TVMazeTvShowObject.getImageMedium() != null) {
            Picasso.with(context).load(TVMazeTvShowObject.getImageMedium())
                    .error(R.drawable.placeholder)
                    .placeholder((R.drawable.placeholder))
                    .into(holder.thumbnail);
        } else if(TVMazeTvShowObject.getImageOriginal() != null){
            Picasso.with(context).load(TVMazeTvShowObject.getImageOriginal())
                    .error(R.drawable.placeholder)
                    .placeholder((R.drawable.placeholder))
                    .into(holder.thumbnail);
        } else {
            Picasso.with(context).load(R.drawable.noimageplaceholder)
                    .into(holder.thumbnail);
        }
        holder.title.setText(TVMazeTvShowObject.getName());
    }

    public void loadNewData(List<TVMazeTVShowObject> newTVMazeTvShowObjects){
        TVMazeTvShowsListObject = newTVMazeTvShowObjects;
        notifyDataSetChanged();
    }

    public TVMazeTVShowObject getTVShow(int position){
        return (null != TVMazeTvShowsListObject ? TVMazeTvShowsListObject.get(position):null);
    }
}
