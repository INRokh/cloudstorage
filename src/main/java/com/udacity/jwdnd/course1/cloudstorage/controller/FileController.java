package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Controller
public class FileController {
    private FileService fileService;
    private UserService userService;
    private FileMapper fileMapper;

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
        if (fileUpload.isEmpty()) {
            model.addAttribute("isSuccess", false);
            model.addAttribute("error", "File is empty try again.");
            return "result";
        }

        String fileName = fileUpload.getOriginalFilename();

        if (fileMapper.getFileByNameAndUserId(fileName, user.getUserId()) != null) {
            model.addAttribute("isSuccess", false);
            model.addAttribute("error", "File already exist.");
            return "result";
        }

        fileService.addFile(fileUpload, user.getUserId());
        model.addAttribute("isSuccess", true);
        return "result";
    }


    @GetMapping("/files/{fileId}")
    public ResponseEntity<InputStreamResource> downloadFile(
            @PathVariable Integer fileId, Authentication authentication, Model model) {

        File file = fileMapper.getFileById(fileId);
        String fileName = file.getFileName();
        String contentType = file.getContentType();
        byte[] fileData = file.getFileData();

        InputStream inputStream = new ByteArrayInputStream(fileData);
        InputStreamResource resource = new InputStreamResource(inputStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}
