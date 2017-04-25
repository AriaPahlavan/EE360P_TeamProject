package edu.utexas.ee360p_teamproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.utexas.ee360p_teamproject.ClientRequestHandler.RequestHandler;
import edu.utexas.ee360p_teamproject.ClientRequestHandler.TCPConnection;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private TextView t;
    private Spinner roomSelector;

    public static final String EXTRA_MESSAGE = "com.example.chatroomclient.MESSAGE";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        roomSelector = (Spinner)findViewById(R.id.chatRoomSelector);

//        initializeChatroomDropdown();
    }

    private void initializeChatroomDropdown() {
        List<String> chatrooms = RequestHandler.listOfAllRooms();

        if (chatrooms==null)
            return;

        chatrooms.add("Other");
        String[] items = chatrooms.toArray(new String[chatrooms.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                                          android.R
                                                                  .layout
                                                                  .simple_spinner_dropdown_item,
                                                          items);
        roomSelector.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "start - setting port to 8080");
        TCPConnection.port = 8080;
        initializeChatroomDropdown();
    }

    public void enterRoom(View view){
        final Intent intent = new Intent(this, ChatRoomActivity.class);
        EditText username = (EditText) findViewById(R.id.editUsername);
        final String name = username.getText().toString();

        if (name.equals(""))
            return;

        Spinner roomSelector = (Spinner)findViewById(R.id.chatRoomSelector);
        String room = roomSelector.getSelectedItem().toString();

        if(room.equals("Other")){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("New Chat Room");
            alert.setMessage("Enter Chat Room Name");

            // Set an EditText view to get user input
            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {


                    String newRoom = input.getText().toString();
                    RequestHandler.enterChatroom(newRoom);

                    Bundle extras = new Bundle();
                    extras.putString("EXTRA_USERNAME", name);
                    extras.putString("EXTRA_ROOM", newRoom);
                    intent.putExtras(extras);


                    MainActivity.this.startActivity(intent);
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
        }
        else{
            // setRoom(String)  send chosen chatroom to middle man who will comment client to chatroom
            RequestHandler.enterChatroom(room);

            Bundle extras = new Bundle();
            extras.putString("EXTRA_USERNAME", name);
            extras.putString("EXTRA_ROOM", room);
            intent.putExtras(extras);


            startActivity(intent);
        }


    }
}
