package com.example.computershopmobile;

public class IpAdress {
    private String Ip = "http://5.167.57.72:13999";
    private static IpAdress instance;

    public static synchronized IpAdress getInstance() {
        if (instance == null) {
            instance = new IpAdress();
        }
        return instance;
    }

    public IpAdress(){}
    public String getIp() {return Ip;}
    public void setIp(String IpAdress){Ip = IpAdress;}
}
