package br.com.adley.whatsnextseries.holders;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.adley.whatsnextseries.R;
import br.com.adley.whatsnextseries.fragments.FavoritesFragment;

/**
 * Created by Adley.Damaceno on 15/04/2016.
 * View Holder for Search List of tvshows
 */
public class FavoritesShowViewHolder extends RecyclerView.ViewHolder{
    private ImageView mThumbnail;
    private TextView mTitle;
    private TextView mDateNextFavorites;
    private View mIsTodayLabel;
    private CheckBox mCheckListItem;
    private CardView mFavoritesCardView;
    private FavoritesFragment mFavoritesFragment;

    public FavoritesShowViewHolder(View view, FavoritesFragment favoritesFragment) {
        super(view);
        this.mThumbnail = (ImageView) view.findViewById(R.id.favorites_thumbnail);
        this.mTitle = (TextView) view.findViewById((R.id.favorites_title));
        this.mDateNextFavorites = (TextView) view.findViewById(R.id.fav_next_episode_input);
        this.mIsTodayLabel = view.findViewById(R.id.fav_is_today_label);
        this.mFavoritesFragment = favoritesFragment;
        this.mCheckListItem = (CheckBox) view.findViewById(R.id.check_list_item);
        this.mFavoritesCardView = (CardView) view.findViewById(R.id.card_view_favorites);
        mFavoritesCardView.setOnLongClickListener(favoritesFragment);
        mFavoritesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFavoritesFragment.isInActionMode()){
                    if(mCheckListItem.isChecked()) {
                        mCheckListItem.setChecked(false);
                    } else {
                        mCheckListItem.setChecked(true);
                    }
                    mFavoritesFragment.prepareSelection(v, getAdapterPosition());
                }else{
                    mFavoritesFragment.onClickOpenDetail(v, getAdapterPosition());
                }

                if(mCheckListItem.isChecked()){
                    mFavoritesCardView.setCardBackgroundColor(ContextCompat.getColor(mFavoritesFragment.getContext(), R.color.cardview_color_selected));
                }else{
                    mFavoritesCardView.setCardBackgroundColor(ContextCompat.getColor(mFavoritesFragment.getContext(), R.color.cardboard_color_theme));
                }
            }
        });

        mCheckListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFavoritesFragment.isInActionMode()){
                    mFavoritesFragment.prepareSelection(v, getAdapterPosition());
                }else{
                    mFavoritesFragment.onClickOpenDetail(v, getAdapterPosition());
                }

                if(mCheckListItem.isChecked()){
                    mFavoritesCardView.setCardBackgroundColor(ContextCompat.getColor(mFavoritesFragment.getContext(), R.color.cardview_color_selected));
                }else{
                    mFavoritesCardView.setCardBackgroundColor(ContextCompat.getColor(mFavoritesFragment.getContext(), R.color.cardboard_color_theme));
                }
            }
        });
    }


    public TextView getDateNextFavorites() {
        return mDateNextFavorites;
    }

    public void setDateNextFavorites(TextView dateNextFavorites) {
        mDateNextFavorites = dateNextFavorites;
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

    public View getIsTodayLabel() {
        return mIsTodayLabel;
    }

    public void setIsTodayLabel(View isTodayLabel) {
        mIsTodayLabel = isTodayLabel;
    }

    public CheckBox getCheckListItem() {
        return mCheckListItem;
    }

    public void setCheckListItem(CheckBox checkListItem) {
        mCheckListItem = checkListItem;
    }

    public CardView getFavoritesCardView() {
        return mFavoritesCardView;
    }

    public void setFavoritesCardView(CardView favoritesCardView) {
        mFavoritesCardView = favoritesCardView;
    }
}
