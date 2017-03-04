package com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.derwrecked.connectivitycustominputviewpagercardview.R;

/**
 * Created by Derek on 2/24/2017.
 */

public class CardViewFragment extends Fragment {
    public final static String FRAGMENT_TAG_CARDVIEW= "cardview_fragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cardview_fragment, container, false);
        Bundle args = getArguments();
        if(args != null){

        }
        return view;
    }
}
