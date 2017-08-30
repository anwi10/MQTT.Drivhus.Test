package com.example.anderswinter.mqttdrivhustest;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.text.Normalizer;

public class MainActivity extends AppCompatActivity {

    MqttAndroidClient client;
    String clientId;
    Boolean connected = false;
    EditText EtPort,ETPublish,EtBrokerUI,ETTopic;
    TextView TwSubscribe;
    MqttConnectOptions options;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        clientId = MqttClient.generateClientId();
        EtBrokerUI  = (EditText) findViewById(R.id.EtBrokerUI);
        EtPort      = (EditText) findViewById(R.id.EtPort);
        ETPublish   = (EditText) findViewById(R.id.ETPublish);
        ETTopic     = (EditText) findViewById(R.id.ETTopic);
        TwSubscribe = (TextView) findViewById(R.id.TwSubscribe);
        client =
                new MqttAndroidClient(this.getApplicationContext(), "tcp://" + EtBrokerUI.getText().toString() + ":" + EtPort.getText().toString(),
                        clientId);

    }

    public void Connect(View v)
    {
        client =
                new MqttAndroidClient(this.getApplicationContext(), "tcp://" + EtBrokerUI.getText().toString() + ":" + EtPort.getText().toString(),
                        clientId);
        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(MainActivity.this,"Connected",Toast.LENGTH_LONG).show();
                    sub();
                    connected = true;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(MainActivity.this,"Not connected!",Toast.LENGTH_LONG).show();
                    connected = false;

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Toast.makeText(MainActivity.this,"Lost connection!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                TwSubscribe.append(new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }
    public void ClearDisplay(View v){
        TwSubscribe.setText("");
    }

    public void pub(View v) {
        if (connected) {
            String topic = ETTopic.getText().toString();
            String message = ETPublish.getText().toString();
            byte[] encodedPayload = new byte[0];
            try {
                client.publish(topic, message.getBytes(), 0, false);
                Toast.makeText(MainActivity.this, "Published! ",Toast.LENGTH_LONG).show();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(MainActivity.this, "Not connected to a broker",Toast.LENGTH_LONG).show();
        }
    }

    public void sub(){
        int qos = 1;
        try {
            IMqttToken subToken = client.subscribe(ETTopic.getText().toString(), qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this, "subscribed to " + ETTopic.getText().toString(),Toast.LENGTH_LONG).show();
                    // The message was published
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    Toast.makeText(MainActivity.this, "could not subscribe",Toast.LENGTH_LONG).show();
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }




    public void Disconnect(View v) {
        if (connected) {
            try {
                connected = false;
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

