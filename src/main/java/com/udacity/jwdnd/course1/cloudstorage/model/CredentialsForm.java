package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class CredentialsForm {
    private Integer credentialId;
    private String url;
    private String username;
    private String password;

    public CredentialsForm(Integer credentialId, String url, String username, String password) {
        this.credentialId = credentialId;
        this.url = url;
        this.username = username;
        this.password = password;
    }
}
