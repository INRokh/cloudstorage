package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NoteController {
    private NoteService noteService;
    private UserService userService;
    private NoteMapper noteMapper;

    public NoteController(NoteService noteService, UserService userService, NoteMapper noteMapper) {
        this.noteService = noteService;
        this.userService = userService;
        this.noteMapper = noteMapper;
    }

    @PostMapping("/notes")
    public String postOrUpdateNote(Authentication authentication, NoteForm noteForm, Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);
        if(user == null) {
            model.addAttribute("isSuccess", false);
            model.addAttribute("error", "User is not found. Try again.");
            return "result";
        }

        if (noteForm.getNoteId() == null) {
            noteService.addNote(noteForm, user.getUserId());
        } else {
            noteService.updateNote(noteForm);
        }
        model.addAttribute("isSuccess", true);
        return "result";
    }

    @GetMapping("/notes/{noteId}")
    public String deleteNote(@PathVariable Integer noteId, NoteForm noteForm, Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);
        if(user == null) {
            model.addAttribute("isSuccess", false);
            model.addAttribute("error", "User is not found.");
            return "result";
        }
        Note noteById = noteMapper.getNoteById(noteForm.getNoteId());
        if(noteById == null) {
            model.addAttribute("isSuccess", false);
            model.addAttribute("error", "Note is not found.");
            return "result";
        }
        if(user.getUserId() != noteById.getUserId()) {
            model.addAttribute("isSuccess", false);
            model.addAttribute("error", "Error when deleting note.");
            return "result";
        }
        noteService.deleteNote(noteId);
        model.addAttribute("isSuccess", true);
        return "result";
    }

}
