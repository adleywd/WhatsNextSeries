package br.com.adley.myseriesproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.library.RecyclerItemClickListener;
import br.com.adley.myseriesproject.library.Utils;
import br.com.adley.myseriesproject.models.TVShow;
import br.com.adley.myseriesproject.themoviedb.BrowseShowRecyclerViewAdapter;
import br.com.adley.myseriesproject.themoviedb.GetTVShowJsonData;

public class SearchTVShowActivity extends BaseActivity {

    private Button idSearchButton;
    private EditText idInputNameSerie;
    private CheckBox idCheckBoxSearchInPtBr;
    private static final String LOG_TAG = "MainActiviry";
    private RecyclerView recyclerView;
    private BrowseShowRecyclerViewAdapter browseShowRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshow_search);

        activateToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        browseShowRecyclerViewAdapter = new BrowseShowRecyclerViewAdapter(SearchTVShowActivity.this, new ArrayList<TVShow>());
        recyclerView.setAdapter(browseShowRecyclerViewAdapter);

        // Create the touch for the recyclerview list
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Creates and configure intent to call tv show details activity
                Intent intent = new Intent(SearchTVShowActivity.this, TVShowDetailsActivity.class);
                intent.putExtra(TVSHOW_TRANSFER, browseShowRecyclerViewAdapter.getTVShow(position));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //Creates and configure intent to call tv show details activity
                Intent intent = new Intent(SearchTVShowActivity.this, TVShowDetailsActivity.class);
                intent.putExtra(TVSHOW_TRANSFER, browseShowRecyclerViewAdapter.getTVShow(position));
                startActivity(intent);
            }

        }));


        idSearchButton = (Button) findViewById(R.id.idSearchButton);
        idInputNameSerie = (EditText) findViewById(R.id.idInputNameSerie);
        idCheckBoxSearchInPtBr = (CheckBox)findViewById(R.id.showInPtBr);

        idSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide keyboard when button was clicked.
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(SearchTVShowActivity.this.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                //Check the phone connection status.
                //AppConnectionStatus appConnectionStatus = new AppConnectionStatus(SearchTVShowActivity.this);
                if (!Utils.checkAppConnectionStatus(SearchTVShowActivity.this)){
                    Toast.makeText(SearchTVShowActivity.this, getString(R.string.error_no_internet_connection), Toast.LENGTH_SHORT).show();
                }else if (idInputNameSerie.getText().toString().isEmpty()) {
                    Toast.makeText(SearchTVShowActivity.this, getString(R.string.error_blank_search_field), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SearchTVShowActivity.this, getString(R.string.search_message), Toast.LENGTH_SHORT).show();
                    // Create and generate the recycler view for list of results
                    recyclerView = (RecyclerView) findViewById(R.id.recycler_view_home);
                    recyclerView.setLayoutManager(new LinearLayoutManager(SearchTVShowActivity.this));
                    ProcessTVShows processTVShows = new ProcessTVShows(idInputNameSerie.getText().toString());
                    processTVShows.execute();
                }
            }
        });
    }

    // Process and execute data into recycler view
    public class ProcessTVShows extends GetTVShowJsonData {

        public ProcessTVShows(String showName){
            super(showName, SearchTVShowActivity.this, idCheckBoxSearchInPtBr.isChecked());
        }

        public void execute(){
            ProcessData processData = new ProcessData();
            processData.execute();
        }
        public class ProcessData extends DownloadJsonData{
            protected void onPostExecute(String webData){
                super.onPostExecute(webData);
                browseShowRecyclerViewAdapter.loadNewData(getTVShows());
            }
        }
    }
}
