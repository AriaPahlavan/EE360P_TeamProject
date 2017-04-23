package edu.utexas.ee360p_teamproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import edu.utexas.ee360p_teamproject.ClientRequestHandler.RequestHandler;


public class ChatRoomActivity extends AppCompatActivity {

    private String myName;
    private int portNumber;
    private int messagesReceived;
    private Thread notificatonHandler;

    private List<String> chats = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        //receive port number and name
        myName = extras.getString("EXTRA_USERNAME");
        messagesReceived = 0;

        /////TODO - check for new messages in bckgrnd (updateChatList(#)) ------ ASYNCH TASK?? 
            //sends messages received to server
            //waits for server to respond for new messages
            //display new messages
        notificatonHandler = new Thread(runnable);

        // listener for send messages
        final Button sendMessage = (Button) findViewById(R.id.sendMessage);
        sendMessage.setOnClickListener(view -> {
            EditText message = (EditText) findViewById(R.id.messageToSend);
            String msgString = message.getText().toString();

            MessageC toSend = new MessageC(myName,
                                           msgString,
                                           System.currentTimeMillis());
            RequestHandler.sendMessage(toSend);

            //maybe TODO - update chatList

        });

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true){
                //call function to get updates
                List<MessageC> notifications = RequestHandler.notifications(messagesReceived);

                //add new messages in a queue
                if((notifications == null) || notifications.isEmpty()){

                }
                else{

                }
            }
        }
    };

}
