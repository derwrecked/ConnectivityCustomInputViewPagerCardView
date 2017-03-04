package com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.DependencyInjection.Components;

import com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.DependencyInjection.Modules.ViewPagerAdapterModule;
import com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.DependencyInjection.Scopes.MainActivityScope;
import com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.Views.MainActivity;

import dagger.Component;

/**
 * Created by Derek on 2/24/2017.
 */

// module will perform the work of constructing the object and dagger
// will do the rest of the work.
@Component(modules = {ViewPagerAdapterModule.class})
// need to list modules to create my component.
@MainActivityScope
//Dagger will create an instance of this (subclass)
public interface MainActivityComponent {
    // Bar Component needs to use these object but where are they created?
    // Modules are the groupings of
    void inject(MainActivity mainActivity);
}
