package br.com.adley.myseriesproject.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.adley.myseriesproject.R;

/**
 * Created by Adley.Damaceno on 21/07/2016.
 * Class to control data for shows airing today
 */
public class AirTodayFragment extends Fragment {

    private View airTodayFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        airTodayFragment = inflater.inflate(R.layout.fragment_airtoday, container, false);
        return airTodayFragment;
    }
}
