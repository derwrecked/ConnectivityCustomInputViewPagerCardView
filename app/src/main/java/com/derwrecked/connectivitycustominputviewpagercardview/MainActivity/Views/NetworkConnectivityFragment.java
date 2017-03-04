package com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.Views;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.Models.NetworkConnectivityModel;
import com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.Models.WiFiDirectBroadcastReceiver;
import com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.Presenters.NetworkConnectivityPresenter;
import com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.Presenters.OperationInterfaces.NetworkConnMVPCommOps;
import com.derwrecked.connectivitycustominputviewpagercardview.R;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by Derek on 2/23/2017.
 */

public class NetworkConnectivityFragment extends Fragment implements
        NetworkConnMVPCommOps.Presenter2ViewOps{

    public final static String FRAGMENT_TAG_NETWORK_CONN= "network_connectivity_fragment";

    public static final String TXTRECORD_PROP_AVAILABLE = "available";
    public static final String SERVICE_INSTANCE = "_wifidemotest1";
    public static final String SERVICE_REG_TYPE = "_presence._tcp";
    private WifiP2pDnsSdServiceRequest serviceRequest;

    // create activity based methods
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private WifiP2pManager.PeerListListener mPeerListListener;

    private NetworkConnMVPCommOps.View2PresenterOps mPresenter;
    private WiFiDirectBroadcastReceiver mP2pBroadcastReceiver;
    private TextView mNetworkTextInfo;
    private Button mRegisterNSDServiceButton;
    private Button mUnregisterNSDServiceButton;
    private Button mStopDiscovery;
    private Button mStartDiscovery;
    private NetConnFragToMainActivity mInterfaceWithMainActivity;
    private IntentFilter mIntentFilter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.network_conn_fragment, container, false);
        Bundle args = getArguments();
        // extablish communication conduit with mainactivity
        mInterfaceWithMainActivity = (NetConnFragToMainActivity) getActivity();
        // setup Model View Presenter
        setupMVP();
        // setup intent filter for wifi P2P services
        setupIntentFilter();

        mNetworkTextInfo = (TextView) view.findViewById(R.id.NetConnFragTextView_serviceInfo);
        mRegisterNSDServiceButton = (Button) view.findViewById(R.id.NetConnFragButton_registerNSDService);
        mUnregisterNSDServiceButton = (Button) view.findViewById(R.id.NetConnFragButton_unregisterNSDService);
        mStartDiscovery = (Button) view.findViewById(R.id.NetConnFragButton_startDiscovery);
        mStopDiscovery = (Button) view.findViewById(R.id.NetConnFragButton_stopDiscovery);
        mRegisterNSDServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegistration();
