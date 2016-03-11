package br.com.adley.myseriesproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.adley.library.TVShow;
import br.com.adley.tvmaze.GetTVShowJsonData;
import br.com.adley.tvmaze.TVMazeRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {

    private Button idSearchButton;
    private EditText idInputNameSerie;
    private static final String LOG_TAG = "MainActiviry";
    private List<TVShow> tvShows = new ArrayList<>();
    private RecyclerView recyclerView;
    private TVMazeRecyclerViewAdapter tvMazeRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        idSearchButton = (Button) findViewById(R.id.idSearchButton);
        idInputNameSerie = (EditText) findViewById(R.id.idInputNameSerie);

        idSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idInputNameSerie.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "ERRO: Insira um nome", Toast.LENGTH_SHORT).show();
                } else {
                    //String url = APIURL + "/search/shows?q=" + idInputNameSerie.getText().toString();
                    Toast.makeText(MainActivity.this, "Buscando...", Toast.LENGTH_LONG).show();
                    GetTVShowJsonData jsonData = new GetTVShowJsonData(idInputNameSerie.getText().toString());
                    jsonData.execute();
                }
            }
        });
    }

    public class ProcessTVShows extends  GetTVShowJsonData{

        public ProcessTVShows(String destinationUrl){
            super(destinationUrl);
        }
        public void execute(){
         super.execute();
            ProcessData processData = new ProcessTVShows();
            processData.execute();
        }
        public class ProcessData extends DownloadJsonData{
            protected void onPostExecute(String webData){
                super.onPostExecute(webData);
                tvMazeRecyclerViewAdapter = new TVMazeRecyclerViewAdapter(MainActivity.this, getTVShows());
                recyclerView.setAdapter(tvMazeRecyclerViewAdapter);
            }
        }
    }
}
