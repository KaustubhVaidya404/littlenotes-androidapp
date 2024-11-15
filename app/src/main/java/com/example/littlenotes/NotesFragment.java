package com.example.littlenotes;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.littlenotes.db.NoteDatabase;
import com.example.littlenotes.entity.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NotesFragment extends Fragment implements NoteAdapter.OnNoteLongClickListener{
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddNote;
    private NoteAdapter noteAdapter;
    private NoteDatabase noteDatabase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        fabAddNote = view.findViewById(R.id.fabAddNote);

        noteDatabase = Room.databaseBuilder(getActivity(), NoteDatabase.class, "note_db").allowMainThreadQueries().build();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        noteAdapter = new NoteAdapter((NoteAdapter.OnNoteLongClickListener) this, noteDatabase);


        List<Note> notes = noteDatabase.noteDao().getNotes();
        noteAdapter.setNotes(notes);
        recyclerView.setAdapter(noteAdapter);
        fabAddNote.setOnClickListener(v -> openAddNoteDialog());

        return view;
    }

    @Override
    public void onNoteLongClick(int position) {
        Note note = noteAdapter.getNotes().get(position);
        openEditNoteDialog(note);
    }

    private void openEditNoteDialog(Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_note, null);
        builder.setView(dialogView);

        EditText etNoteTitle = dialogView.findViewById(R.id.etNoteTitle);
        EditText etNoteContent = dialogView.findViewById(R.id.etNoteContent);
        Button btnSaveNote = dialogView.findViewById(R.id.btnSaveNote);
        Button btnCancel = dialogView.findViewById(R.id.buttonCancel);
        ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);

        etNoteTitle.setText(note.getNoteTitle());
        etNoteContent.setText(note.getNoteContent());

        AlertDialog dialog = builder.create();
        dialog.show();

        btnSaveNote.setOnClickListener(v -> {
            String updatedTitle = etNoteTitle.getText().toString().trim();
            String updatedContent = etNoteContent.getText().toString().trim();

            if (!updatedContent.isEmpty()) {
                progressBar.setVisibility(View.VISIBLE);

                new Thread(() -> {
                    note.setNoteTitle(updatedTitle);
                    note.setNoteContent(updatedContent);
                    noteDatabase.noteDao().update(note);

                    getActivity().runOnUiThread(() -> {
                        noteAdapter.setNotes(noteDatabase.noteDao().getNotes());
                        progressBar.setVisibility(View.GONE);
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Note updated", Toast.LENGTH_SHORT).show();
                    });
                }).start();
            } else {
                Toast.makeText(getActivity(), "Please enter content", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
    }

    private void openAddNoteDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_note, null);
        builder.setView(dialogView);


        EditText etNoteTitle = dialogView.findViewById(R.id.etNoteTitle);
        EditText etNoteContent = dialogView.findViewById(R.id.etNoteContent);
        Button btnSaveNote = dialogView.findViewById(R.id.btnSaveNote);
        Button btnCancel = dialogView.findViewById(R.id.buttonCancel);
        ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);


        AlertDialog dialog = builder.create();
        dialog.show();


        btnSaveNote.setOnClickListener(v -> {
            String noteTitle = etNoteTitle.getText().toString().trim();
            String noteContent = etNoteContent.getText().toString().trim();

            if (!noteContent.isEmpty()) {

                progressBar.setVisibility(View.VISIBLE);

                new Thread(() -> {
                    Note newNote = new Note();
                    newNote.setNoteTitle(noteTitle);
                    newNote.setNoteContent(noteContent);
                    noteDatabase.noteDao().insert(newNote);


                    getActivity().runOnUiThread(() -> {
                        noteAdapter.setNotes(noteDatabase.noteDao().getNotes());
                        progressBar.setVisibility(View.GONE);
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Note saved", Toast.LENGTH_SHORT).show();
                    });
                }).start();
            } else {
                Toast.makeText(getActivity(), "Please enter content", Toast.LENGTH_SHORT).show();
            }
        });


        btnCancel.setOnClickListener(v -> dialog.dismiss());
    }

}
