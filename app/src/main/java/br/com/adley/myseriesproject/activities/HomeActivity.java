package br.com.adley.myseriesproject.activities;

import android.os.Bundle;

import br.com.adley.myseriesproject.R;

public class HomeActivity extends BaseActivity
       {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        activateToolbarWithNavigationView(HomeActivity.this);

    }
}
