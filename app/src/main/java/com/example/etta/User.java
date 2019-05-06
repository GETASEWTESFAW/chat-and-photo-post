package com.example.etta;

public class User {
    private String email;
    private String name;
    private String password;
    private  String uid;
   public User(){

   }
   public User(String email,String name,String password,String uid){
       this.email=email;
       this.name=name;
       this.password=password;
       this.uid=uid;
   }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
