package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE filename = #{fileName} AND userid = #{userId}")
    File getFileByNameAndUserId(String fileName, Integer userId);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId} AND userid = #{userId}")
    File getFileByIdAndUserId(Integer fileId, Integer userId);

    @Insert("INSERT INTO FILES(filename, contenttype, filesize, userid, filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int addFile(File file);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileId} AND userid = #{userId}")
    int delete(Integer fileId, Integer userId);

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getUserFiles(Integer userId);
}