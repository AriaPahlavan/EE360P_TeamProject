package edu.utexas.ee360p_teamproject;

import android.util.Log;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static edu.utexas.ee360p_teamproject.RequestHandler.CONNECTING;
import static edu.utexas.ee360p_teamproject.RequestHandler.ERROR;
import static edu.utexas.ee360p_teamproject.RequestHandler.RECEIVED;
import static edu.utexas.ee360p_teamproject.RequestHandler.SENDING;
import static edu.utexas.ee360p_teamproject.RequestHandler.SENT;

/**
 * Created by aria on 4/21/17.
 */

class TCPConnection {
    private static final String TAG = "TCPConnection";
    private static int port   = 8080;
    private final static String ip  = "10.0.2.2";
    private String command, tag;


    /**
     * @param tag
     * @param command  command for the server
     */
    TCPConnection(String tag, String command) {
        this.tag = tag;
        this.command  = command;
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

    List<String> run() {
        Log.d(TAG, "Connecting...");

        String response;
        List<String> serverResponse = new ArrayList<>();

        log(CONNECTING);

        ClientSocket clientSocket;

        try {
            clientSocket = new ClientSocket(ip, port);

            log(SENDING);

            switch (tag) {
                case ClientTask.INIT:
                    response = clientSocket.inStream()
                                           .readLine();

                    int roomCount = Integer.parseInt(response);

                    for (int i = 0; i < roomCount; i++)
                        serverResponse.add(clientSocket.inStream()
                                                       .readLine());
                    break;
                case ClientTask.ROOM:
                    sendMessage(command,
                                clientSocket.outStream());

                    //Update port to desired chat-room
                    port = Integer.parseInt(clientSocket.inStream()
                                                        .readLine());
                    break;
                case ClientTask.SEND:
                    sendMessage(command,
                                clientSocket.outStream());
                    break;
                case ClientTask.UPDATE:
                    sendMessage(command,
                                clientSocket.outStream());
                    sendMessage("GetALL",
                                clientSocket.outStream());

                    response = clientSocket.inStream()
                                           .readLine();

                    serverResponse.add(response);
                    int newPostsCount = Integer.parseInt(response);

                    for (int i = 0; i < newPostsCount; i++) {
                        String timeStamp = clientSocket.inStream()
                                                       .readLine();
                        String author    = clientSocket.inStream()
                                                       .readLine();
                        String msg       = clientSocket.inStream()
                                                       .readLine();
                        serverResponse.add(timeStamp);
                        serverResponse.add(author);
                        serverResponse.add(msg);
                    }
                    break;
            }

            log(RECEIVED);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Error", e);
            log(ERROR);

        } finally {
            log(SENT);
            Log.d(TAG, "Socket Closed");
        }

        return serverResponse;
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
}
