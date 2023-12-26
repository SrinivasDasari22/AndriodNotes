package com.srinivas.andriodnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteDetailsActivity extends AppCompatActivity {

    private EditText note_title2;
    private EditText notes2;
    private String updated_time;
    private String temp_title,temp_note;
    private String typeOfSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        note_title2 = findViewById(R.id.noteTitle2);
//        temp_title = note_title2.getText().toString();
        notes2 = findViewById(R.id.notes2);

//        temp_note = notes2.getText().toString();

        if(getIntent().hasExtra("EDIT_NOTE")){
            Notes notes = (Notes) getIntent().getSerializableExtra("EDIT_NOTE");
            note_title2.setText(notes.getTitle());
            notes2.setText(notes.getNoteText());
            temp_title = note_title2.getText().toString();
            temp_note = notes2.getText().toString();

        }


    }

    public boolean onCreateOptionsMenu(@NonNull Menu menu){
        getMenuInflater().inflate(R.menu.save_option,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){


        if(item.getItemId()==R.id.saveButton) {

            if (note_title2.getText().toString().isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setPositiveButton("Ok", (dialog, which) -> {

                    super.onBackPressed();
                });

                builder.setNegativeButton("CANCEL", (dialog, which) -> {

                    dialog.dismiss();

                });

                builder.setTitle("The user note will not be saved without a title. ");

                AlertDialog dialog = builder.create();
                dialog.show();
            } else
                if(!note_title2.getText().toString().isEmpty()){

                Intent intent = new Intent();
                if (getIntent().hasExtra("EDIT_NOTE")) {
                    typeOfSave = "EDIT_NOTE";
                } else {
                    typeOfSave = "NEW_NOTE";
                }

                Notes notes = saveData();
                if (notes != null) {


                    if (getIntent().hasExtra("EDIT_NOTE")) {

                        intent.putExtra("EDIT_NOTE", notes);
                        typeOfSave = "EDIT_NOTE";
                    } else {
                        intent.putExtra("NEW_NOTE", notes);
                        typeOfSave = "NEW_NOTE";
                    }
                    setResult(RESULT_OK, intent);

                }
                finish();
            }
        }
        return  super.onOptionsItemSelected(item);
    }

    public Notes saveData(){
        if(note_title2.getText().toString().isEmpty()){

            return  null;
        }
        if(notes2.getText().toString().isEmpty()){
            return  null;
        }
        if (typeOfSave == "EDIT_NOTE"){
            if((!temp_title.equals(note_title2.getText().toString())) || (!temp_note.equals(notes2.getText().toString()))){

                Notes notes = new Notes(note_title2.getText().toString(),notes2.getText().toString(),System.currentTimeMillis());
                return notes;
            }
            return null;

        } else if (typeOfSave == "NEW_NOTE"){
            Notes notes = new Notes(note_title2.getText().toString(),notes2.getText().toString(),System.currentTimeMillis());
            return notes;
        }
        return null;
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder  = new AlertDialog.Builder(this);

        builder.setPositiveButton("YES",(dialog, which) -> {


            Intent intent = new Intent();
            if (getIntent().hasExtra("EDIT_NOTE")) {
                typeOfSave = "EDIT_NOTE";
            } else {
                typeOfSave = "NEW_NOTE";
            }
            Notes notes = saveData();
            if(notes !=null) {


                if (getIntent().hasExtra("EDIT_NOTE")) {

                    intent.putExtra("EDIT_NOTE", notes);
                } else {
                    intent.putExtra("NEW_NOTE", notes);
                }
                setResult(RESULT_OK, intent);

            }
            super.onBackPressed();
        });

        builder.setNegativeButton("NO",(dialog, which) -> {        super.onBackPressed();

        });

        builder.setTitle(" Your note is not saved!\n Save note '"+ note_title2.getText()+"'?" );

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}