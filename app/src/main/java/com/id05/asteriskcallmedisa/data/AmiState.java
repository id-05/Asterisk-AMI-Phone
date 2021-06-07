package com.id05.asteriskcallmedisa.data;

public class AmiState {

    public AmiState(){

    }

    Boolean ResultOperation;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String action;

    public String instruction;

    public Boolean getResultOperation() {
        return ResultOperation;
    }

    public void setResultOperation(Boolean resultOperation) {
        ResultOperation = resultOperation;
    }

    public void setDescription(String description) {
        Description = description;
    }

    String Description;
}
