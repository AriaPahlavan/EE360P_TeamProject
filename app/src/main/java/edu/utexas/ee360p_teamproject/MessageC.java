package edu.utexas.ee360p_teamproject;

import java.util.ArrayList;

/**
 * Created by Bailey on 4/22/2017.
 */

public class MessageC {
    String author;
    String content;
    long timestamp; //System.currentTimeMillis()

    MessageC(String name, String msg, long ts){
        this.author=name;
        this.content=msg;
        this.timestamp=ts;
    }

    public String toTCPString(){
        String sendable= new String("");

        sendable += Long.toString(timestamp);
        sendable +="\n";
        sendable += author;
        sendable +="\n";
        sendable += content;
        //timestamp\nAuthor\nContent
        //timestamp = Long.toString(System.currentTimeMillis())
        return sendable;
    }

}
