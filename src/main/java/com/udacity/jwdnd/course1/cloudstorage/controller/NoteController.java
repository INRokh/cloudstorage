package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NoteController {
    private NoteService noteService;
    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(NoteController.class);

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping("/notes")
    public String getNotesPage(Authentication authentication, NoteForm noteForm, Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);
        if(user == null){
            return "home";
        }
        model.addAttribute("notes", noteService.getNotes(user.getUserId()));
        return "home";
    }

    @PostMapping("/notes")
    public String postOrUpdateNote(Authentication authentication, NoteForm noteForm, Model model) {
        logger.info("noteid: " + noteForm.getNoteId());
        String username = authentication.getName();
        User user = userService.getUser(username);
        if(user == null){
            return "home";
        }
        if (noteForm.getNoteId() == null) {
            noteService.addNote(noteForm, user.getUserId());
        } else {
            noteService.updateNote(noteForm);
        }
        model.addAttribute("notes", noteService.getNotes(user.getUserId()));
        return "home";
    }

    @GetMapping("/notes/{noteId}")
    public String deleteNote(@PathVariable Integer noteId, NoteForm noteForm, Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);
        noteService.deleteNote(noteId);
        model.addAttribute("notes", noteService.getNotes(user.getUserId()));
        return "home";
//        check that belongs to user
    }

}
