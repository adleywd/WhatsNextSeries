package br.com.adley.myseriesproject.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.com.adley.myseriesproject.R;

/**
 * Created by Adley.Damaceno on 11/04/2016.
 * Base activity.
 * If need to set a general code, which can be
 * used in all application, write here.
 */
public class BaseActivity extends AppCompatActivity {
    public Toolbar getToolbar() {
        return mToolbar;
    }

    private Toolbar mToolbar;
    public static final String TVSHOW_TRANSFER = "TVSHOW_TRANSFER";
    private String LOG_TAG = BaseActivity.class.getSimpleName();


    protected Toolbar activateToolbar(){
        if(mToolbar == null){
            mToolbar = (Toolbar) findViewById(R.id.app_bar);
            if(mToolbar != null){
                setSupportActionBar(mToolbar);
            }
        }
        return mToolbar;
    }

    protected Toolbar activateToolbarWithHomeEnabled() {
        activateToolbar();
        if(mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        return mToolbar;

    }
}
