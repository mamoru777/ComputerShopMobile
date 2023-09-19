package com.example.computershopmobile.Models;

import java.util.UUID;

public class ResponseUser {
    public UUID Id;
    public boolean isExist;
    public String Role;

    public ResponseUser() {}

    public UUID getId() {
        return Id;
    }

    public Boolean getIsExist() {
        return isExist;
    }
    public String getRole(){return Role;}
    public void setId(UUID id) {
        this.Id = id;
    }
    public void setIsExist(Boolean isExist) {
        this.isExist = isExist;
    }
    public void setRole(String Role){this.Role = Role;}
}
