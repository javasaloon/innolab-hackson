package com.innolab.hackson;

public class ChaincodeData {

    public String operation = "init";

    public String triger = "triger tx";

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getTriger() {
        return triger;
    }

    public void setTriger(String triger) {
        this.triger = triger;
    }
}
