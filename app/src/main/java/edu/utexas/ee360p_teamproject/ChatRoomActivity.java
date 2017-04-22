package edu.utexas.ee360p_teamproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChatRoomActivity extends AppCompatActivity {

    String myName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        myName = name;

        //listener for new messages

        // listener for send messages
        final Button sendMessage = (Button) findViewById(R.id.sendMessage);
        sendMessage.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                EditText message = (EditText) findViewById(R.id.messageToSend);
                String msgString = message.getText().toString();
                //send string to server?

            }
        });


    }

}
