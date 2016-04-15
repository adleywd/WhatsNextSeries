package br.com.adley.myseriesproject.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import br.com.adley.myseriesproject.R;

/**
 * Created by Adley.Damaceno on 11/04/2016.
 */
public class BaseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public static final String TVSHOW_TRANSFER = "TVSHOW_TRANSFER";
    private String LOG_TAG = BaseActivity.class.getSimpleName();


    protected Toolbar activateToolbar(){
        if(toolbar == null){
            toolbar = (Toolbar) findViewById(R.id.app_bar);
            if(toolbar != null){
                setSupportActionBar(toolbar);
            }
        }
        return toolbar;
    }

    protected Toolbar activateToolbarWithHomeEnabled() {
        activateToolbar();
        if(toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        return toolbar;

    }
}
