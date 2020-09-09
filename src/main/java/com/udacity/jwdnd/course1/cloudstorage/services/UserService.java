package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final HashService hashService;

    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    /**
     * Check if username is available.
     *
     * @param  username
     * @return <code>true</code> if username is available; <code>false</code> if username is already exists
     */
    public boolean isUsernameAvailable(String username) {
        return userMapper.getUser(username) == null;
    }

    /**
     * Create user in the database.
     *
     * @param  user user data
     * @return id of created user
     */
    public int createUser(User user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        return userMapper.insert(new User(null, user.getUsername(), encodedSalt, hashedPassword, user.getFirstName(), user.getLastName()));
    }

    /**
     * Load user data.
     *
     * @param  username user data
     * @return user data
     */
    public User getUser(String username) {
        return userMapper.getUser(username);
    }

}
