package com.example.jszx.itimeapplication;

import android.content.Context;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by jszx on 2019/11/20.
 */

public class EventSaver {
    public ArrayList<Event> getEvents() {
        return events;
    }

    Context context;
    ArrayList<Event>events=new ArrayList<Event>();

    public EventSaver(Context context) {
        this.context = context;
    }

    public void saver(){
        try{
            //序列化
            ObjectOutputStream outputStream = new ObjectOutputStream(context.openFileOutput("Serializable.txt",Context.MODE_PRIVATE));
            outputStream.writeObject(events);
            outputStream.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<Event>load(){
//反序列化
        try {
            ObjectInputStream inputStream = new ObjectInputStream(context.openFileInput("Serializable.txt"));
            events = (ArrayList<Event>) inputStream.readObject();
            inputStream.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return events;
    }
}
