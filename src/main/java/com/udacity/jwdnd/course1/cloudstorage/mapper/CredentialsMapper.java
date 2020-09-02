package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialsMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    Credentials getCredentialById(Integer credentialId);

    @Insert("INSERT INTO CREDENTIALS(credentialid, url, username, key, password, userid, decryptedpassword) VALUES(#{credentialId}, #{url}, #{username}, #{key}, #{password}, #{userId}, #{decryptedPassword})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int addCredentials(Credentials credentials);

    @Update("UPDATE credentials " +
            "SET url = #{url}, username = #{username}, password = #{password}, key = #{key}, decryptedPassword = #{decryptedPassword} " +
            "WHERE credentialId = #{credentialId}")
    int updateCredentials(Credentials credentials);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    void delete(Integer noteId);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    List<Credentials> getUserCredentials(Integer userId);
}