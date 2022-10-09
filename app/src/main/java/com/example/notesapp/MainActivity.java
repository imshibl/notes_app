package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    //to add our notes
    static List<String> notes;

    static ArrayAdapter adapter;

    SharedPreferences sharedPreferences;

    //to display our notes
    ListView notesListView;

    TextView noNotesTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = MainActivity.this.getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE);

        notesListView = findViewById(R.id.notes_listView);
        noNotesTV = findViewById(R.id.empytTV);
        notes = new ArrayList<>();

        try{
            HashSet<String> noteSet = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

            if (noteSet.isEmpty()) {
                noNotesTV.setVisibility(View.VISIBLE);


            } else {
                noNotesTV.setVisibility(View.GONE);
                notes = new ArrayList<>(noteSet);
            }

        }catch (Exception e){

        }




        adapter = new ArrayAdapter(getApplicationContext(), R.layout.notes_row, R.id.notesTV, notes);
        notesListView.setAdapter(adapter);

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                intent.putExtra("noteId", position);
                startActivity(intent);
                finish();

            }
        });

        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                int itemToDelete = position;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(itemToDelete);
                                adapter.notifyDataSetChanged();

                                HashSet<String> noteSet = new HashSet<>(notes);
                                sharedPreferences.edit().putStringSet("notes", noteSet).apply();

                                if (noteSet.isEmpty() || noteSet == null) {
                                    noNotesTV.setVisibility(View.VISIBLE);


                                }

                            }
                        }).setNegativeButton("No", null)
                        .show();
                return true;
            }
        });


    }

    //create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //menu click handler


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.add_note) {
            Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
            startActivity(intent);
            finish();

            return true;
        }
        return false;
    }
}