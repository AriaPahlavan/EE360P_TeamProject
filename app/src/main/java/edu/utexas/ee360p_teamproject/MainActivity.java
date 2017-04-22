package edu.utexas.ee360p_teamproject;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import static edu.utexas.ee360p_teamproject.ClientHandler.CONNECTING;
import static edu.utexas.ee360p_teamproject.ClientHandler.ERROR;
import static edu.utexas.ee360p_teamproject.ClientHandler.RECEIVED;
import static edu.utexas.ee360p_teamproject.ClientHandler.SENDING;
import static edu.utexas.ee360p_teamproject.ClientHandler.SENT;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testConnectionToServer();
    }

    private void testConnectionToServer() {
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CONNECTING: Log.d(TAG, "Connecting..."); break;
                    case SENDING:    Log.d(TAG, "Sending...");    break;
                    case SENT:       Log.d(TAG, "Sent...");       break;
                    case RECEIVED:   Log.d(TAG, "Received...");   break;
                    case ERROR:      Log.d(TAG, "Error...");      break;
                    default:         Log.d(TAG, "Default value... ERROR");
                }
            }
        };

        new ClientTask().execute();
    }
}
