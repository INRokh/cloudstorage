package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialsForm;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialsService {
    private CredentialsMapper credentialsMapper;
    private EncryptionService encryptionService;

    public CredentialsService(CredentialsMapper credentialsMapper, EncryptionService encryptionService) {
        this.credentialsMapper = credentialsMapper;
        this.encryptionService = encryptionService;
    }

    public void addCredentials(CredentialsForm credentialsForm, Integer userId) {
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
        newCredentials.setDecryptedPassword(encryptionService.decryptValue(encryptedPassword, encodedKey));

        credentialsMapper.addCredentials(newCredentials);
    }

    public void updateCredentials(CredentialsForm credentialsForm) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credentialsForm.getPassword(), encodedKey);


        Credentials credentials = credentialsMapper.getCredentialById(credentialsForm.getCredentialId());
        if (credentials == null) {
            return;
        }
        credentials.setUrl(credentialsForm.getUrl());
        credentials.setPassword(encryptedPassword);
        credentials.setKey(encodedKey);
        credentials.setUsername(credentialsForm.getUsername());
        credentials.setDecryptedPassword(encryptionService.decryptValue(encryptedPassword, encodedKey));

        credentialsMapper.updateCredentials(credentials);
    }

    public void deleteCredentials(Integer credentialId) {
        credentialsMapper.delete(credentialId);
    }

    public List<Credentials> getCredentials(Integer userId) {
        return credentialsMapper.getUserCredentials(userId);
    }
}
