package com.srinivas.andriodnotes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {

    private static  final String TAG ="MainActivity";

    private RecyclerView recyclerView;

    private NotesAdapter notesAdapter;

    private final ArrayList<Notes> noteList = new ArrayList<>();

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Notes currentNote ;
    private  Notes currentNote2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_id);


        notesAdapter = new NotesAdapter(this,noteList);
        recyclerView.setAdapter(notesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            readJson();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG," "+noteList);

        setTitle("Android Notes ["+noteList.size()+"]");

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),this::doAction);


    }

    public void doAction(ActivityResult result) {

        if(result ==null || result.getData()==null){
            return;
        }

        Intent data = result.getData();
        if(result.getResultCode()== RESULT_OK){
            if(data.hasExtra("NEW_NOTE")){
                Notes notes = (Notes) data.getSerializableExtra("NEW_NOTE");
                noteList.add(notes);
                Collections.sort(noteList);
                notesAdapter.notifyItemRangeChanged(0,noteList.size());
            } else if(data.hasExtra("EDIT_NOTE")){
                Notes notes = (Notes) data.getSerializableExtra("EDIT_NOTE");
                currentNote.setTitle(notes.getTitle());
                currentNote.setNoteText(notes.getNoteText());
                currentNote.setUpdatedTime(notes.getUpdatedTime());
                Collections.sort(noteList);
                notesAdapter.notifyItemRangeChanged(0,noteList.size());
            }
        }


        Log.d(TAG,"out from seconds activity");
        try {
            FillJson();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("Android Notes ["+noteList.size()+"]");

    }


    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu){
        getMenuInflater().inflate(R.menu.menu_options,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() ==R.id.menu_about){

            Intent intent = new Intent(this,AboutActivity.class);
            startActivity(intent);
            return true;

        } else if(item.getItemId() ==R.id.menu_addnote){
            Intent intent = new Intent(this,NoteDetailsActivity.class);
//            intent.putExtra("NEW_NOTE","nothing");
            activityResultLauncher.launch(intent);
//            try {
//                FillJson();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            setTitle("Android Notes ["+noteList.size()+"]");
            return true;
        } else{

            Log.d(TAG,"Different menu item selected: "+ item.getTitle());
            return  super.onOptionsItemSelected(item);
        }
//        setTitle("Android Notes ["+noteList.size()+"]");



    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            FillJson();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readJson() throws IOException, JSONException {
        FileInputStream fis = getApplicationContext().openFileInput("JSONText.json");
        StringBuilder fileContent = new StringBuilder();

        byte[] buffer = new byte[1024];
        int n;
        while ((n = fis.read(buffer)) != -1)
        {
            fileContent.append(new String(buffer, 0, n));
        }
        JSONArray jsonArray = new JSONArray(fileContent.toString());
        Log.d(TAG, "readFromFile: ");
//        noteList.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            noteList.add(Notes.createFromJSON(jsonArray.getJSONObject(i)));
        }
        Log.d(TAG, "readFromFile: ");
        setTitle("Android Notes ["+noteList.size()+"]");

    }

    private void FillJson() throws JSONException,IOException {
        JSONArray jsonArray = new JSONArray();

        for (Notes notes : noteList) {
            jsonArray.put(notes.toJSON());
        }
        FileOutputStream fos = getApplicationContext().openFileOutput("JSONText.json", MODE_PRIVATE);
        PrintWriter pr = new PrintWriter(fos);
        pr.println(jsonArray);
        pr.close();
        fos.close();
//        setTitle("Android Notes ["+noteList.size()+"]");

    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this,NoteDetailsActivity.class);

        int position = recyclerView.getChildLayoutPosition(v);
        Log.d(TAG," on click"+noteList);

        currentNote = noteList.get(position);
        intent.putExtra("EDIT_NOTE",currentNote);
        activityResultLauncher.launch(intent);

    }

    @Override
    public boolean onLongClick(View v) {

        int position = recyclerView.getChildLayoutPosition(v);


        currentNote2 = noteList.get(position);


        AlertDialog.Builder builder  = new AlertDialog.Builder(this);

        builder.setPositiveButton("YES",(dialog, which) -> {

            Notes n = noteList.remove(position);
            Log.d(TAG," "+noteList);

            notesAdapter.notifyItemRemoved(position);
            setTitle("Android Notes ["+noteList.size()+"]");

//            try {
//                FillJson();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        });

        builder.setNegativeButton("NO",(dialog, which) -> {
            dialog.dismiss();
        });

        builder.setTitle(" Delete Note '"+ currentNote2.getTitle()+"'?" );

        AlertDialog dialog = builder.create();
        dialog.show();




//        Toast.makeText(this, "Item deleted :"+n.getTitle(), Toast.LENGTH_LONG).show();
        return true;
    }
}