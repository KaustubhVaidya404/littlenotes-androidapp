package com.example.littlenotes;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.littlenotes.db.NoteDatabase;
import com.example.littlenotes.entity.Note;

import java.util.List;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> notes;
    private OnNoteLongClickListener longClickListener;

    private NoteDatabase noteDatabase;


    public NoteAdapter(OnNoteLongClickListener longClickListener, NoteDatabase noteDatabase) {
        this.notes = noteDatabase.noteDao().getNotes();
        this.longClickListener = longClickListener;
        this.noteDatabase = noteDatabase;
    }

    public interface OnNoteLongClickListener {
        void onNoteLongClick(int position);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.noteTitle.setText(note.getNoteTitle());
        holder.noteContent.setText(note.getNoteContent());

        // Set up long click listener
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onNoteLongClick(position);
            }
            return true;
        });

        holder.deleteButton.setOnClickListener(v -> {
            deleteNote(position);
            Toast.makeText(v.getContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
        });
    }


    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();
    }

    public List<Note> getNotes() {
        return notes;
    }

    private void deleteNote(int position) {
        Note note = notes.get(position);
        new Thread(() -> {
            noteDatabase.noteDao().delete(note);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                notes.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeRemoved(position, notes.size());
            });
        }).start();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView noteTitle, noteContent;
        private ImageButton deleteButton;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteContent = itemView.findViewById(R.id.noteContent);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

    }
}

