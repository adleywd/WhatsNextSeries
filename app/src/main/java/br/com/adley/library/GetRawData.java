package br.com.adley.library;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Adley on 08/03/2016.
 */

enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALSED, FAILED_OR_EMPTY, OK}

public class GetRawData {
    private String LOG_TAG = GetRawData.class.getSimpleName();
    private String rawUrl;
    private String data;
    private DownloadStatus downloadStatus;

    public GetRawData(String rawUrl) {
        this.rawUrl = rawUrl;
        this.downloadStatus = DownloadStatus.IDLE;
    }

    public void reset(){
        this.downloadStatus = DownloadStatus.IDLE;
        this.rawUrl = null;
        this.data = null;
    }

    public String getData() {
        return data;
    }

    public DownloadStatus getDownloadStatus() {
        return downloadStatus;
    }

    public void execute(){
        this.downloadStatus = DownloadStatus.PROCESSING;
        DownloadRawData downloadRawData = new DownloadRawData();
        downloadRawData.execute(rawUrl);
    }

    public class DownloadRawData extends AsyncTask<String, Void, String>{
        protected void onPostExecute(String webData){
            data = webData;
            Log.v(LOG_TAG, "Data returned was: "+data);
            if(data == null){
                if(rawUrl == null){
                    downloadStatus = DownloadStatus.NOT_INITIALSED;
                }else{
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY;
                }
            }else{
                //Success
                downloadStatus = DownloadStatus.OK;
            }
        }

        protected String doInBackground(String... params){
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            if(params == null)
                return null;
            try{
                URL url = new URL(params[0]);

                urlConnection= (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if(inputStream == null)
                    return null;

                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                return buffer.toString();

            }catch (IOException e){
                Log.e(LOG_TAG, "Error", e);
                return null;
            }finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if (reader != null){
                    try {
                        reader.close();
                    }catch (final IOException e){
                        Log.e(LOG_TAG, "Error closing stream",e);
                    }
                }
            }
        }
    }
}
