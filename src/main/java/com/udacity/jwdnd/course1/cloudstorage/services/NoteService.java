package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private Logger logger = LoggerFactory.getLogger(HashService.class);
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public void addNote(NoteForm noteForm, Integer userId) {
        Note newNote = new Note();
        newNote.setUserId(userId);
        newNote.setNoteTitle(noteForm.getNoteTitle());
        newNote.setNoteDescription(noteForm.getNoteDescription());
        noteMapper.addNote(newNote);
    }

    public void updateNote(NoteForm noteForm) {
        Note note = noteMapper.getNoteById(noteForm.getNoteId());
        if (note == null) {
            logger.error("not found");
            return;
        }
        note.setNoteTitle(noteForm.getNoteTitle());
        note.setNoteDescription(noteForm.getNoteDescription());
        noteMapper.updateNote(note);
    }

    public void deleteNote(Integer noteId) {
        noteMapper.delete(noteId);
    }

    public List<Note> getNotes(Integer userId) {
        return noteMapper.getUserNotes(userId);
    }

}
