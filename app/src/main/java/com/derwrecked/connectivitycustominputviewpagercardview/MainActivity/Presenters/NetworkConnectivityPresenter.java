package com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.Presenters;

import com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.Presenters.OperationInterfaces.NetworkConnMVPCommOps;

import java.io.IOException;

/**
 * Created by Derek on 2/25/2017.
 *
 * Here are the interfaces that are created to maintain the MVP communication.
 * RequiredViewOps: required View operations available to Presenter
 * ProvidedPresenterOps: operations offered to View for communication with Presenter
 * RequiredPresenterOps: required Presenter operations available to Model
 * ProvidedModelOps: operations offered to Model to communicate with Presenter
 */


public class NetworkConnectivityPresenter implements
        NetworkConnMVPCommOps.Model2PresenterOps, // methods in the presenter that the model can use
        NetworkConnMVPCommOps.View2PresenterOps{  // methods in the presenter that the view can use
    NetworkConnMVPCommOps.Presenter2ViewOps mView;
    NetworkConnMVPCommOps.Presenter2ModelOps mModel;
    public NetworkConnectivityPresenter(NetworkConnMVPCommOps.Presenter2ViewOps mView){
        this.mView = mView; // give reference to the view
    }
    public void setModel(NetworkConnMVPCommOps.Presenter2ModelOps mModel){
        this.mModel = mModel;
    }

    @Override
    public void updateViewNetworkInfo(String info) {
        mView.updateViewNetworkInfo(info);
    }

    @Override
    public void registerNSDService() throws IOException {
        mModel.registerNSDService();
    }

    @Override
    public void unregisterNSDService() {
        mModel.unregisterNSDService();
    }

    @Override
    public void updateViewDiscoveryState(Boolean bool) {
        mView.updateViewDiscoveryState(bool);
    }

    @Override
    public void updateViewServiceState(Boolean bool) {
        // pass on to view
        mView.updateViewServiceState(bool);
    }

    @Override
    public void startDiscovery() {
        mModel.startDiscovery();
    }

    @Override
    public void stopDiscovery() {
        mModel.stopDiscovery();
    }
}
