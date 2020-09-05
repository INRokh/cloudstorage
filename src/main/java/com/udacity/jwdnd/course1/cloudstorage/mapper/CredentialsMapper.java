package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialsMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialId} AND userid = #{userId}")
    Credentials getUserCredentialsById(Integer credentialId, Integer userId);

    @Insert("INSERT INTO CREDENTIALS(credentialid, url, username, key, password, userid) " +
            "VALUES(#{credentialId}, #{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int addCredentials(Credentials credentials);

    @Update("UPDATE credentials " +
            "SET url = #{url}, username = #{username}, password = #{password}, key = #{key} " +
            "WHERE credentialId = #{credentialId} AND userid = #{userId}")
    int updateCredentials(Credentials credentials);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId} AND userid = userId")
    int delete(Integer credentialId, Integer userId);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    List<Credentials> getUserCredentials(Integer userId);
}