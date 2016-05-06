package br.com.adley.myseriesproject.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.library.Utils;
import br.com.adley.myseriesproject.models.TVShowDetails;
import br.com.adley.myseriesproject.themoviedb.adapters.FavoritesRecyclerViewAdapter;
import br.com.adley.myseriesproject.themoviedb.GetTVShowDetailsJsonData;

public class HomeActivity extends BaseActivity {
    private List<Integer> mIdShowList;
    private ProgressDialog mProgress;
    private int mShowListCount;
    private RecyclerView mRecyclerView;
    private FavoritesRecyclerViewAdapter mFavoritesRecyclerViewAdapter;
    private final String PREFIX_IMG_DIMENSION_FAVORITES = "w92";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        activateToolbarWithNavigationView(HomeActivity.this);
        mShowListCount = 0;
        mIdShowList = new ArrayList<>();
        mIdShowList.add(1412);
        mIdShowList.add(1414);
        mIdShowList.add(1415);
        mIdShowList.add(1416);
        mIdShowList.add(1417);

        mFavoritesRecyclerViewAdapter = new FavoritesRecyclerViewAdapter(this, new ArrayList<TVShowDetails>());
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_favorites_list);

        mRecyclerView.setAdapter(mFavoritesRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(HomeActivity.this, 2));
        mRecyclerView.setHasFixedSize(true);

        //Start loading dialog
        mProgress = Utils.configureProgressDialog("Aguarde...", "Carregando os dados da séries...", true, true, HomeActivity.this);
        //Get Show Details Data
        for (int idShow : mIdShowList) {
            ProcessFavoritesTVShowsDetails processFavoritesTVShowsDetails = new ProcessFavoritesTVShowsDetails(idShow, PREFIX_IMG_DIMENSION_FAVORITES);
            processFavoritesTVShowsDetails.execute();
        }

    }

    // Process and execute data into recycler view
    public class ProcessFavoritesTVShowsDetails extends GetTVShowDetailsJsonData {
        private ProcessData processData;

        public ProcessFavoritesTVShowsDetails(int idShow) {
            super(idShow, HomeActivity.this);
        }

        public ProcessFavoritesTVShowsDetails(int idShow, String prefixImg) {
            super(idShow,prefixImg, HomeActivity.this);
        }

        public void execute() {
            // Start process data (download and get)
            processData = new ProcessData();
            processData.execute();
            mProgress.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    processData.cancel(true);
                }
            });
            mProgress.show();
        }

        public class ProcessData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                //Get and Process SeasonData
                mShowListCount++;
                mFavoritesRecyclerViewAdapter.loadNewData(getTVShowsDetails());
                if(mShowListCount >= mIdShowList.size()) {
                    mProgress.dismiss();
                }
            }
        }
    }
}
