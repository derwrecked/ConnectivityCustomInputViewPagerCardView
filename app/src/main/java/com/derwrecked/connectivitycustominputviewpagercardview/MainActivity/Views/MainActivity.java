package com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.Views;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.DependencyInjection.Components.DaggerMainActivityComponent;
import com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.DependencyInjection.Components.MainActivityComponent;
import com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.DependencyInjection.Modules.MainActivityContextModule;
import com.derwrecked.connectivitycustominputviewpagercardview.R;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements
        NetworkConnectivityFragment.NetConnFragToMainActivity{
    ViewPager mViewPager;
    @Inject
    MyViewPagerAdapter mMyViewPagerAdapter;
    private static boolean switchNumber = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // set my toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainActivityToolbar);
        setSupportActionBar(toolbar);

        MainActivityComponent comp = DaggerMainActivityComponent.builder()
                .mainActivityContextModule(new MainActivityContextModule(this))
                .build();
        comp.inject(this);

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mViewPager = (ViewPager) findViewById(R.id.mainActivityViewPager);
        mViewPager.setAdapter(mMyViewPagerAdapter);
        // create tab layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.mainActivityTabBar);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.mainActivityToolbar_networkServiceStatus){
            if(switchNumber){
                updateViewServiceState(true);
                switchNumber = false;
            }else{
                updateViewServiceState(false);
                switchNumber = true;
            }
        }
        return true;
    }
    */

    @Override
    public void updateViewServiceState(Boolean bool) {
        ActionMenuItemView item = (ActionMenuItemView) findViewById(R.id.mainActivityToolbar_networkServiceStatus);
        Drawable drawable;
        if(bool){
            drawable = getResources().getDrawable(R.drawable.greenbox_27x27);
            item.setIcon(drawable);
        }else{
            drawable = getResources().getDrawable(R.drawable.redbox_27x27);
            item.setIcon(drawable);
        }
    }
}
