package edu.utexas.ee360p_teamproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import edu.utexas.ee360p_teamproject.ClientRequestHandler.RequestHandler;

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
        List<String> result = RequestHandler.listOfAllRooms();

        if (result != null)
            result.forEach(s -> Log.d(TAG, s));
        else Log.d(TAG, "No rooms received from server :(");
    }
}
