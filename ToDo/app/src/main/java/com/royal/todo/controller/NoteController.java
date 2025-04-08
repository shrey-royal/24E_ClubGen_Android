package com.royal.todo.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.royal.todo.model.Note;

import java.util.List;

public class NoteController {
    private FirebaseFirestore db;
    private static final String COLLECTION_NAME = "notes";

    public NoteController() {
        db = FirebaseFirestore.getInstance();
    }

    public void fetchNote(NotesCallBack notesCallBack) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            notesCallBack.onFailure(new Exception("User not authenticated"));
            return;
        }
        String uid = user.getUid();

        db.collection(COLLECTION_NAME).document(uid)
                .collection("userNotes")
                .addSnapshotListener((value, error) -> {
            if (error != null) {
                notesCallBack.onFailure(error);
                return;
            }

            assert value != null;
            List<Note> notes = value.toObjects(Note.class);
            notesCallBack.onSuccess(notes);
        });
    }

    public void addNote(String title, String content, NoteCallBack callBack) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            callBack.onFailure(new Exception("User not authenticated"));
            return;
        }
        String uid = user.getUid();
        String noteId = db.collection(COLLECTION_NAME).document(uid)
                .collection("userNotes").document().getId();
        Note note = new Note(noteId, title, content);

        db.collection(COLLECTION_NAME).document(uid)
                .collection("userNotes").document(noteId)
                .set(note)
                .addOnSuccessListener(aVoid -> callBack.onSuccess(note))
                .addOnFailureListener(callBack::onFailure);
    }

    public void updateNote(String noteId, String newTitle, String newContent, NoteCallBack callBack) {
        db.collection(COLLECTION_NAME).document(noteId)
                .update("title", newTitle, "content", newContent)
                .addOnSuccessListener(aVoid ->
                    callBack.onSuccess(new Note(noteId, newTitle, newContent))
                )
                .addOnFailureListener(callBack::onFailure);
    }

    public void deleteNote(String noteId, DeleteCallBack callBack) {
        db.collection(COLLECTION_NAME).document(noteId)
                .delete()
                .addOnSuccessListener(aVoid -> callBack.onSuccess())
                .addOnFailureListener(callBack::onFailure);
    }

    // Callback interfaces for Firebase operations
    public interface NotesCallBack {
        void onSuccess(List<Note> notes);
        void onFailure(Exception e);
    }

    public interface NoteCallBack {
        void onSuccess(Note note);
        void onFailure(Exception e);
    }

    public interface DeleteCallBack {
        void onSuccess();
        void onFailure(Exception e);
    }

}