package com.example.model;

public abstract class Place {
    //Не обращаем внимание...
    private static int last_id = 0;
    private int id;
    private String loginOwner;
    
    public Place(String loginOwner) {
        this.id = ++last_id;
        this.loginOwner = loginOwner;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ");
        sb.append(id);
        sb.append("Логин собственника: ");
        sb.append(loginOwner);
        return sb.toString();
    }

    public String getLoginOwner() {
        return loginOwner;
    }

}
