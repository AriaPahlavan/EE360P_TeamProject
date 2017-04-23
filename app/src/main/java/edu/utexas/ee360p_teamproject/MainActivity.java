package edu.utexas.ee360p_teamproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import edu.utexas.ee360p_teamproject.ClientRequestHandler.RequestHandler;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testConnectionToServer();
    }

    private void testConnectionToServer() {
        //getting the list of all available chatrooms
        List<String> chatrooms = RequestHandler.listOfAllRooms();

        if (chatrooms == null){
            //server connection failed
            Log.d(TAG, "connections failed :(");
        }
        else if(chatrooms.isEmpty()){
            //no rooms available (this is an error)
            Log.d(TAG, "No rooms received from server :(");
        }else {
            chatrooms.forEach(chatroom -> Log.d(TAG, "chatroom: " + chatroom));
        }

        //choosing a chatroom to enter
        RequestHandler.enterChatroom("Default");

        //sending a new message to current chatroom
        RequestHandler.sendMessage(new MessageC("Aria", "Hello bananas!", System.currentTimeMillis()));

        //getting new notifications
        List<MessageC> notifications = RequestHandler.notifications(0);
        if (notifications == null){
            //server connection failed
            Log.d(TAG, "connections failed :(");
        }
        else if (notifications.isEmpty()){
            //no new notifications
            Log.d(TAG, "no new notification available :(");
        }
        else {
            //you have new notifications
            notifications.forEach(notification -> Log.d(TAG, "notification: " + notification));
        }

    }
}
