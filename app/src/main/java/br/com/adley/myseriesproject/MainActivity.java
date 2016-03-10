package br.com.adley.myseriesproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import br.com.adley.tvmaze.GetTVShowJsonData;

public class MainActivity extends AppCompatActivity {

    private ListView idSeriesList;
    private Button idSearchButton;
    private EditText idInputNameSerie;
    private static final String APIURL = "http://api.tvmaze.com";

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
}
