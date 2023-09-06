package com.example.computershopmobile;

import java.util.UUID;

public class ResponseUser {
    public UUID Id;
    public boolean isExist;

    public ResponseUser() {}

    public UUID getId() {
        return Id;
    }

    public Boolean getIsExist() {
        return isExist;
    }

    public void setId(UUID id) {
        this.Id = id;
    }

    public void setIsExist(Boolean isExist) {
        this.isExist = isExist;
    }
}
