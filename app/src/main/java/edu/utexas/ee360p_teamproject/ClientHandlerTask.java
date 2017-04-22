package edu.utexas.ee360p_teamproject;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

/**
 * Created by aria on 4/21/17.
 */

class ClientHandlerTask extends AsyncTask<String, String, TCPClient> {
    private static final String COMMAND = "list";
    private static final String TAG = "ShutdownAsyncTask";
    private Handler handler;

    /**
     * ShutdownAsyncTask constructor with handler passed as argument. The UI is updated via handler.
     * In doInBackground(...) method, the handler is passed to TCPClient object.
     *
     * @param handler Handler object that is retrieved from MainActivity class and passed to TCPClient
     *                class for sending messages and updating UI.
     */
    ClientHandlerTask(Handler handler) {
        this.handler = handler;
    }

    /**
     * Overriden method from AsyncTask class. There the TCPClient object is created.
     *
     * @param params From MainActivity class empty string is passed.
     * @return TCPClient object for closing it socket_in onPostExecute method.
     */
    @Override
    protected TCPClient doInBackground(String... params) {
        Log.d(TAG, "In doInBackground");

        TCPClient tcpClient = null;

        try {
            tcpClient = new TCPClient(handler,
                                      COMMAND,
                                      "10.0.2.2",
                                      this::publishProgress);
            tcpClient.run();
        }
        catch (NullPointerException e) {
            Log.d(TAG, "Caught null pointer exception");
            e.printStackTrace();
        }
        return tcpClient;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(TCPClient tcpClient) {
        super.onPostExecute(tcpClient);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        Log.d(TAG, values[0]);
    }
}
