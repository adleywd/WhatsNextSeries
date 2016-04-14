package br.com.adley.myseriesproject;

import android.content.Intent;
import android.os.Bundle;

import br.com.adley.library.TVShow;

public class TVShowDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshow_details);
        activateToolbarWithHomeEnabled();

        Intent intent = getIntent();
        TVShow tvShow = (TVShow) intent.getSerializableExtra(TVSHOW_TRANSFER);
    }

}
