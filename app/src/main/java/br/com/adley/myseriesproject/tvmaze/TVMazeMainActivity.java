package br.com.adley.myseriesproject.tvmaze;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.activities.BaseActivity;
import br.com.adley.myseriesproject.activities.TVShowDetailsActivity;
import br.com.adley.myseriesproject.library.AppConnectionStatus;
import br.com.adley.myseriesproject.library.RecyclerItemClickListener;

public class TVMazeMainActivity extends BaseActivity {

    private Button idSearchButton;
    private EditText idInputNameSerie;
    private static final String LOG_TAG = "MainActiviry";
    private RecyclerView recyclerView;
    private TVMazeRecyclerViewAdapter TVMazeRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activateToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TVMazeRecyclerViewAdapter = new TVMazeRecyclerViewAdapter(TVMazeMainActivity.this, new ArrayList<TVMazeTVShowObject>());
        recyclerView.setAdapter(TVMazeRecyclerViewAdapter);

        // Create the touch for the recyclerview list
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Creates and configure intent to call tv show details activity
                Intent intent = new Intent(TVMazeMainActivity.this, TVShowDetailsActivity.class);
                intent.putExtra(TVSHOW_TRANSFER, TVMazeRecyclerViewAdapter.getTVShow(position));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //Creates and configure intent to call tv show details activity
                Intent intent = new Intent(TVMazeMainActivity.this, TVShowDetailsActivity.class);
                intent.putExtra(TVSHOW_TRANSFER, TVMazeRecyclerViewAdapter.getTVShow(position));
                startActivity(intent);

            }
        }));


        idSearchButton = (Button) findViewById(R.id.idSearchButton);
        idInputNameSerie = (EditText) findViewById(R.id.idInputNameSerie);

        idSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide keyboard when button was clicked.
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(TVMazeMainActivity.this.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                //Check the phone connection status.
                AppConnectionStatus appConnectionStatus = new AppConnectionStatus(TVMazeMainActivity.this);
                if (!appConnectionStatus.isInternetConnection()) {
                    Toast.makeText(TVMazeMainActivity.this, getString(R.string.error_no_internet_connection), Toast.LENGTH_SHORT).show();
                }else if (idInputNameSerie.getText().toString().isEmpty()) {
                    Toast.makeText(TVMazeMainActivity.this, getString(R.string.error_blank_search_field), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(TVMazeMainActivity.this, getString(R.string.search_message), Toast.LENGTH_SHORT).show();
                    // Create and generate the recycler view for list of results
                    recyclerView = (RecyclerView) findViewById(R.id.recycler_view_home);
                    recyclerView.setLayoutManager(new LinearLayoutManager(TVMazeMainActivity.this));
                    ProcessTVShowsTVMaze processTVShows = new ProcessTVShowsTVMaze(idInputNameSerie.getText().toString());
                    processTVShows.execute();
                }
            }
        });
    }

    // Process and execute data into recycler view
    public class ProcessTVShowsTVMaze extends TVMazeGetTVShowJsonData {

        public ProcessTVShowsTVMaze(String showName){
            super(showName, TVMazeMainActivity.this);
        }
        public void execute(){
            ProcessData processData = new ProcessData();
            processData.execute();
        }
        public class ProcessData extends DownloadJsonData{
            protected void onPostExecute(String webData){
                super.onPostExecute(webData);
                TVMazeRecyclerViewAdapter.loadNewData(getTVShows());
            }
        }
    }
}
