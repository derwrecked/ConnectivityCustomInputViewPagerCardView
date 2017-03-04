package com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.Presenters.OperationInterfaces;

import java.io.IOException;

/**
 * Created by Derek on 2/25/2017.
 */

public interface NetworkConnMVPCommOps {
    /**
     * Created by Derek on 2/25/2017.
     * How the presenter accesses the model.
     */

    interface Presenter2ModelOps {
        void registerNSDService() throws IOException;
        void unregisterNSDService();
        void startDiscovery();
        void stopDiscovery();
    }

    /**
     * Created by Derek on 2/25/2017.
     * How the Model accesses the presenter.
     * Methods the Model can use in the presenter
     *
     */

    interface Model2PresenterOps {
        void updateViewNetworkInfo(String info);
        void updateViewServiceState(Boolean bool);
        void updateViewDiscoveryState(Boolean bool);
    }

    /**
     * Created by Derek on 2/25/2017.
     * How the View can talk to the presenter.
     */

    interface View2PresenterOps {
        void registerNSDService() throws IOException;
        void unregisterNSDService();
        void startDiscovery();
        void stopDiscovery();

    }


    /**
     * Created by Derek on 2/25/2017.
     *
     * Contains operations that the presenter can perform to the view ie
     * tell view to update a list view.
     */
    interface Presenter2ViewOps {
        void updateViewNetworkInfo(String info);
        void updateViewServiceState(Boolean bool);
        void updateViewDiscoveryState(Boolean bool);
    }
}
