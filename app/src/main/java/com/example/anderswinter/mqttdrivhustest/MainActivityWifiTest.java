package com.example.anderswinter.mqttdrivhustest;

/**
 * Created by Anders on 10/16/2017.
 */


import android.app.DownloadManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class MainActivityWifiTest extends AppCompatActivity  {

    EditText EtPort,ETPublish,EtBrokerUI,ETTopic;
    TextView TVGetRequest;
    WifiManager wifi;
    WifiConfiguration config;
    ConnectivityManager connManager;
    NetworkInfo mWifi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wifi_test);
        TVGetRequest = (TextView) findViewById(R.id.textView);
        wifi = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);
        config = new WifiConfiguration();
        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    }


    public void ConnectToMicroPythoon(View v){

        String SSID = "MicroPython-06519c";
        String Key  = "micropythoN";
        config.SSID = "\"" + SSID + "\"";
        config.preSharedKey = "\"" + Key + "\"";
        /*config.status = WifiConfiguration.Status.ENABLED;
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);*/
        if (!wifi.isWifiEnabled()){
            wifi.setWifiEnabled(true);
            wifi.startScan();
        }

        wifi.addNetwork(config);
        List<WifiConfiguration> list = wifi.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {

            if (wifi.enableNetwork(i.networkId, true)) {
                Toast.makeText(MainActivityWifiTest.this, "Is connected!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivityWifiTest.this, "Not connected!", Toast.LENGTH_LONG).show();
            }
        }
        }

public void SendRequest(View v){

    RequestQueue queue = Volley.newRequestQueue(this);
    String url ="http://google.com";

// Request a string response from the provided URL.
    StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
            new Response.Listener<String>(){
                @Override
                public void onResponse(String response){
                    // Display the first 500 characters of the response string.
                    TVGetRequest.setText("Response is: "+ response.substring(0,500));
                }
            }, new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            TVGetRequest.setText("That didn't work!");
        }
    });
    // Add the request to the RequestQueue.
        queue.add(stringRequest);


        }
}
