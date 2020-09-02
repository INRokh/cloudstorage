package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CredentialsController {
    private Logger logger = LoggerFactory.getLogger(CredentialsController.class);
    private UserService userService;
    private CredentialsMapper credentialsMapper;
    private CredentialsService credentialsService;
    private EncryptionService encryptionService;

    public CredentialsController(UserService userService, CredentialsMapper credentialsMapper, CredentialsService credentialsService, EncryptionService encryptionService) {
        this.userService = userService;
        this.credentialsMapper = credentialsMapper;
        this.credentialsService = credentialsService;
        this.encryptionService = encryptionService;
    }

    @PostMapping("/credentials")
    public String addOrUpdateCredentials(Authentication authentication, CredentialsForm credentialsForm, Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);

        if(user == null) {
            logger.error("user not found");
            model.addAttribute("isSuccess", false);
            model.addAttribute("error", "User is not found. Try again.");
            return "result";
        }

        if (credentialsForm.getCredentialId() == null) {
            credentialsService.addCredentials(credentialsForm, user.getUserId());

        } else {
            credentialsService.updateCredentials(credentialsForm);
        }
        model.addAttribute("isSuccess", true);

        return "result";
    }

    @GetMapping("/credentials/{credentialId}")
    public String deleteCredentials(@PathVariable Integer credentialId, CredentialsForm credentialsForm, Authentication authentication, Model model) {
        credentialsService.deleteCredentials(credentialId);
        model.addAttribute("isSuccess", true);
        return "result";
    }
}
