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
    private boolean isReady;
    private List<String> resposne;

    enum ConnectionStatus{
        CONNECTING,
        ERROR,
        DONE,
        SENDING,
        SENT
    }

    TCPConnection(String tag, String command) {
        this.tag = tag;
        this.command  = command;
        this.isReady = false;
    }

    public List<String> responseIfAvailable(){
        while (!isReady) {}

        return resposne;
    }

    List<String> run() {
        log(CONNECTING);

        List<String> serverResponse = new ArrayList<>();
        ClientSocket clientSocket;

        try {
            log(SENDING);

            clientSocket = new ClientSocket(ip, port);
            switch (tag) {
                case ClientTask.INIT:
                    Log.d(TAG, "Initializing...");
                    serverResponse = initializeServer(clientSocket);
                    break;
                case ClientTask.ROOM:

                    selectChatroom(clientSocket);
                    log(SENT);
                    break;
                case ClientTask.SEND:
                    sendMessage(command,
                                clientSocket.outStream());
                    log(SENT);
                    break;
                case ClientTask.UPDATE:
                    serverResponse = getNotifications(clientSocket);
                    break;
            }
            log(DONE);
        }
        catch (Exception e) {
            log(ERROR);
            e.printStackTrace();

        } finally {
            Log.d(TAG, "Socket Closed");
        }

        resposne = serverResponse;
        isReady = true;
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

        Log.d(TAG, "Getting count of avail rooms...");
        String response = clientSocket.inStream()
                                      .readLine();

        int roomCount = Integer.parseInt(response);

        Log.d(TAG, "there are " + roomCount + " rooms available");

        for (int i = 0; i < roomCount; i++) {
            Log.d(TAG, "Getting name of room #" + (i+1));

            serverResponse.add(clientSocket.inStream()
                                           .readLine());
        }

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
            case DONE:       Log.d(TAG, "Done...");       break;
            case ERROR:      Log.d(TAG, "Error...");      break;
            default:         Log.d(TAG, "Default value... ERROR");
        }
    }
}
