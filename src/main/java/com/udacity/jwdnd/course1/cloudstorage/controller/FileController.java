package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Controller
public class FileController {
    private Logger logger = LoggerFactory.getLogger(EncryptionService.class);
    private FileService fileService;
    private UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/files")
    public String addFile(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication, Model model)
            throws AccessDeniedException {
        String username = authentication.getName();
        User user = userService.getUser(username);
        if(user == null) {
            throw new AccessDeniedException("User not found: " + username);
        }

        if (fileUpload.isEmpty()) {
            logger.error("Nothing to upload.");
            model.addAttribute("isSuccess", false);
            return "result";
        }

        String fileName = fileUpload.getOriginalFilename();
        if (fileService.getFileByNameAndUserId(fileName, user.getUserId()) != null) {
            logger.error("File already exist.");
            model.addAttribute("isSuccess", false);
            return "result";
        }

        model.addAttribute("isSuccess",  fileService.addFile(fileUpload, user.getUserId()));
        return "result";
    }

    @GetMapping("/files/{fileId}")
    public ResponseEntity<InputStreamResource> downloadFile(
            @PathVariable Integer fileId, Authentication authentication, Model model)
            throws  AccessDeniedException, FileNotFoundException {
        String username = authentication.getName();
        User user = userService.getUser(username);
        if(user == null) {
            throw new AccessDeniedException("User not found: " + username);
        }

        File file = fileService.getFileByIdAndUserId(fileId, user.getUserId());
        if(file == null) {
            throw new FileNotFoundException("File not found.");
        }

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

    @GetMapping("/files/delete/{fileId}")
    public String deleteFile(@PathVariable Integer fileId, Authentication authentication, Model model)
            throws AccessDeniedException, FileNotFoundException {
        String username = authentication.getName();
        User user = userService.getUser(username);
        if (user == null) {
            throw new AccessDeniedException("User not found: " + username);
        }

        model.addAttribute("isSuccess", fileService.deleteFile(fileId, user.getUserId()));
        return "result";
    }
}
