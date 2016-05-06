package br.com.adley.myseriesproject.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.library.Utils;
import br.com.adley.myseriesproject.models.TVShow;
import br.com.adley.myseriesproject.models.TVShowDetails;
import br.com.adley.myseriesproject.themoviedb.GetTVShowDetailsJsonData;

public class HomeActivity extends BaseActivity {
    private List<Integer> mIdShowList;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        activateToolbarWithNavigationView(HomeActivity.this);
        mIdShowList = new ArrayList<>();
        mIdShowList.add(1412);
        //Get Show Details Data
        for (int idShow : mIdShowList) {
            ProcessTVShowsDetails processTVShowsDetails = new ProcessTVShowsDetails(idShow);
            processTVShowsDetails.execute();
        }

    }

    // Process and execute data into recycler view
    public class ProcessTVShowsDetails extends GetTVShowDetailsJsonData {
        private ProcessData processData;

        public ProcessTVShowsDetails(int idShow) {
            super(idShow, HomeActivity.this);
        }

        public void execute() {
            // Start process data (download and get)
            processData = new ProcessData();
            processData.execute();

            // Start loading dialog
            mProgress = Utils.configureProgressDialog("Aguarde...", "Carregando os dados da séries...", true, true, HomeActivity.this);
            mProgress.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    processData.cancel(true);
                    HomeActivity.this.finish();
                }
            });
            mProgress.show();
        }

        public class ProcessData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                //Get and Process SeasonData
                mProgress.dismiss();
            }
        }
    }
}
