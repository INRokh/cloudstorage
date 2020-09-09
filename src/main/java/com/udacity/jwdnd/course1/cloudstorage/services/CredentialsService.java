package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialsForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialsService {
    private Logger logger = LoggerFactory.getLogger(EncryptionService.class);

    private CredentialsMapper credentialsMapper;
    private EncryptionService encryptionService;

    public CredentialsService(CredentialsMapper credentialsMapper, EncryptionService encryptionService) {
        this.credentialsMapper = credentialsMapper;
        this.encryptionService = encryptionService;
    }

    /**
     * Store credentials in the database securely
     *
     * @param  credentialsForm  credentials data
     * @param  userId id of the owner of the credentials
     * @return <code>true</code> if successfully added credentials;
     *         <code>false</code> if error occurred
     */
    public boolean addCredentials(CredentialsForm credentialsForm, Integer userId) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credentialsForm.getPassword(), encodedKey);

        Credentials newCredentials = new Credentials();
        newCredentials.setUserId(userId);
        newCredentials.setUrl(credentialsForm.getUrl());
        newCredentials.setUsername(credentialsForm.getUsername());
        newCredentials.setPassword(encryptedPassword);
        newCredentials.setKey(encodedKey);

        int id = credentialsMapper.addCredentials(newCredentials);
        if (id == 0) {
            logger.error("Could not save credentials to database.");
            return false;
        }
        return true;
    }

    /**
     * Update existing credentials in the database.
     *
     * @param  credentialsForm  credentials data
     * @param  userId id of the owner of the credentials
     * @return <code>true</code> if successfully updated credentials;
     *         <code>false</code> if credentials are not updated
     */
    public boolean updateCredentials(CredentialsForm credentialsForm, Integer userId) {
        Credentials credentials = credentialsMapper.getUserCredentialsById(credentialsForm.getCredentialId(), userId);
        if (credentials == null) {
            logger.error("Could not find credentials id {} for userid  {}.", credentialsForm.getCredentialId(), userId);
            return false;
        }

        String encryptedPassword = encryptionService.encryptValue(credentialsForm.getPassword(), credentials.getKey());
        credentials.setUrl(credentialsForm.getUrl());
        credentials.setPassword(encryptedPassword);
        credentials.setUsername(credentialsForm.getUsername());

        int updatedRecords = credentialsMapper.updateCredentials(credentials);
        if (updatedRecords != 1) {
            logger.error("Could not update credentials id {} for userid {}: ", credentials.getCredentialId(), userId);
            return false;
        }
        return true;
    }

    /**
     * Delete existing credentials in the database.
     *
     * @param  credentialsId
     * @param  userId id of the owner of the credentials
     * @return <code>true</code> if successfully deleted credentials;
     *         <code>false</code> if credentials are not deleted
     */
    public boolean deleteCredentials(Integer credentialsId, Integer userId) {
        int deletedRecords = credentialsMapper.delete(credentialsId, userId);
        if (deletedRecords != 1) {
            logger.error("Could not delete credentials id {} for userid {}: ", credentialsId, userId);
            return false;
        }
        return true;
    }

    /**
     * Loads and decrypt credentials
     *
     * @param  userId id of the owner of the credentials
     * @return credentialsList of decrypted credentials
     */
    public List<Credentials> getDecryptedCredentials(Integer userId) {
        List<Credentials> credentialsList = credentialsMapper.getUserCredentials(userId);
        for (Credentials credentials: credentialsList) {
            String decryptedPassword = encryptionService.decryptValue(credentials.getPassword(),credentials.getKey());
            credentials.setDecryptedPassword(decryptedPassword);
        }
        return credentialsList;
    }
}
