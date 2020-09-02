package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public void addFile(MultipartFile file, Integer userId) {
        File newFile = new File();
        newFile.setUserId(userId);
        newFile.setFileName(file.getOriginalFilename());
        newFile.setContentType(file.getContentType());
        newFile.setFileSize(String.valueOf(file.getSize()));
        try {
            newFile.setFileData(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileMapper.addFile(newFile);
    }

    public List<File> getFiles(Integer userId) {
        return fileMapper.getUserFiles(userId);
    }
}
