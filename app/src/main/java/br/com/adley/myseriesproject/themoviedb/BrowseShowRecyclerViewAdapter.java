package br.com.adley.myseriesproject.themoviedb;

/**
 * Created by Adley on 10/03/2016.
 * TODO
 */
public class BrowseShowRecyclerViewAdapter {//extends RecyclerView.Adapter<TVMazeImageViewHolder> {
//    private List<TVMazeTVShowObject> tvShowsList;
//    private Context context;
//
//    public BrowseShowRecyclerViewAdapter(Context context, List<TVMazeTVShowObject> tvShowsList){
//        this.context = context;
//        this.tvShowsList = tvShowsList;
//    }
//
//    @Override
//    public TVMazeImageViewHolder onCreateViewHolder (ViewGroup viewGroup, int i){
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shared_browse, null);
//        TVMazeImageViewHolder tvMazeImageViewHolder = new TVMazeImageViewHolder(view);
//        return tvMazeImageViewHolder;
//    }
//
//    @Override
//    public int getItemCount() {
//        return (tvShowsList != null ? tvShowsList.size() : 0);
//    }
//
//    @Override
//    public void onBindViewHolder(TVMazeImageViewHolder holder, int position) {
//        TVMazeTVShowObject tvShow = tvShowsList.get(position);
//        if(tvShow.getImageMedium() != null) {
//            Picasso.with(context).load(tvShow.getImageMedium())
//                    .error(R.drawable.placeholder)
//                    .placeholder((R.drawable.placeholder))
//                    .into(holder.thumbnail);
//        } else if(tvShow.getImageOriginal() != null){
//            Picasso.with(context).load(tvShow.getImageOriginal())
//                    .error(R.drawable.placeholder)
//                    .placeholder((R.drawable.placeholder))
//                    .into(holder.thumbnail);
//        } else {
//            Picasso.with(context).load(R.drawable.noimageplaceholder)
//                    .into(holder.thumbnail);
//        }
//        holder.title.setText(tvShow.getName());
//    }
//
//    public void loadNewData(List<TVMazeTVShowObject> newTvShows){
//        tvShowsList = newTvShows;
//        notifyDataSetChanged();
//    }
//
//    public TVMazeTVShowObject getTVShow(int position){
//        return (null != tvShowsList ? tvShowsList.get(position):null);
//    }
}
