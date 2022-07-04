package com.williambastos.openevents.model;

import java.io.Serializable;

public class Login implements Serializable {
    private String email;
    private String password;
    private String accessToken;

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
