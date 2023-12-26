package com.srinivas.andriodnotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    private MainActivity mainActivity;
    private ArrayList<Notes> notesArrayList;
    private final SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd, hh:mm aa", Locale.getDefault());

    public NotesAdapter(MainActivity mainActivity, ArrayList<Notes> notesArrayList) {
        this.mainActivity = mainActivity;
        this.notesArrayList = notesArrayList;
    }




    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_entry,parent,false);

        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);

        return new NotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {

        Notes notes = notesArrayList.get(position);
        if(notes.getTitle().length()<80) {
            holder.note_title.setText(notes.getTitle());
        } else {
            holder.note_title.setText((notes.getTitle().substring(0,80)) +"...");

        }
//        holder.note_title.setText(notes.getTitle());
        if(notes.getNoteText().length()<80) {
            holder.note_text.setText(notes.getNoteText());
        } else {
            holder.note_text.setText((notes.getNoteText().substring(0,80)) +"...");

        }
        holder.date_text.setText(formatter.format(new Date(notes.getUpdatedTime())));


    }

    @Override
    public int getItemCount() {
        return notesArrayList.size();
    }
}
