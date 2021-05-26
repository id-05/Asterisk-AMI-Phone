package com.id05.asteriskcallmedisa;

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

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    String action;
    String instruction;

    public Boolean getResultOperation() {
        return ResultOperation;
    }

    public void setResultOperation(Boolean resultOperation) {
        ResultOperation = resultOperation;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    String Description;
}
