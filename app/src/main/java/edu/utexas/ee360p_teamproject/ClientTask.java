package edu.utexas.ee360p_teamproject;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by aria on 4/21/17.
 */

class ClientTask extends AsyncTask<String, String, TCPConnection> {
    private static final String COMMAND = "list";
    private static final String TAG = "ShutdownAsyncTask";
    static final String INIT   = "Initialization";
    static final String ROOM   = "Select a room";
    static final String SEND   = "Send new messages";
    static final String UPDATE = "Get Updates";

    /**
     * ShutdownAsyncTask constructor with handler passed as argument. The UI is updated via handler.
     * In doInBackground(...) method, the handler is passed to TCPConnection object.
     *
     */
    ClientTask() {}

    /**
     * Overriden method from AsyncTask class. There the TCPConnection object is created.
     *
     * @param params From MainActivity class empty string is passed.
     * @return TCPConnection object for closing it socket_in onPostExecute method.
     */
    @Override
    protected TCPConnection doInBackground(String... params) {
        Log.d(TAG, "In doInBackground");

        String command;

        switch (params[0]) {
            case INIT:  break;
            case ROOM:  break;
            case SEND:  break;
            case UPDATE:break;
        }

        TCPConnection connection = null;

        try {
            connection = new TCPConnection(COMMAND,
                                          this::publishProgress);
            connection.run();
        }
        catch (NullPointerException e) {
            Log.d(TAG, "Caught null pointer exception");
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(TCPConnection tcpClient) {
        super.onPostExecute(tcpClient);
    }

    // TODO: 4/22/17 remove build.version limit
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        Arrays.stream(values)
              .forEachOrdered(msg -> Log.d(TAG, msg));
    }
}
