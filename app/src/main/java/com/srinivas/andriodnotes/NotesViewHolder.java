package com.srinivas.andriodnotes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesViewHolder extends RecyclerView.ViewHolder {


    TextView note_text;
    TextView date_text;
    TextView note_title;


    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);

        note_title = itemView.findViewById(R.id.note_title);
        date_text = itemView.findViewById(R.id.updated_time);
        note_text = itemView.findViewById(R.id.note_text);
}

}
