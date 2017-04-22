package edu.utexas.ee360p_teamproject;

import android.os.Handler;

/**
 * Created by aria on 4/22/17.
 */

public class ClientHandler {
    private final static String TAG = "ClientHandler";
    public final static int CONNECTING = 0;
    public final static int SENDING = 1;
    public final static int SENT = 2;
    public final static int RECEIVED = 3;
    public final static int ERROR = 4;
    private static Handler handler;

    private ClientHandler(Handler handler) {
        this.handler = handler;
//        handler = new Handler() {
//            public void handleMessage(Message msg) {
//                switch (msg.what) {
//                    case CONNECTING: Log.d(TAG, "Connecting..."); break;
//                    case SENDING:    Log.d(TAG, "Sending...");    break;
//                    case SENT:       Log.d(TAG, "Sent...");       break;
//                    case RECEIVED:   Log.d(TAG, "Received...");   break;
//                    case ERROR:      Log.d(TAG, "Error...");      break;
//                    default:         Log.d(TAG, "Default value... ERROR");
//                }
//            }
//        };
    }

    public static Handler singletonHandler(){
        if (handler==null) new ClientHandler(null);

        return handler;
    }
}