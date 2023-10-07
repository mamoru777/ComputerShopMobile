package com.example.computershopmobile.Models;

import java.util.UUID;

public class Good {
    private UUID Id;
    private String GoodType;
    private String Name;
    private String Description;
    private float Price;
    private byte[] Avatar;

    public void setId(UUID id){this.Id = id;}
    public void setName(String Name){this.Name = Name;}
    public void setAvatar(byte[] Avatar){this.Avatar = Avatar;}
    public void setGoodType(String GoodType){this.GoodType = GoodType;}
    public void setDescription(String Description){this.Description = Description;}
    public void setPrice(float Price){this.Price = Price;}
    public UUID getId() {
        return Id;
    }
    public String getName() {
        return Name;
    }
    public byte[] getAvatar() {
        return Avatar;
    }
    public String getGoodType(){return GoodType;}
    public String getDescription(){return Description;}
    public float getPrice(){return Price;}
}
