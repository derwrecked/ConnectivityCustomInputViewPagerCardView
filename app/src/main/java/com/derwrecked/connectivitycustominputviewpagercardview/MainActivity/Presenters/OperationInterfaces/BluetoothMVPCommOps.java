package com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.Presenters.OperationInterfaces;

/**
 * Created by Derek on 2/25/2017.
 */

public interface BluetoothMVPCommOps {
    /**
     * Created by Derek on 2/25/2017.
     * How the presenter accesses the model.
     */

    interface Presenter2ModelOps {
    }

    /**
     * Created by Derek on 2/25/2017.
     * How the Model accesses the presenter.
     * Methods the Model can use in the presenter
     *
     */

    interface Model2PresenterOps {
    }

    /**
     * Created by Derek on 2/25/2017.
     * How the View can talk to the presenter.
     */

    interface View2PresenterOps {

    }


    /**
     * Created by Derek on 2/25/2017.
     *
     * Contains operations that the presenter can perform to the view ie
     * tell view to update a list view.
     */
    interface Presenter2ViewOps {
    }
}
