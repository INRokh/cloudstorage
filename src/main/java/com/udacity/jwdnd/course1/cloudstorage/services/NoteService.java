package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
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

    public boolean addNote(NoteForm noteForm, Integer userId) {
        Note newNote = new Note();
        newNote.setUserId(userId);
        newNote.setNoteTitle(noteForm.getNoteTitle());
        newNote.setNoteDescription(noteForm.getNoteDescription());

        int id = noteMapper.addNote(newNote);
        if (id == 0) {
            logger.error("Could not save note to database.");
            return false;
        }
        return true;
    }

    public boolean updateNote(NoteForm noteForm, Integer userId) {
        Note note = noteMapper.getNoteById(noteForm.getNoteId());
        if (note == null) {
            logger.error("Could not find note id {} for userid  {}.", noteForm.getNoteId(), userId);
            return false;
        }

        note.setNoteTitle(noteForm.getNoteTitle());
        note.setNoteDescription(noteForm.getNoteDescription());

        int updatedRecords = noteMapper.updateNote(note);
        if (updatedRecords != 1) {
            logger.error("Could not update credentials id {} for userid {}: ", note.getNoteId(), userId);
            return false;
        }
        return true;
    }

    public boolean deleteNote(Integer noteId, Integer userId) {
        int deletedRecords = noteMapper.delete(noteId, userId);
        if (deletedRecords != 1) {
            logger.error("Could not update note id {} for userid {}: ", noteId, userId);
            return false;
        }
        return true;
    }

    public List<Note> getNotes(Integer userId) {
        return noteMapper.getUserNotes(userId);
    }

}
