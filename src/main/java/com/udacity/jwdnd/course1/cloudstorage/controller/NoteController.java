package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NoteController {
    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("/notes")
    public String postOrUpdateNote(Authentication authentication, NoteForm noteForm, Model model)
            throws  AccessDeniedException {
        String username = authentication.getName();
        User user = userService.getUser(username);
        if(user == null) {
            throw new AccessDeniedException("User not found: " + username);
        }

        boolean isSuccess = noteForm.getNoteId() == null ?
                noteService.addNote(noteForm, user.getUserId()) :
                noteService.updateNote(noteForm, user.getUserId());
        model.addAttribute("isSuccess", true);
        return "result";
    }

    @GetMapping("/notes/{noteId}")
    public String deleteNote(@PathVariable Integer noteId, Authentication authentication, Model model)
            throws  AccessDeniedException {
        String username = authentication.getName();
        User user = userService.getUser(username);
        if(user == null) {
            throw new AccessDeniedException("User not found: " + username);
        }

        boolean isSuccess = noteService.deleteNote(noteId, user.getUserId());
        model.addAttribute("isSuccess", isSuccess);
        return "result";
    }

}
