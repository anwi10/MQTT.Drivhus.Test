package com.example.anderswinter.mqttdrivhustest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    MqttAndroidClient client;
    String clientId;
    Boolean flag = false;
    EditText EtPort,ETPublish,EtBrokerUI,ETTopic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EtBrokerUI  = (EditText) findViewById(R.id.EtBrokerUI);
        EtPort      = (EditText) findViewById(R.id.EtPort);
        ETPublish   = (EditText) findViewById(R.id.ETPublish);
        ETTopic     = (EditText) findViewById(R.id.ETTopic);

    }

    public void pub(View v) {
        if (flag) {
            String topic = ETTopic.getText().toString();
            String message = ETPublish.getText().toString();
            byte[] encodedPayload = new byte[0];
            try {
                client.publish(topic, message.getBytes(), 0, false);
                Toast.makeText(MainActivity.this, "Published!",Toast.LENGTH_LONG).show();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(MainActivity.this, "Not connected to a broker",Toast.LENGTH_LONG).show();
        }
    }

    public  void Connect(View v){
        clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), EtBrokerUI.getText().toString() + ":" + EtPort.getText().toString(), clientId);
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(MainActivity.this,"Connected",Toast.LENGTH_LONG).show();
                    flag = true;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(MainActivity.this,"Not connected!",Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void Disconnect(View v) {
        if (flag) {
            try {
                flag = false;
                IMqttToken token = client.disconnect();
                token.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        // We are connected
                        Toast.makeText(MainActivity.this, "Disconnected", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        // Something went wrong e.g. connection timeout or firewall problems
                        Toast.makeText(MainActivity.this, "Could not disconnect!", Toast.LENGTH_LONG).show();

                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(MainActivity.this, "Nothing to disconnect", Toast.LENGTH_LONG).show();

        }
    }
}

