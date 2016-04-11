package br.com.adley.myseriesproject;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by Adley.Damaceno on 11/04/2016.
 */
public class BaseActivity extends AppCompatActivity {
    private Toolbar toolbar;

    protected Toolbar activateToolbar(){
        if(toolbar == null){
            toolbar = (Toolbar) findViewById(R.id.app_bar);
            if(toolbar != null){
                setSupportActionBar(toolbar);
            }
        }
        return toolbar;
    }
}
