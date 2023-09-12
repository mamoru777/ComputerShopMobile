package com.example.computershopmobile.Models;

import java.util.UUID;

public class User
{
    public UUID Id;
    public String Login;
    public String Password;
    public String Name;
    public String LastName;
    public String SurName;
    public String Email;
    public byte[] Avatar;

    public User(){}

    public void setId(UUID id){this.Id = id;}

    public void setLogin(String Login){this.Login = Login;}

    public void setPassword(String Password){this.Password = Password;}

    public void setName(String Name){this.Name = Name;}

    public void setLastName(String LastName){this.LastName = LastName;}

    public void setSurName(String SurName){this.SurName = SurName;}

    public void setEmail(String Email){this.Email = Email;}

    public void setAvatar(byte[] Avatar){this.Avatar = Avatar;}
    public UUID getId() {
        return Id;
    }
    public String getLogin() {
        return Login;
    }
    public String getPassword() {
        return Password;
    }
    public String getName() {
        return Name;
    }
    public String getLastName() {
        return LastName;
    }
    public String getSurName() {
        return SurName;
    }
    public String getEmail() {
        return Email;
    }
    public byte[] getAvatar() {
        return Avatar;
    }
}
