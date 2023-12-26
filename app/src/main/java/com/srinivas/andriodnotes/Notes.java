package com.srinivas.andriodnotes;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Notes implements Comparable<Notes>, Serializable {

    private  String title;
    private  String noteText;
    private  long updatedTime;

    Date date = new Date();



    public String getTitle() {
        return title;
    }

    public String getNoteText() {
        return noteText;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public Notes(String title, String noteText, long updatedTime ){

        this.title = title;
        this.noteText = noteText;
        this.updatedTime = updatedTime;

    }

//    public Notes(String title, String noteText) {
//        this.title = title;
//        this.noteText = noteText;
//
//        this.updatedTime = formatter.format(date);
//
//    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }

    @NonNull
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", noteText='" + noteText + '\'' +
                ", updatedTime=" + updatedTime +
                '}';
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject obj = new JSONObject();

        obj.put("title",title);
        obj.put("noteText",noteText);
        obj.put("updatedTime",updatedTime);
        return obj;

    }

    public static Notes createFromJSON(JSONObject jsonObject) throws JSONException {
        String title = jsonObject.getString("title");
        String noteText = jsonObject.getString("noteText");
        long updatedTime = jsonObject.getLong("updatedTime");
        return new Notes(title, noteText,updatedTime);
    }

    @Override
    public int compareTo(Notes o) {

        return (int) (o.updatedTime - updatedTime);
    }
}
