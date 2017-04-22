package edu.utexas.ee360p_teamproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.chatroomclient.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ///comment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void enterRoom(View view){
        Intent intent = new Intent(this, ChatRoomActivity.class);
        EditText username = (EditText) findViewById(R.id.editUsername);
        String name = username.getText().toString();

        intent.putExtra(EXTRA_MESSAGE,name);
        startActivity(intent);
    }


}
