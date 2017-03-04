package com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.Models;

import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.util.Log;

import com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.Presenters.OperationInterfaces.NetworkConnMVPCommOps;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import static android.os.Looper.getMainLooper;

/**
 * Created by Derek on 2/25/2017.
 */

public class NetworkConnectivityModel implements
        NetworkConnMVPCommOps.Presenter2ModelOps {
    // interface reference to the presenter so model can talk to the presenter
    private NetworkConnMVPCommOps.Model2PresenterOps mPresenter;
    private WifiP2pManager mWifiP2pManager;
    private WifiP2pManager.Channel mWifiP2pChannel;

    // nsd
    private NsdServiceInfo mNsdServiceInfo;
    private ServerSocket mServerSocket;
    private NsdManager.RegistrationListener mRegistrationListener;
    private NsdManager.DiscoveryListener mDiscoveryListener;
    private NsdManager.ResolveListener mResolveListener;
    private NsdManager mNsdManager;
    private int mLocalPort;
    private String mServiceName = "NsdChat";
    private final String SERVICE_TYPE = "nsdchat._http._tcp";
    private NsdServiceInfo mService;
    private final String TAG = "NetConModel";
    private static boolean listenerStatus = false;

    public NetworkConnectivityModel(NetworkConnMVPCommOps.Model2PresenterOps mPresenter,
                                    WifiP2pManager p2pManager, WifiP2pManager.Channel channel) {
        this.mPresenter = mPresenter;
        this.mWifiP2pManager = p2pManager;
        this.mWifiP2pChannel = channel;
    }

    @Override
    public void registerNSDService() throws IOException {
        initializeRegistrationListener();

        // Initialize a server socket on the next available port.
        mServerSocket = new ServerSocket(0);
        // Store the chosen port.
        mLocalPort = mServerSocket.getLocalPort();

        // Create the NsdServiceInfo object, and populate it.
        mNsdServiceInfo = new NsdServiceInfo();
        // The name is subject to change based on conflicts
        // with other services advertised on the same network.
        mNsdServiceInfo.setServiceName(mServiceName);
        mNsdServiceInfo.setServiceType(SERVICE_TYPE);
        mNsdServiceInfo.setPort(mLocalPort);
        mNsdManager.registerService(
                mNsdServiceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
    }


    private void initializeRegistrationListener() {
        listenerStatus = false;
        mRegistrationListener = new NsdManager.RegistrationListener() {

            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                Log.d(TAG, "onServiceRegistered");
                // Save the service name.  Android may have changed it in order to
                // resolve a conflict, so update the name you initially requested
                // with the name Android actually used.
                mServiceName = NsdServiceInfo.getServiceName();

                // TODO: this works but is it the best solution?.
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.updateViewServiceState(false); // set start to off and stop to on
                    }
                });

                listenerStatus = true;
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.d(TAG, "onRegistrationFailed");
                // Registration failed!  Put debugging code here to determine why.
                // TODO: see above.
                listenerStatus = false;
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Service listener ran on ui thread.");
                        mPresenter.updateViewServiceState(false); // set start to off and stop to on
                    }
                });
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {
                Log.d(TAG, "onServiceUnregistered");
                // Service has been unregistered.  This only happens when you call
                // NsdManager.unregisterService() and pass in this listener.
                mPresenter.updateViewServiceState(true);
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.d(TAG, "onUnregistrationFailed");
                // Unregistration failed.  Put debugging code here to determine why.
                mPresenter.updateViewServiceState(true);
            }
        };
    }

    @Override
    public void unregisterNSDService() {
        if (mNsdManager != null && mRegistrationListener != null && listenerStatus != false){
            mNsdManager.unregisterService(mRegistrationListener);
        }
        mRegistrationListener = null;
        mPresenter.updateViewServiceState(false); // set start to on and stop off
    }



    private void initializeDiscoveryListener() {
        //initializeResolveListener(); // used to determine the connection information
        // Instantiate a new DiscoveryListener
        mDiscoveryListener = new NsdManager.DiscoveryListener() {

            //  Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
                //mPresenter.updateViewServiceState(true);
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {

                // A service was found!  Do something with it.
                Log.d(TAG, "Service discovery success" + service);
                String string = service.getServiceType()+ "\n" +
                        service.getServiceName();
                mPresenter.updateViewNetworkInfo(string);

            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
                Log.e(TAG, "service lost" + service);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }
        };
    }

    public void initializeResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Called when the resolve fails.  Use the error code to debug.
                Log.e(TAG, "Resolve failed" + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.e(TAG, "Resolve Succeeded. " + serviceInfo);

                if (serviceInfo.getServiceName().equals(mServiceName)) {
                    Log.d(TAG, "Same IP.");
                    return;
                }
                mService = serviceInfo;
                int port = mService.getPort();
                InetAddress host = mService.getHost();
            }
        };
    }

    @Override
    public void startDiscovery() {
        stopDiscovery();  // Cancel any existing discovery request
        initializeDiscoveryListener();
        mNsdManager.discoverServices(
                "_http._tcp", NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
        mPresenter.updateViewDiscoveryState(true);
    }

    @Override
    public void stopDiscovery() {
        if (mDiscoveryListener != null) {
            try {
                mNsdManager.stopServiceDiscovery(mDiscoveryListener);
            } finally {
                mDiscoveryListener = null;
            }
        }
        mPresenter.updateViewDiscoveryState(false);
    }

    public void tearDown() {
        Log.d(TAG, "tearDown called");
        if (mResolveListener != null){
            mResolveListener = null;
        }
        if (mNsdManager != null && mDiscoveryListener != null){
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
        }
        if(mDiscoveryListener != null){
            mResolveListener = null;
        }
    }
}
