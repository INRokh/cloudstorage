package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.HashService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;

@Controller
public class FileController {
    private FileService fileService;
    private UserService userService;
    private FileMapper fileMapper;
    private Logger logger = LoggerFactory.getLogger(FileController.class);

    public FileController(FileService fileService, UserService userService, FileMapper fileMapper) {
        this.fileService = fileService;
        this.userService = userService;
        this.fileMapper = fileMapper;
    }

    @PostMapping("/files")
    public String addFile(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);
        if(user == null) {
            model.addAttribute("isSuccess", false);
            model.addAttribute("error", "User is not found. Try again.");
            return "result";
        }
        logger.info("size " + fileUpload.getSize(),toString());
        fileService.addFile(fileUpload, user.getUserId());
        model.addAttribute("isSuccess", true);
        return "result";
    }

//    @GetMapping("/files/#{fileId}")
//    public String showFile(@PathVariable Integer noteId, Authentication authentication, FileForm fileForm, Model model) {
//
//    }
}
