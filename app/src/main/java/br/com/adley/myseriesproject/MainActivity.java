package br.com.adley.myseriesproject;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.adley.library.GetRawData;

public class MainActivity extends AppCompatActivity {

    private ListView idSeriesList;
    private Button idSearchButton;
    private EditText idInputNameSerie;
    private static final String APIURL="http://api.tvmaze.com";

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
                    }else {
                        String url = APIURL + "/search/shows?q=" + idInputNameSerie.getText().toString();
                        Toast.makeText(MainActivity.this, "Buscando...", Toast.LENGTH_LONG).show();
                        GetRawData theRawData = new GetRawData(url);
                        theRawData.execute();
                        //new JSONTask().execute(url);
                    }
                }
            });
    }
    public class JSONTask extends AsyncTask<String,String,List<String> > {
        @Override
        protected List<String> doInBackground(String... params) {

            BufferedReader reader = null;
            HttpURLConnection connection = null;
            try {
                URL reqURL = new URL(params[0]);
                connection = (HttpURLConnection) (reqURL.openConnection());
                connection.connect();
                //connection.setRequestMethod("GET");
                InputStream in = new BufferedInputStream(connection.getInputStream());

                reader = new BufferedReader(new InputStreamReader(in));
                String line;
                StringBuilder result = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                String finalJson = result.toString();
                List<String> listNames = new ArrayList<String>();
                JSONArray jsonArray = new JSONArray(finalJson);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    JSONObject oneShowJsonObject = new JSONObject(jsonobject.toString());
                    JSONObject show = oneShowJsonObject.getJSONObject("show");
                    listNames.add(show.getString("name"));
                }

                return listNames;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "MalformedUrlException error", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "IoException error", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "JSONException error", Toast.LENGTH_LONG).show();
            } finally {
                try{
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (reader != null) {
                        reader.close();
                    }
                }catch (IOException e){
                    Toast.makeText(MainActivity.this, "Finally IOException error", Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, "Finally Exception error", Toast.LENGTH_LONG).show();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<String> listNames) {
            super.onPostExecute(listNames);
            if(!listNames.isEmpty()) {
                idSeriesList = (ListView) findViewById(R.id.idSeriesList);
                ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                        getApplicationContext(),
                        android.R.layout.simple_list_item_2,
                        android.R.id.text2,
                        listNames
                );
                idSeriesList.setAdapter(adaptador);
                idSeriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(MainActivity.this, listNames.get(position).toString() , Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(MainActivity.this, "Ocorreu um erro no onPostExecute", Toast.LENGTH_LONG).show();
            }
        }
    }
}
