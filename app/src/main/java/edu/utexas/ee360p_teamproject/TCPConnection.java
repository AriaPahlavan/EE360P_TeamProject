package edu.utexas.ee360p_teamproject;

import android.os.Handler;
import android.util.Log;

import java.io.PrintWriter;

import static edu.utexas.ee360p_teamproject.ClientHandler.*;

/**
 * Created by aria on 4/21/17.
 */

class TCPConnection {
    private static final String TAG = "TCPConnection";
    private final static int port   = 8080;
    private final static String ip  = "10.0.2.2";
    private MessageCallback listener = null;
    private String command;
    private boolean mRun = false;


    /**
     * @param command  command for the server
     * @param listener Callback listener
     */
    TCPConnection(String command, MessageCallback listener) {
        this.command  = command;
        this.listener = listener;
    }

    /**
     * sends the message via socket_out
     *
     * @param message    Message passed as an argument and sent via OutputStream object.
     * @param socket_out socket output stream
     */
    private void sendMessage(String message, PrintWriter socket_out) {
        if (socket_out == null || socket_out.checkError())
            return;

        socket_out.println(message);
        socket_out.flush();
        log(SENDING);
        Log.d(TAG, "Sent Message: " + message);
    }

    /**
     * stops the TCPConnection object from AsyncTask
     */
    public void stopClient() {
        Log.d(TAG, "Client stopped!");
        mRun = false;
    }

    void run() {
        Log.d(TAG, "Connecting...");
        String incomingMessage = "No incoming messages";
        mRun = true;
        log(CONNECTING);

        ClientSocket clientSocket;

        try {
            clientSocket = new ClientSocket(ip, port);

            log(SENDING);
            this.sendMessage(command,
                             clientSocket.outStream());

            //Listen for the incoming messages
            while (mRun) {
                incomingMessage = clientSocket.inStream()
                                              .readLine();

                if (incomingMessage != null && listener != null)
                    listener.callbackMessageReceiver(incomingMessage);

                incomingMessage = null;
            }

            Log.d(TAG, "Received Message: " + incomingMessage);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Error", e);
            log(ERROR);

        } finally {
            log(SENT);
            Log.d(TAG, "Socket Closed");
        }
    }

    private void log(int tag){
        switch (tag) {
            case CONNECTING: Log.d(TAG, "Connecting..."); break;
            case SENDING:    Log.d(TAG, "Sending...");    break;
            case SENT:       Log.d(TAG, "Sent...");       break;
            case RECEIVED:   Log.d(TAG, "Received...");   break;
            case ERROR:      Log.d(TAG, "Error...");      break;
            default:         Log.d(TAG, "Default value... ERROR");
        }
    }

    /**
     * Callback Interface for sending received messages to 'onPublishProgress' method in AsyncTask.
     */
    @FunctionalInterface
    interface MessageCallback {
        void callbackMessageReceiver(String message);
    }
}
