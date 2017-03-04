package com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.DependencyInjection.Modules;

import android.support.v4.app.FragmentManager;

import com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.DependencyInjection.Scopes.MainActivityScope;
import com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.Views.MainActivity;
import com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.Views.MyViewPagerAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Derek on 2/24/2017.
 */

@Module(includes = MainActivityContextModule.class)
public class ViewPagerAdapterModule {
    @Provides
    @MainActivityScope
    MyViewPagerAdapter provideBar(FragmentManager fragmentManager) {
        return new MyViewPagerAdapter(fragmentManager);
    }

    @Provides
    @MainActivityScope
    FragmentManager provideSupportFragmentManager(MainActivity mainActivity){
        return mainActivity.getSupportFragmentManager();
    }

}
