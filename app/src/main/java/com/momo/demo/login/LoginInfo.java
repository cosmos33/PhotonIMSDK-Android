package com.momo.demo.login;

public class LoginInfo {
    private static final LoginInfo ourInstance = new LoginInfo();
    private String token;
    private String userId;
    private String sessionId;

    public static LoginInfo getInstance() {
        return ourInstance;
    }


    private LoginInfo() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setTokenId(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
