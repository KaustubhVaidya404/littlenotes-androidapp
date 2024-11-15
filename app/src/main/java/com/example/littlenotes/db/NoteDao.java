package com.example.littlenotes.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.littlenotes.entity.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM notes")
    List<Note> getNotes();

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

}
