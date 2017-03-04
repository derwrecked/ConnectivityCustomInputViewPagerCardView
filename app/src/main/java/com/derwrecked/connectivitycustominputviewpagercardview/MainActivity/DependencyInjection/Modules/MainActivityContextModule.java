package com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.DependencyInjection.Modules;

import android.content.Context;

import com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.Views.MainActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Derek on 2/24/2017.
 */
@Module
public class MainActivityContextModule {
    private final Context mContext;
    private final MainActivity mMainActivity;
    public MainActivityContextModule(Context context){
        mContext = context;
        mMainActivity = (MainActivity) context;
    }

    @Provides
    public Context provideContext(){
        return mContext;
    }

    @Provides
    public MainActivity provideMainActivityContext(){
        return mMainActivity;
    }
}
