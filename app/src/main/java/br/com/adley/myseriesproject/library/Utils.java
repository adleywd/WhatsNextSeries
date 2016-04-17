package br.com.adley.myseriesproject.library;

import android.content.Context;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

import br.com.adley.myseriesproject.R;

/**
 * Created by adley on 16/04/16.
 * Class with utilities to project.
 */
public class Utils {

    //--- Append URI ---//
    public static Uri appendUri(String uri, HashMap<String, String> queryParams) {
        Uri newUri = Uri.parse(uri);
        if (!queryParams.isEmpty()) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                newUri.buildUpon().appendQueryParameter(key, value).build();
            }
            return newUri;
        }
        return null;
    }

    public static String getApiKey(Context context){
        return context.getString(R.string.api_key);
    }
}
