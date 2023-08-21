package br.com.adley.whatsnextseries.library;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.ContextThemeWrapper;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import br.com.adley.whatsnextseries.BuildConfig;
import br.com.adley.whatsnextseries.R;

/**
 * Created by Adley.Damaceno on 03/11/2016.
 * This class manage the Changelog Event in the APP.
 */

public class Changelog {
    private String LOG_TAG = Changelog.class.getSimpleName();

    private static final String LAST_VERSION = "last_app_version";
    private SharedPreferences mSharedPreferences;
    private Context mContext;
    private AlertDialog mAlertDialog;

    /***
     *
     * @param context
     * If has internet connection and if it is a new version, the change log will run.
     */
    public Changelog(Context context, boolean isMenu){
        mContext = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (isNewVersion() || isMenu ) {
            mAlertDialog = dialogBuild();
        }
    }

    /***
     * Execute (show) the alert with the change log.
     */
    public void execute(){
        if(mAlertDialog != null) mAlertDialog.show();
    }

    /***
     *
     * @return the preference to load or not the change log (first time will be true)
     */
    private boolean isNewVersion() {
        String version = mSharedPreferences.getString(LAST_VERSION, "");
        // Do not show change log for new users
        if(version.isEmpty()) {
            updateChangeLogPreferences();
            return false;
        }else {
            return !version.equals(BuildConfig.VERSION_NAME);
        }
    }

    /***
     *
     * Update the load of change log into preferences
     */
    private void updateChangeLogPreferences(){
        SharedPreferences.Editor spEditor = mSharedPreferences.edit();
        spEditor.putString(LAST_VERSION, BuildConfig.VERSION_NAME);
        spEditor.apply();
        spEditor.commit();
    }

    /***
     *
     * @return File log(txt in html format) into a string
     */
    private String getLog(){
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = mContext.getResources().openRawResource(R.raw.changelog);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    /***
     *
     * @return AlertDialog with the change log
     */
    private AlertDialog dialogBuild(){
        /*** DISABLED ***
         * IF has no connection load offline changelogtxt
        boolean hasConnection = Utils.checkAppConnectionStatus(mContext);
        if(hasConnection){
            webView.loadUrl("http://adley.com.br/whatsnextsite/appconfig/pt/changelog.html");
         } else {
            String customHtml = getLog();
            webView.loadDataWithBaseURL(null, customHtml, "text/html", "UTF-8", null);
        }
         ***/

        WebView webView = new WebView(mContext);
        webView.loadDataWithBaseURL(null, getLog(), "text/html", "UTF-8", null);
        AlertDialog.Builder builder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext,
                    android.R.style.Theme_Material_Dialog_Alert));
        }else{
            builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext,
                    android.R.style.Theme_Dialog));
        }
        builder.setTitle(mContext.getString(R.string.change_log_news))
                .setView(webView)
                .setCancelable(false)
                .setPositiveButton(mContext.getResources().getString(R.string.button_ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                updateChangeLogPreferences();
                            }
                        });
        return builder.create();
    }
}
