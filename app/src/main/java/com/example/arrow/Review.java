package com.example.arrow;

public class Review {

    private String fname;
    private String lname;
    private int sync;
    private int attendance;
    private int grading;
    private float overall;
    private String comment;
    private String UID;
    private String college;

    public Review(String fname, String lname, int sync, int attendance, int grading, float overall, String comment, String UID, String college) {
        this.fname = fname;
        this.lname = lname;
        this.sync = sync;
        this.attendance = attendance;
        this.grading = grading;
        this.overall = overall;
        this.comment = comment;
        this.UID = UID;
        this.college = college;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public int getSync() {
        return sync;
    }

    public int getAttendance() {
        return attendance;
    }

    public int getGrading() {
        return grading;
    }

    public float getOverall() {
        return overall;
    }

    public String getComment() {
        return comment;
    }

    public String getUID() {
        return UID;
    }

    public String getCollege() {
        return college;
    }
}
