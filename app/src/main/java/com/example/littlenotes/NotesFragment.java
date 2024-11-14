package com.example.littlenotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.littlenotes.db.NoteDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotesFragment extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddNote;
    private NoteAdapter noteAdapter;
    private NoteDatabase noteDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        fabAddNote = view.findViewById(R.id.fabAddNote);

        noteDatabase = Room.databaseBuilder(getActivity(), NoteDatabase.class, "note_db").allowMainThreadQueries().build();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        noteAdapter = new NoteAdapter(noteDatabase.noteDao().getNotes());
        recyclerView.setAdapter(noteAdapter);

        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddNoteDialog();
            }
        });

        return view;
    }

    private void openAddNoteDialog() {
        //TODO: Add a dialog for entering a new note
    }
}
