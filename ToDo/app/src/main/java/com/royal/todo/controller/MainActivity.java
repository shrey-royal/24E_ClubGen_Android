package com.royal.todo.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.royal.todo.R;
import com.royal.todo.model.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private List<Note> noteList;
    private NoteController noteController;
    private Button addNoteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteController = new NoteController();
        recyclerView = findViewById(R.id.view_recycler);
        addNoteBtn = findViewById(R.id.btn_add_note);
        noteList = new ArrayList<>();
        adapter = new NotesAdapter(noteList, note -> {
            Toast.makeText(this, "Clicked on: " + note.getTitle(), Toast.LENGTH_SHORT).show();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadNotes();

        addNoteBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddNoteActivity.class));
        });
    }

    private void loadNotes() {
        noteController.fetchNote(new NoteController.NotesCallBack() {
            @Override
            public void onSuccess(List<Note> notes) {
                noteList.clear();
                noteList.addAll(notes);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(MainActivity.this, "Error fetching notes", Toast.LENGTH_SHORT).show();
            }
        });
    }
}