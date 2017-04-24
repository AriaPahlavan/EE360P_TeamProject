package edu.utexas.ee360p_teamproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.utexas.ee360p_teamproject.ClientRequestHandler.RequestHandler;

import static java.lang.Thread.sleep;


public class ChatRoomActivity extends AppCompatActivity {
    private final static String TAG = "ChatroomActivity";
    private String myName;
    private int messagesReceived;
    private Thread notificatonHandler;
    private AsyncTask<Integer, List<MessageC>, String> notificationTask;

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
        String thisRoom = extras.getString("EXTRA_ROOM");
        messagesReceived = 0;
        context = getApplicationContext();

        TextView chatRoom = (TextView) findViewById(R.id.chatRoomTitle);
        EditText message = (EditText) findViewById(R.id.messageToSend);

        chatRoom.setText(thisRoom);

        //get the messages that are already in the room
        displayNewNotifications();


        final Button sendMessage = (Button) findViewById(R.id.sendMessage);

//        notificationTask = new NotificationTask(this::displayAsConsumer).execute();

        sendMessage.setOnClickListener(view -> sendIfValid(message));

        message.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendIfValid(message);
                handled = true;
            }
            return handled;
        });

        findViewById(R.id.Refresh).setOnClickListener(v -> displayNewNotifications());
    }

    private void sendIfValid(EditText message) {
        String msgString = message.getText().toString();
        if (msgString.contains("\n") || msgString.equals(""))
            return;

        displayNewMessage(msgString);
        message.setText("");
    }

    private void displayNewMessage(String msgString) {
        MessageC toSend = new MessageC(myName,
                                       msgString,
                                       System.currentTimeMillis());
        RequestHandler.sendMessage(toSend);
        displayNewNotifications();
    }

    private void displayNewNotifications() {
        //call function to get updates
        List<MessageC> notifications = RequestHandler.notifications(messagesReceived);

        //add new messages in a queue
        if ((notifications == null) || notifications.isEmpty())
            return;

        ListView chatList = (ListView) findViewById(R.id.chatList);
        for (int i = 0; i < notifications.size(); i++) {
            long milliseconds = notifications.get(i).timestamp;
            int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
            int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);


            String totalChat = hours + ":" + minutes + " " + notifications.get(i).author + ": " + notifications.get(i).content;
            chats.add(totalChat);
            messagesReceived++;
        }

        String[] values = chats.toArray(new String[chats.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, android.R.id.text1, values);

        chatList.setAdapter(adapter);
    }

    private void displayAsConsumer(List<MessageC> notifications){
        Log.d(TAG, "in consumer, received notification");

        //add new messages in a queue
        if ((notifications == null) || notifications.isEmpty())
            return;

        ListView chatList = (ListView) findViewById(R.id.chatList);
        for (int i = 0; i < notifications.size(); i++) {
            long milliseconds = notifications.get(i).timestamp;
            int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
            int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);


            String totalChat = hours + ":" + minutes + " " + notifications.get(i).author + ": " + notifications.get(i).content;
            chats.add(totalChat);
            messagesReceived++;
        }

        String[] values = chats.toArray(new String[chats.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, android.R.id.text1, values);

        chatList.setAdapter(adapter);
    }

    public synchronized int currentMessageCount(){
        return messagesReceived;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        notificationTask.cancel(true);
    }


    private class NotificationTask extends AsyncTask<Integer, List<MessageC>, String> {
        private final String TAG = "NotificationTask";
        MyConsumer<List<MessageC>> notifucationDisplayer;

        public NotificationTask
                (MyConsumer<List<MessageC>> notifucationDisplayer)
        {
            this.notifucationDisplayer = notifucationDisplayer;
        }

        @Override
        protected String doInBackground(Integer... params) {
            Log.d(TAG, "getting notifications..");

            while (true){
                List<MessageC> notifications =
                        RequestHandler.notifications(currentMessageCount());

                if (notifications==null)
                    continue;

                Log.d(TAG, "got a notification!");
                publishProgress(notifications);

                try {
                    Thread.sleep(5000);
                }
                catch (InterruptedException e) {
                    Log.d(TAG, "thread interrupted!!");
                }

                Log.d(TAG, "woke up to get more notifications");
            }
//            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected final void onProgressUpdate(List<MessageC>... values) {
            super.onProgressUpdate(values);

            Log.d(TAG, "progress update with new notification");

            this.notifucationDisplayer
                    .accept(values[0]);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
