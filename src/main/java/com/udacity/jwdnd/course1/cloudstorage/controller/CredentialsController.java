package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CredentialsController {
    private UserService userService;
    private CredentialsService credentialsService;

    public CredentialsController(UserService userService, CredentialsService credentialsService) {
        this.userService = userService;
        this.credentialsService = credentialsService;
    }

    @PostMapping("/credentials")
    public String addOrUpdateCredentials(Authentication authentication, CredentialsForm credentialsForm, Model model)
            throws AccessDeniedException {
        String username = authentication.getName();
        User user = userService.getUser(username);
        if(user == null) {
            throw new AccessDeniedException("User not found: " + username);
        }

        boolean isSuccess = credentialsForm.getCredentialId() == null ?
                credentialsService.addCredentials(credentialsForm, user.getUserId()) :
                credentialsService.updateCredentials(credentialsForm, user.getUserId());
        model.addAttribute("isSuccess", isSuccess);
        return "result";
    }

    @GetMapping("/credentials/{credentialId}")
    public String deleteCredentials(@PathVariable Integer credentialId, Authentication authentication, Model model)
            throws AccessDeniedException {
        String username = authentication.getName();
        User user = userService.getUser(username);
        if(user == null) {
            throw new AccessDeniedException("User not found: " + username);
        }

        boolean isSuccess = credentialsService.deleteCredentials(credentialId, user.getUserId());
        model.addAttribute("isSuccess", isSuccess);
        return "result";
    }
}
