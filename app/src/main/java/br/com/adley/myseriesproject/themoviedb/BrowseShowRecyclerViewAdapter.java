package br.com.adley.myseriesproject.themoviedb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.models.TVShow;

/**
 * Created by Adley on 10/03/2016.
 * TODO
 */
public class BrowseShowRecyclerViewAdapter extends RecyclerView.Adapter<BrowseShowViewHolder> {
   private List<TVShow> tvShowsList;
   private Context context;
//
   public BrowseShowRecyclerViewAdapter(Context context, List<TVShow> tvShowsList){
       this.context = context;
       this.tvShowsList = tvShowsList;
   }
//
   @Override
   public BrowseShowViewHolder onCreateViewHolder (ViewGroup viewGroup, int i){
       View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shared_browse, null);
       BrowseShowViewHolder browseShowViewHolder = new BrowseShowViewHolder(view);
       return browseShowViewHolder;
   }
//
   @Override
   public int getItemCount() {
       return (tvShowsList != null ? tvShowsList.size() : 0);
   }
//
   @Override
   public void onBindViewHolder(BrowseShowViewHolder holder, int position) {
       TVShow tvShow = tvShowsList.get(position);
       if(tvShow.getPosterPath() != null) {
           Picasso.with(context).load(tvShow.getPosterPath())
                   .error(R.drawable.placeholder)
                   .placeholder((R.drawable.placeholder))
                   .into(holder.thumbnail);
       } else {
           Picasso.with(context).load(R.drawable.noimageplaceholder)
                   .into(holder.thumbnail);
       }
       holder.title.setText(tvShow.getName());
   }
//
   public void loadNewData(List<TVShow> newTvShows){
       tvShowsList = newTvShows;
       notifyDataSetChanged();
   }
//
   public TVShow getTVShow(int position){
       return (null != tvShowsList ? tvShowsList.get(position):null);
   }
}
