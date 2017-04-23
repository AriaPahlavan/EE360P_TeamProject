package edu.utexas.ee360p_teamproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
        try {
            List<String> result = new ClientTask(ClientTask.INIT, null).execute().get().run();

            result.forEach(s -> Log.d(TAG, s));
        }
        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
