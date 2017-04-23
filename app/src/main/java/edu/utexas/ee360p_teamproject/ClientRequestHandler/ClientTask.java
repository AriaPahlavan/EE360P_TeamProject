package edu.utexas.ee360p_teamproject.ClientRequestHandler;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by aria on 4/21/17.
 */

class ClientTask extends AsyncTask<String, String, TCPConnection> {
    private static final String TAG = "ClientTask";
    private String command;
    private String tag;
    static final String INIT   = "Initialization";
    static final String ROOM   = "Select a room";
    static final String SEND   = "Send new messages";
    static final String UPDATE = "Get Updates";

    /**
     * ShutdownAsyncTask constructor with handler passed as argument. The UI is updated via handler.
     * In doInBackground(...) method, the handler is passed to TCPConnection object.
     *
     */
    ClientTask(String tag, String command) {
        this.tag = tag;
        this.command = command;
    }

    /**
     * create a tcp object and return it.
     *
     * @param params From MainActivity class empty string is passed.
     * @return TCPConnection object for closing it in onPostExecute method.
     */
    @Override
    protected TCPConnection doInBackground(String... params) {
        Log.d(TAG, "In doInBackground");

        TCPConnection connection = null;

        try {
            connection = new TCPConnection(tag,
                                           command);
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

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
}
