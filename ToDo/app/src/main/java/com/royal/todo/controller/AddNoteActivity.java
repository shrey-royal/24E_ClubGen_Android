package com.royal.todo.controller;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.royal.todo.R;
import com.royal.todo.model.Note;

public class AddNoteActivity extends AppCompatActivity {
    private EditText titleInput, contentInput;
    private Button saveNoteBtn;
    private NoteController noteController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        noteController = new NoteController();
        titleInput = findViewById(R.id.edt_inputTitle);
        contentInput = findViewById(R.id.edt_inputContent);
        saveNoteBtn = findViewById(R.id.btn_save_note);

        saveNoteBtn.setOnClickListener(v -> saveNote());
    }

    private void saveNote() {
        String title = titleInput.getText().toString().trim();
        String content = contentInput.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        noteController.addNote(title, content, new NoteController.NoteCallBack() {
            @Override
            public void onSuccess(Note note) {
                //ISSUE
                Toast.makeText(AddNoteActivity.this, "Note added!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AddNoteActivity.this, "Error saving note", Toast.LENGTH_SHORT).show();
            }
        });

    }
}