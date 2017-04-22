package edu.utexas.ee360p_teamproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.chatroomclient.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ///comment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner roomSelector = (Spinner)findViewById(R.id.chatRoomSelector);
        // TODO - getAllRooms() -- get rooms from middle man and put in Spinner
        //ask coordinator for available rooms in strings and replace rooms below with those strings
        String[] items = new String[]{"Room 1", "Room 2", "Room 3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        roomSelector.setAdapter(adapter);

    }

    public void enterRoom(View view){
        Intent intent = new Intent(this, ChatRoomActivity.class);
        EditText username = (EditText) findViewById(R.id.editUsername);
        String name = username.getText().toString();

        // TODO - setRoom(String)  send chosen chatroom to middle man who will comment client to chatroom

        Bundle extras = new Bundle();
        extras.putString("EXTRA_USERNAME", name);
        intent.putExtras(extras);


        startActivity(intent);
    }


}
