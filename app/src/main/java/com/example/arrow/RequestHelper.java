package com.example.arrow;

public class RequestHelper {

    String reqName;
    String reqCollege;
    String reqDescription;

    public RequestHelper() {

    }

    public RequestHelper(String reqName, String reqCollege, String reqDescription) {
        this.reqName = reqName;
        this.reqCollege = reqCollege;
        this.reqDescription = reqDescription;
    }

    public String getReqName() {
        return reqName;
    }

    public void setReqName(String reqName) {
        this.reqName = reqName;
    }

    public String getReqCollege() {
        return reqCollege;
    }

    public void setReqCollege(String reqCollege) {
        this.reqCollege = reqCollege;
    }

    public String getReqDescription() {
        return reqDescription;
    }

    public void setReqDescription(String reqDescription) {
        this.reqDescription = reqDescription;
    }
}
