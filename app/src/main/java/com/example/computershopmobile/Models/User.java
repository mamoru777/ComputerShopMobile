package com.example.computershopmobile.Models;

public class User
{
    public int Id;
    public String Login;
    public String Password;
    public String Name;
    public String LastName;
    public String SurName;
    public String Email;
    public byte[] Avatar;

    public User(){}

    public void setLogin(String Login){this.Login = Login;}

    public void setPassword(String Password){this.Password = Password;}

    public void setName(String Name){this.Name = Name;}

    public void setLastName(String LastName){this.LastName = LastName;}

    public void setSurName(String SurName){this.SurName = SurName;}

    public void setEmail(String Email){this.Email = Email;}

    public void setAvatar(byte[] Avatar){this.Avatar = Avatar;}
}
