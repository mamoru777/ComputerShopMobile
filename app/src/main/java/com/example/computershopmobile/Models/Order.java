package com.example.computershopmobile.Models;

import java.util.UUID;

public class Order {
    private UUID Id;
    private float Summ;
    private String City;
    private String Adress;
    private String Phone;
    private String Status;
    private Boolean IsPaid;
    private UUID UserId;

    public Order(){}

    public void setId(UUID id) {this.Id = id;}
    public void setSumm(float summ) {this.Summ = summ;}
    public void setCity(String city) {this.City = city;}
    public void setAdress(String adress) {this.Adress = adress;}
    public void setPhone(String phone) {this.Phone = phone;}
    public void setStatus(String status) {this.Status = status;}
    public void setIsPaid(Boolean isPaid) {this.IsPaid = isPaid;}
    public void setUserId(UUID userId) {this.UserId = userId;}

    public UUID getId(){return Id;}
    public float getSumm(){return Summ;}
    public String getCity(){return City;}
    public String getAdress(){return Adress;}
    public String getPhone(){return Phone;}
    public String getStatus(){return Status;}
    public Boolean getIsPaid(){return IsPaid;}
    public UUID getUserId(){return UserId;}
}
