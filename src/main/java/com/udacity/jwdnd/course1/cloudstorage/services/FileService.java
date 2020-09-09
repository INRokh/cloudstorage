package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private Logger logger = LoggerFactory.getLogger(EncryptionService.class);
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    /**
     * Store a file in the database.
     *
     * @param  file  file data
     * @param  userId id of the owner of the file
     * @return <code>true</code> if successfully added file;
     *         <code>false</code> if error occurred
     */
    public boolean addFile(MultipartFile file, Integer userId) {
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

        int id = fileMapper.addFile(newFile);
        if (id == 0) {
            logger.error("Could not save file to database.");
            return false;
        }
        return true;
    }

    /**
     * Loads file by filename and userid.
     *
     * @param  fileName
     * @param  userId id of the owner of the file
     * @return file
     */
    public File getFileByNameAndUserId(String fileName, Integer userId) {
        File file = fileMapper.getFileByNameAndUserId(fileName, userId);
        if(file == null) {
            logger.error("Could not find file filename {} for userid {}.", fileName, userId);
        }
        return file;
    }

    /**
     * Loads file by fileid and userid.
     *
     * @param  fileId
     * @param  userId id of the owner of the file
     * @return file
     */
    public File getFileByIdAndUserId(Integer fileId, Integer userId) {
        return fileMapper.getFileByIdAndUserId(fileId, userId);
    }

    /**
     * Delete existing file in the database.
     *
     * @param  fileId
     * @param  userId id of the owner of the credentials
     * @return <code>true</code> if successfully deleted file;
     *         <code>false</code> if file are not deleted
     */
    public boolean deleteFile(Integer fileId, Integer userId) {
        int deletedRecords = fileMapper.delete(fileId, userId);
        if (deletedRecords != 1) {
            logger.error("Could not delete file id {} for userid {}: ", fileId, userId);
            return false;
        }
        return true;
    }

    /**
     * Loads files
     *
     * @param  userId id of the owner of the files
     * @return list of files
     */
    public List<File> getFiles(Integer userId) {
        return fileMapper.getUserFiles(userId);
    }
}
