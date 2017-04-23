package edu.utexas.ee360p_teamproject.ClientRequestHandler;

import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static edu.utexas.ee360p_teamproject.ClientRequestHandler.TCPConnection.ConnectionStatus.*;


/**
 * Created by aria on 4/21/17.
 */

class TCPConnection {
    private static final String TAG = "TCPConnection";
    private static int port   = 8080;
    private final static String ip  = "10.0.2.2";
    private String command, tag;

    enum ConnectionStatus{
        CONNECTING,
        ERROR,
        RECEIVED,
        SENDING,
        SENT
    }


    TCPConnection(String tag, String command) {
        this.tag = tag;
        this.command  = command;
    }

    List<String> run() {
        Log.d(TAG, "Connecting...");
        log(CONNECTING);

        List<String> serverResponse = new ArrayList<>();
        ClientSocket clientSocket;

        try {
            log(SENDING);

            clientSocket = new ClientSocket(ip, port);

            switch (tag) {
                case ClientTask.INIT:
                    serverResponse = initializeServer(clientSocket);
                    break;
                case ClientTask.ROOM:
                    selectChatroom(clientSocket);
                    break;
                case ClientTask.SEND:
                    sendMessage(command,
                                clientSocket.outStream());
                    break;
                case ClientTask.UPDATE:
                    serverResponse = getNotifications(clientSocket);
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

    private List<String> getNotifications(ClientSocket clientSocket) throws IOException {
        List<String> serverResponse = new ArrayList<>();

        sendMessage(command, clientSocket.outStream());
        sendMessage("GetALL", clientSocket.outStream());

        String response = clientSocket.inStream()
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

        return serverResponse;
    }

    private List<String> initializeServer(ClientSocket clientSocket) throws IOException {
        List<String> serverResponse = new ArrayList<>();

        String response = clientSocket.inStream()
                                      .readLine();

        int roomCount = Integer.parseInt(response);

        for (int i = 0; i < roomCount; i++)
            serverResponse.add(clientSocket.inStream()
                                           .readLine());

        return serverResponse;
    }

    private void selectChatroom(ClientSocket clientSocket) throws IOException {
        sendMessage(command,
                    clientSocket.outStream());

        //Update port to desired chat-room
        port = Integer.parseInt(clientSocket.inStream()
                                            .readLine());
    }

    private void sendMessage(String message, PrintWriter socket_out) {
        if (socket_out == null || socket_out.checkError())
            return;

        socket_out.println(message);
        socket_out.flush();
        log(SENDING);
        Log.d(TAG, "Sent Message: " + message);
    }

    private void log(ConnectionStatus tag){
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