/*                try {
                    mPresenter.registerNSDService();
                } catch (IOException e) {
                    Toast.makeText(getActivity(), "Error registering service.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }*/
            }
        });
        mUnregisterNSDServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.unregisterNSDService();
            }
        });

        mStartDiscovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.startDiscovery();
            }
        });
        mStopDiscovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.stopDiscovery();
            }
        });



        mRegisterNSDServiceButton.setEnabled(true);
        mUnregisterNSDServiceButton.setEnabled(false);
        mStartDiscovery.setEnabled(true);
        mStopDiscovery.setEnabled(false);
        return view;
    }

    /**
     * Setup Model View Presenter pattern
     */
    private void setupMVP() {

        // Create the Presenter
        NetworkConnectivityPresenter presenter = new NetworkConnectivityPresenter(this);
        // Create the Model
        NetworkConnectivityModel model = new NetworkConnectivityModel(presenter, wifiP2pManager, channel);
        // Set Presenter model
        presenter.setModel(model);
        // Set the Presenter as a interface
        mPresenter = presenter;
    }




    @Override
    public void updateViewNetworkInfo(String info) {
        mNetworkTextInfo.setText(info);
    }

    @Override
    public void updateViewServiceState(Boolean bool) {
        if(bool){
            mRegisterNSDServiceButton.setEnabled(false);
            mUnregisterNSDServiceButton.setEnabled(true);
        }else{
            mRegisterNSDServiceButton.setEnabled(true);
            mUnregisterNSDServiceButton.setEnabled(false);
        }
    }

    /**
     * If false this means discovery is on and turn start button off and stop button on
     * if true discovery off and turn stop off and start on.
     * @param bool
     */
    @Override
    public void updateViewDiscoveryState(Boolean bool) {
        if(bool){
            mStartDiscovery.setEnabled(false);
            mStopDiscovery.setEnabled(true);
        }else{
            mStartDiscovery.setEnabled(true);
            mStopDiscovery.setEnabled(false);
        }
    }

    @Override
    public void onPause() {
        mPresenter.unregisterNSDService();
        Log.d("NetConnFragment", "onPause");
        super.onPause();
        getActivity().unregisterReceiver(mP2pBroadcastReceiver);
        mP2pBroadcastReceiver = null;
        mPeerListListener = null;
    }

    @Override
    public void onResume() {
        Log.d("NetConnFragment", "onResume");
        super.onResume();
        wifiP2pManager = (WifiP2pManager) getContext().getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(getContext(), Looper.getMainLooper(), null);
        mP2pBroadcastReceiver = new WiFiDirectBroadcastReceiver(wifiP2pManager, channel, getActivity());
        getActivity().registerReceiver(mP2pBroadcastReceiver, mIntentFilter);
    }

    @Override
    public void onDestroy() {
        mPresenter.unregisterNSDService();
        super.onDestroy();
    }


    // communication from this fragment to the master activity
    public interface NetConnFragToMainActivity{
        void updateViewServiceState(Boolean bool);
    }

    /**
     *
     * WIFI_P2P_STATE_CHANGED_ACTION
     * Indicates whether Wi-Fi P2P is enabled
     *
     * WIFI_P2P_PEERS_CHANGED_ACTION
     * Indicates that the available peer list has changed.
     *
     * WIFI_P2P_CONNECTION_CHANGED_ACTION
     * Indicates the state of Wi-Fi P2P connectivity has changed.
     *
     * WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
     * Indicates this device's configuration details have changed.
     */
    private void setupIntentFilter(){
        //  Indicates a change in the Wi-Fi P2P status.
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }


    private void startRegistration() {
        // Initialize a server socket on the next available port.
        ServerSocket mServerSocket = null;
/*        try {
            mServerSocket = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        // Store the chosen port.
        int mLocalPort = 4747;
        //  Create a string map containing information about your service.
        Map<String, String> record = new HashMap<String, String>();
        record.put(TXTRECORD_PROP_AVAILABLE, "visible");
        WifiP2pDnsSdServiceInfo service = WifiP2pDnsSdServiceInfo.newInstance(
                SERVICE_INSTANCE, SERVICE_REG_TYPE, record);
        wifiP2pManager.addLocalService(channel, service, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("P2P_startReg", "added local service");
            }
            @Override
            public void onFailure(int error) {
                Log.d("P2P_startReg", "failed to add local service");
            }
        });

        discoverService();
    }


    private void discoverService() {
        /*
         * Register listeners for DNS-SD services. These are callbacks invoked
         * by the system when a service is actually discovered.
         */
        wifiP2pManager.setDnsSdResponseListeners(channel,
                new WifiP2pManager.DnsSdServiceResponseListener() {
                    @Override
                    public void onDnsSdServiceAvailable(String instanceName,
                                                        String registrationType, WifiP2pDevice srcDevice) {
                        // A service has been discovered. Is this our app?
                        if (instanceName.equalsIgnoreCase(SERVICE_INSTANCE)) {
                            // update the UI and add the item the discovered
                            // device.
                        }

                        Log.d(TAG, "DnsSdServiceAvailable");
                        Log.d(TAG, "deviceAddress" + srcDevice.deviceAddress);
                        Log.d(TAG, "deviceName" + srcDevice.deviceName);
                    }
                }, new WifiP2pManager.DnsSdTxtRecordListener() {
                    /**
                     * A new TXT record is available. Pick up the advertised
                     * buddy name.
                     */
                    @Override
                    public void onDnsSdTxtRecordAvailable(
                            String fullDomain, Map record, WifiP2pDevice device) {
                        Log.d(TAG, "DnsSdTxtRecord available -" + record.toString());
                        String buddyname = (String) record.get("buddyname");
                        Log.d(device.deviceAddress, buddyname);
                    }
                });
        // After attaching listeners, create a service request and initiate
        // discovery.
        serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        wifiP2pManager.addServiceRequest(channel, serviceRequest,
                new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("P2P_addSerReq", "Added service discovery request");
                    }
                    @Override
                    public void onFailure(int arg0) {
                        Log.d("P2P_addSerReq", "Failed adding service discovery request");
                    }
                });
        wifiP2pManager.discoverServices(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("P2P_disService", "successfully discover services");
            }
            @Override
            public void onFailure(int arg0) {
                Log.d("P2P_disService", "Failed to discover services");
            }
        });
    }




}
