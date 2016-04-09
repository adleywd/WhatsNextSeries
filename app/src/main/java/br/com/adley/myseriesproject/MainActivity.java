package br.com.adley.myseriesproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.adley.library.AppConnectionStatus;
import br.com.adley.tvmaze.GetTVShowJsonData;
import br.com.adley.tvmaze.TVMazeRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {

    private Button idSearchButton;
    private EditText idInputNameSerie;
    private static final String LOG_TAG = "MainActiviry";
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
                AppConnectionStatus appConnectionStatus = new AppConnectionStatus(MainActivity.this);
                if (!appConnectionStatus.isInternetConnection()) {
                    Toast.makeText(MainActivity.this, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                }else if (idInputNameSerie.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "ERRO: Insira um nome", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Buscando...", Toast.LENGTH_SHORT).show();
                    recyclerView = (RecyclerView) findViewById(R.id.recycler_view_home);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    ProcessTVShows processTVShows = new ProcessTVShows(idInputNameSerie.getText().toString());
                    processTVShows.execute();
                }
            }
        });
    }

    public class ProcessTVShows extends GetTVShowJsonData{

        public ProcessTVShows(String showName){
            super(showName);
        }
        public void execute(){
            //super.execute();
            ProcessData processData = new ProcessData();
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
