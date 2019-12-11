package com.cosmos.photonim.imbase;

public class LoginInfo {
    private String tokenId;
    private String sessenId;
    private String userId;
    private String icon;

    private LoginInfo() {
    }

    public void setTokenId(String token) {
        this.tokenId = token;
    }

    public String getTokenId() {
        return tokenId;
    }

    public String getIcon() {
        return icon;
    }

    private static class LoginInfoHolder {
        static LoginInfo loginInfo = new LoginInfo();
    }

    public static LoginInfo getInstance() {
        return LoginInfoHolder.loginInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMyIcon(String avatar) {
        this.icon = avatar;
    }

    public String getSessenId() {
        return sessenId;
    }

    public void setSessenId(String sessenId) {
        this.sessenId = sessenId;
    }
}
