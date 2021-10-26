package com.example.arrow;

public class CommentHelperClass {
    String fName;
    String lName;
    String course;

    String learning;
    String attendance;
    String grading;
    String review;
    String studentID;
    float rating;

    public CommentHelperClass(String studentID, String fName, String lName, String course, String learning, String attendance, String grading, String review) {
        this.studentID = studentID;
        this.fName = fName;
        this.lName = lName;
        this.course = course;
        this.learning = learning;
        this.attendance = attendance;
        this.grading = grading;
        this.review = review;
    }

    public CommentHelperClass(String studentID, String fName, String lName, String course, int rLearning, int rAttendance, int rGrade, float rating, String review) {
        this.studentID = studentID;
        this.fName = fName;
        this.lName = lName;
        this.course = course;
        this.rating = rating;

        if(rLearning == 1)
            this.learning = "Pure Asynchronous";
        if(rLearning == 2)
            this.learning = "More Asynchronous";
        if(rLearning == 3)
            this.learning = "Balanced";
        if(rLearning == 4)
            this.learning = "More Synchronous";
        if(rLearning == 5)
            this.learning = "Pure Synchronous";


        if(rAttendance == 1)
            this.attendance = "Attendance Not Required";
        if(rAttendance == 2)
            this.attendance = "Attendance Encouraged";
        if(rAttendance == 3)
            this.attendance = "Attendance Graded";
        if(rAttendance == 4)
            this.attendance = "Important Attendance";
        if(rAttendance == 5)
            this.attendance = "Attendance Required";

        if(rGrade == 1)
            this.grading = "Pure Output-Based";
        if(rGrade == 2)
            this.grading = "More Output-Based";
        if(rGrade == 3)
            this.grading = "Balanced Exams and Output";
        if(rGrade == 4)
            this.grading = "More Exams";
        if(rGrade == 5)
            this.grading = "Pure Exams";

        this.review = review;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getLearning() {
        return learning;
    }

    public void setLearning(String learning) {
        this.learning = learning;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getGrading() {
        return grading;
    }

    public void setGrading(String grading) {
        this.grading = grading;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
