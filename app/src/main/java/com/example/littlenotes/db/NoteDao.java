package com.example.littlenotes.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.littlenotes.entity.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM notes")
    List<Note> getNotes();

    @Insert
    void insert(Note note);
}
