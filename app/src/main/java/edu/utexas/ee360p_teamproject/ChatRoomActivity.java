package edu.utexas.ee360p_teamproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChatRoomActivity extends AppCompatActivity {

    String myName;
    int portNumber;
    int messagesReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        //receive port number and name
        String name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        myName = name;
        //save portnumber
        messagesReceived = 0;

        //listener or while loop for new messages
            //sends messages received to server
            //waits for server to respond for new messages
            //display new messages

        // listener for send messages
        final Button sendMessage = (Button) findViewById(R.id.sendMessage);
        sendMessage.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                EditText message = (EditText) findViewById(R.id.messageToSend);
                String msgString = message.getText().toString();

                MessageC toSend = new MessageC(myName, msgString, System.currentTimeMillis());
                String msg = toSend.toTCPString();

                //send msg to server

            }
        });


    }

}
