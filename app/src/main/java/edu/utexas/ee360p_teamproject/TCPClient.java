package edu.utexas.ee360p_teamproject;

import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Optional;

/**
 * Created by aria on 4/21/17.
 */

class TCPClient {
    private static final String TAG = "TCPClient";
    private final Handler handler;
    private String ip, incomingMessage, command;
    private MessageCallback listener = null;
    private boolean mRun = false;
    private final static int port = 8000;


    /**
     * @param handler  handler for updating the UI with sent messages
     * @param command  command for the server
     * @param ipNumber ip number for the server
     * @param listener Callback listener
     */
    TCPClient(Handler handler, String command, String ipNumber, MessageCallback listener) {
        this.handler = handler;
        this.command = command;
        this.ip = ipNumber;
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
        handler.sendEmptyMessageDelayed(MainActivity.SENDING, 1000);
        Log.d(TAG, "Sent Message: " + message);
    }

    /**
     * stops the TCPClient object from AsyncTask
     */
    public void stopClient() {
        Log.d(TAG, "Client stopped!");
        mRun = false;
    }

    void run() {
        Log.d(TAG, "Connecting...");

        mRun = true;
        handler.sendEmptyMessageDelayed(MainActivity.CONNECTING, 1000);

        ClientSocket clientSocket;

            try {
                clientSocket = new ClientSocket(ip, port);

                handler.sendEmptyMessageDelayed(MainActivity.SENDING, 2000);
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
                handler.sendEmptyMessageDelayed(MainActivity.ERROR, 2000);

            } finally {
                handler.sendEmptyMessageDelayed(MainActivity.SENT, 3000);
                Log.d(TAG, "Socket Closed");
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
