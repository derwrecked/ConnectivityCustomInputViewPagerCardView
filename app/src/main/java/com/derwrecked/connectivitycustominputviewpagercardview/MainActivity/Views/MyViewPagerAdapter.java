package com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.Views;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Derek on 2/23/2017.
 */

public class MyViewPagerAdapter extends FragmentPagerAdapter {
    final private int NUMBER_OF_TABS = 2;
    final private String NETWORK_CONN_TAB_TITLE = "Network";
    final private String CARDVIEW_TAB_TITLE = "CardView";

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0 :
                return new BluetoothFragment();
            case 1 :
                return new CardViewFragment();
            default:
                return new CardViewFragment(); // only if there is a miss-match with the number of tabs
        }

    }

    @Override
    public int getCount() {
        return NUMBER_OF_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0 :
                return NETWORK_CONN_TAB_TITLE;
            case 1 :
                return CARDVIEW_TAB_TITLE;
            default:
                return null;
        }
    }
}
