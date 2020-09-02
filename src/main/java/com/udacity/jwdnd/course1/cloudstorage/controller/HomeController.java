package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
    private UserService userService;
    private NoteService noteService;
    private FileService fileService;

    public HomeController(NoteService noteService, UserService userService, FileService fileService) {
        this.noteService = noteService;
        this.userService = userService;
        this.fileService = fileService;
    }
    @GetMapping
    public String HomeView(Authentication authentication, NoteForm noteForm, Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);
        if(user == null) {
            model.addAttribute("isSuccess", false);
            model.addAttribute("error", "Note is not found.");
            return "result";

        }
        model.addAttribute("notes", noteService.getNotes(user.getUserId()));
        model.addAttribute("files", fileService.getFiles(user.getUserId()));

        return "home";
    }
}
