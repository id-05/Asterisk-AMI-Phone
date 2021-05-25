package com.id05.asteriskcallmedisa;

import java.util.Date;

public class Call {

    public String name;
    public String number;
    public String callDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public Call(String name, String number, String date){
        this.name = name;
        this.number = number;
        this.callDate = date;
    }
}
