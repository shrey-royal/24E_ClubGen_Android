package com.royal.todo.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.royal.todo.R;
import com.royal.todo.model.Note;

import java.util.List;
import java.util.function.Consumer;

/**
 * Adapter for displaying a list of notes in a RecyclerView
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private final List<Note> noteList;
    public final Consumer<Note> onItemClickListener;

    /**
     * Constructor for initializing the NotesAdapter with a list of notes.
     *
     * @param noteList List of notes to display
     * @param onItemClickListener A lambda expression or method reference for item click handling
     */
    public NotesAdapter(@NonNull List<Note> noteList, @NonNull Consumer<Note> onItemClickListener) {
        this.noteList = noteList;
        this.onItemClickListener = onItemClickListener;
    }

    /**
     *
     * Creates a new ViewHolder for the note item view.
     *
     * @param parent The parent ViewGroup
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder
     */
    @NonNull
    @Override
    public NotesAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);

        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.bind(note);

        holder.itemView.setOnClickListener(v -> onItemClickListener.accept(note));
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, content;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.noteTitle);
            content = itemView.findViewById(R.id.noteContent);
        }

        public void bind(@NonNull Note note) {
            title.setText(note.getTitle());
            content.setText(note.getContent());
        }
    }
}
