package edu.utexas.ee360p_teamproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import edu.utexas.ee360p_teamproject.ClientRequestHandler.RequestHandler;


public class ChatRoomActivity extends AppCompatActivity {

    private String myName;
    private int portNumber;
    private int messagesReceived;
    private Thread notificatonHandler;

    private List<String> chats = new ArrayList<>();
    private Context context;


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

        notificatonHandler = new Thread(runnable);

        context = getApplicationContext();

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

                    ListView chatList = (ListView) findViewById(R.id.chatList);
                    for(int i=0; i<notifications.size(); i++){
                        String totalChat = notifications.get(i).timestamp + " " + notifications.get(i).author+": " + notifications.get(i).content;
                        chats.add(totalChat);
                        messagesReceived++;
                    }

                    String[] values = chats.toArray(new String[chats.size()]);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, values);

                    chatList.setAdapter(adapter);

                    
                }
            }
        }
    };

}
