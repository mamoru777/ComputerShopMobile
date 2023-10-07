package com.example.computershopmobile.Models;

import java.util.UUID;

public class User
{
    private UUID Id;
    private String Login;
    private String Password;
    private String Name;
    private String LastName;
    private String SurName;
    private String Email;
    private byte[] Avatar;

    private String Role;

    public User(){}

    public void setId(UUID id){this.Id = id;}

    public void setLogin(String Login){this.Login = Login;}

    public void setPassword(String Password){this.Password = Password;}

    public void setName(String Name){this.Name = Name;}

    public void setLastName(String LastName){this.LastName = LastName;}

    public void setSurName(String SurName){this.SurName = SurName;}

    public void setEmail(String Email){this.Email = Email;}

    public void setAvatar(byte[] Avatar){this.Avatar = Avatar;}

    public void setRole(String role){this.Role = role;}
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
    public String getRole() {return Role;}
}
