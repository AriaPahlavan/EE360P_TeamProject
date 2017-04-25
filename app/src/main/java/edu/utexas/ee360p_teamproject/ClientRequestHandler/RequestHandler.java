package edu.utexas.ee360p_teamproject.ClientRequestHandler;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.utexas.ee360p_teamproject.MessageC;

/**
 * Created by aria on 4/22/17.
 */

public class RequestHandler {
    private final static String TAG = "RequestHandler";

    /**
     * initializes connection to sever and gets a list of all available chatrooms
     *
     *
     * @return a list of all available chatrooms, or null
     *          if failed to connect with server
     */
    public static List<String> listOfAllRooms() {
        try {
            return new ClientTask(ClientTask.INIT, null).execute()
                                                        .get()
                                                        .responseIfAvailable();
        }
        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * selects a chatroom based on the NAME
     *
     *
     * @param name name of the chatroom to enter
     */
    public static void enterChatroom(String name) {
        try {
            new ClientTask(ClientTask.ROOM, name).execute()
                                                 .get()
                                                 .responseIfAvailable();
        }
        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * sends a new message to the server under the current chatroom
     *
     * @param message the new message to be sent
     */
    public static void sendMessage(MessageC message) {
        try {
            new ClientTask(ClientTask.SEND, message.toString())
                    .execute()
                    .get()
                    .responseIfAvailable();
        }
        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * checks with server for any notifications
     *
     * @param count current count of messages recoded in the chatroom
     *
     *
     * @return a list of new messages from the server,
     *         or null failed to connect with server.
     *
     *         NOTE: if there are no updates, the list of messages returned
     *         will be empty
     */
    public static List<MessageC> notifications(int count) {
        try {
            List<MessageC> newMessages = new ArrayList<>();
            List<String> messagesStr =
                    new ClientTask(ClientTask.UPDATE, String.valueOf(count))
                            .execute()
                            .get()
                            .responseIfAvailable();

            if (messagesStr == null || messagesStr.isEmpty())
                return new ArrayList<>();

            int msgCount = Integer.parseInt(messagesStr.remove(0));

            for (int i = 0; i < msgCount; i++) {
                if (messagesStr.isEmpty()) return new ArrayList<>(); //error
                String timeStamp = messagesStr.remove(0);

                if (messagesStr.isEmpty()) return new ArrayList<>(); //error
                String author = messagesStr.remove(0);

                if (messagesStr.isEmpty()) return new ArrayList<>(); //error
                String msg = messagesStr.remove(0);

                MessageC curMessage = new MessageC(author,
                                                   msg,
                                                   Long.parseLong(timeStamp));
                newMessages.add(curMessage);
            }

            return newMessages;
        }
        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void pullNotification(int count, MessageCallback listener) {
        Log.d(TAG, "pulling notifications");
        TCPConnection connection = new TCPConnection(ClientTask.UPDATE,
                                                     String.valueOf(count));
        connection.run();

        List<String> messagesStr = connection.responseIfAvailable();


        if (messagesStr == null || messagesStr.isEmpty()) {
            Log.d(TAG, "no notification, returning");
            return;
        }

        int msgCount = Integer.parseInt(messagesStr.remove(0));

        for (int i = 0; i < msgCount; i++) {

            if (messagesStr.isEmpty()) return; //error
            String timeStamp = messagesStr.remove(0);

            if (messagesStr.isEmpty()) return; //error
            String author = messagesStr.remove(0);

            if (messagesStr.isEmpty()) return; //error
            String msg = messagesStr.remove(0);

            MessageC curMessage = new MessageC(author,
                                               msg,
                                               Long.parseLong(timeStamp));


            Log.d(TAG, "Got notification: " + curMessage.toString());

            listener.callBackMessageReceiver(curMessage);
        }
    }

    public interface MessageCallback {
        void callBackMessageReceiver(MessageC notification);
    }

}