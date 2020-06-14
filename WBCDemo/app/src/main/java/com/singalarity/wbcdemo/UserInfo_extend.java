package com.singalarity.wbcdemo;


import java.io.Serializable;

public class UserInfo_extend implements Serializable {
    private String hashPassword;
    private String uid;

    public UserInfo_extend(String uid2, String hashPassword2) {
        this.uid = uid2;
        this.hashPassword = hashPassword2;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid2) {
        this.uid = uid2;
    }

    public String getHashPassword() {
        return this.hashPassword;
    }

    public void setHashPassword(String hashPassword2) {
        this.hashPassword = hashPassword2;
    }
}