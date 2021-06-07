package com.id05.asteriskcallmedisa.data;

public class Call {

    public String name;
    public String number;
    public String callDate;

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getCallDate() {
        return callDate;
    }

    public Call(String name, String number, String date){
        this.name = name;
        this.number = number;
        this.callDate = date;
    }
}
